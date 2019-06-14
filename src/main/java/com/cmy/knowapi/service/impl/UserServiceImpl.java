package com.cmy.knowapi.service.impl;

import com.cmy.knowapi.mapper.RoleMapper;
import com.cmy.knowapi.mapper.UserInfoMapper;
import com.cmy.knowapi.mapper.UserMapper;
import com.cmy.knowapi.model.*;
import com.cmy.knowapi.service.MenuService;
import com.cmy.knowapi.service.RoleService;
import com.cmy.knowapi.service.UserService;
import com.cmy.knowapi.util.Md5Util;
import com.cmy.util.OSSOperate;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

@Service
@CacheConfig(cacheNames = "userCache")
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    UserInfoMapper userInfoMapper;
    @Autowired
    RoleMapper roleMapper;
    @Autowired
    RoleService roleService;
    @Autowired
    MenuService menuService;

    @Override
    @Cacheable(key = "'user_'+#userName", unless = "#result == null")
    public User findUserByName(String userName) {
        Example example = new Example(User.class);
        example.createCriteria().andEqualTo("userName", userName);
        User user = userMapper.selectOneByExample(example);
        String avatar = "default_handsome.jpg";
        Integer udid = userInfoMapper.selectUdidByUid(user.getId());
        if (udid != null && "".equals(udid)) {
            UserInfo userInfo = userInfoMapper.selectByPrimaryKey(udid);
            avatar = userInfo.getAvatar();
        }
        user.setAvatar(OSSOperate.getSafeURL(avatar).toString());

        if (user != null) {
            List<Role> roleList = userMapper.findRole(user.getId());
            Iterator<Role> it = roleList.iterator();
            while (it.hasNext()) {
                Role role = it.next();
                List<Permission> permissionList = roleMapper.findPermission(role.getId());
                role.setPermissionList(permissionList);
                List<Menu> menuList = menuService.finfMenusByRole(role);
                role.setMenuList(new HashSet<>(menuList));
            }
            user.setRoleList(roleList);
        }
        return user;
    }

    @Override
    @Cacheable(key = "'user_'+#uid", unless = "#result==null")
    public User findUserById(Integer uid) {
        User user = userMapper.selectByPrimaryKey(uid);
        return user;
    }

    @Override
    @Cacheable(key = "'uid_'+#eid", unless = "#result==null")
    public Integer selectUidByEid(Integer eid) {
        Integer uid = userMapper.selectUidByEid(eid);
        return uid;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertUser(User user) {
        boolean succeed = false;
        String userName = user.getUserName();
        User hasUser = findUserByName(userName);
        if (hasUser == null) {
            int rows = userMapper.insert(user);
            Role role = roleService.findRoleByCode("user");
            if (rows == 1) {
                userMapper.insertUserRole(user.getId(), role.getId());
                succeed = true;
            }
        }
        return succeed;
    }

    @Override
    public Map<String, Object> changeNowPsw(String oldPassword, String password) {
        boolean isSuccee = false;
        Map<String, Object> map = new HashMap<>();
        Subject subject = SecurityUtils.getSubject();
        String userName = (String) subject.getPrincipal();
        User user = findUserByName(userName);
        if (user != null) {
            if (user.getPassword() != null && user.getPassword().equals(Md5Util.toMd5(oldPassword, user.getCredentialsSalt()))) {
                user.setPassword(Md5Util.toMd5(password, user.getCredentialsSalt()));
                int row = userMapper.updateByPrimaryKeySelective(user);
                if (row == 1) {
                    isSuccee = true;
                } else {
                    map.put("msg", "密码更新失败,请重试");
                }
            } else {
                map.put("msg", "原密码错误,请重新输入");
            }
        } else {
            map.put("msg", "用户不存在");
        }
        if (isSuccee) {
            map.put("result", "true");
        } else {
            map.put("result", "false");
        }
        return map;
    }

    @Override
    @Cacheable(key = "'userInfo_'+#userName", unless = "#result == null")
    public UserInfo findUserInfoByName(String userName) {
        User user = findUserByName(userName);
        if (user != null) {
            Integer udid = userInfoMapper.selectUdidByUid(user.getId());
            UserInfo userInfo = userInfoMapper.selectByPrimaryKey(udid);
            return userInfo;
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertUserInfo(UserInfo userInfo) {
        boolean succeed = false;
        Subject subject = SecurityUtils.getSubject();
        String userName = (String) subject.getPrincipal();
        User user = findUserByName(userName);
        if (user == null) {
            return succeed;
        }

        Example example = new Example(UserInfo.class);
        example.createCriteria().andEqualTo("account", userInfo.getAccount());
        UserInfo userInfo1 = userInfoMapper.selectOneByExample(example);
        if (userInfo1 == null) {
            int rows = userInfoMapper.insert(userInfo);
            if (rows == 1) {
                userInfoMapper.insertUserUserDetail(user.getId(), userInfo.getId());
                succeed = true;
            }
        } else {
            userInfo.setId(userInfo1.getId());
            int row = userInfoMapper.updateByPrimaryKeySelective(userInfo);
            if (row == 1) {
                succeed = true;
            }
        }
        return succeed;
    }

    @Override
    public boolean updateStateById(Integer id, Integer state) {
        boolean isSucceed = false;
        User user = new User();
        user.setId(id);
        user.setState(state);
        int row = userMapper.updateByPrimaryKeySelective(user);
        if (row == 1) {
            isSucceed = true;
        }
        return isSucceed;
    }

    @Override
    @Cacheable(key = "'userInfo_'+#param", unless = "#result==null")
    public List<UserInfo> queryByName(String param) {
        return userInfoMapper.selectUserInfoByName(User.DELETE_STATE, param);
    }

    @Override
    public boolean pswRest(Integer uid) {
        User user = userMapper.selectByPrimaryKey(uid);
        if (user != null) {
            user.setPassword(Md5Util.toMd5("111111", user.getCredentialsSalt()));
            int row = userMapper.updateByPrimaryKeySelective(user);
            if (row == 1) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean delUser(Integer uid) {
        boolean isSuccess = false;
        User user = new User();
        user.setId(uid);
        user.setState(User.DELETE_STATE);
        if (userMapper.updateByPrimaryKeySelective(user) == 1) {
            isSuccess = true;
        }
        return isSuccess;
    }

    @Override
    public boolean checkisNormalUser(User user) {
        boolean isSuccess = false;
        List<Role> roleList = user.getRoleList();
        if (roleList != null) {
            for (Role role : roleList) {
                if (Role.NORMAL_USER.equals(role.getRole()) && roleList.size() == 1) {
                    isSuccess = true;
                    break;
                }
            }
        }
        return isSuccess;
    }

    @Override
    public List<Role> roleList(Integer uid) {
        return userMapper.findRole(uid);
    }

    @Override
    public List<UserInfo> selectUserInfoByAnswerNum() {
        List<UserInfo> userInfos = userInfoMapper.selectUserInfoByAnswerNum(User.NORMAL_STATE);
        return userInfos;
    }
}

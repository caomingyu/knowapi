package com.cmy.knowapi.controller;

import com.alibaba.fastjson.JSON;
import com.cmy.knowapi.mapper.UserInfoMapper;
import com.cmy.knowapi.model.User;
import com.cmy.knowapi.model.UserInfo;
import com.cmy.knowapi.service.UserService;
//import com.cmy.util.OSSOperate;
import com.cmy.ossutil.util.OssUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class UserController {
    @Autowired
    UserInfoMapper userInfoMapper;
    @Autowired
    UserService userService;

    @RequestMapping("/user/list")
    @ResponseBody
    public Object queryList(Integer pageNum, Integer limit, String param) {
        Map<String, Object> map = new HashMap<>();
        try {
            Page page = PageHelper.startPage(pageNum, limit);
            List<UserInfo> userInfos;
            if (param == null || "".equals(param)) {
                userInfos = userInfoMapper.selectUserInfo(User.DELETE_STATE);
            } else {
                userInfos = userService.queryByName(param);
            }
            PageInfo info = new PageInfo<>(page.getResult());
            map.put("code", 0);
            map.put("msg", "用户信息分页查询成功");
            map.put("count", info.getTotal());
            map.put("data", userInfos);
        } catch (Exception e) {
            map.put("code", 1);
            map.put("msg", "用户信息分页查询失败,错误信息: " + e.getMessage());
            map.put("count", 0);
            map.put("data", null);
        }
        return JSON.toJSONString(map);
    }

    @RequestMapping("/user_add")
    public String toRegist() {
        return "/system/user_add.btl";
    }

    @PostMapping("/user/tofreeze")
    @ResponseBody
    public Object tofree(Integer id, Integer state) {
        Map<String, Object> map = new HashMap(2);
        if (userService.updateStateById(id, state)) {
            map.put("data", "true");
            map.put("msg", "状态更新成功");
        } else {
            map.put("data", "false");
            map.put("msg", "状态更新失败");
        }
        return JSON.toJSONString(map);
    }

    @RequestMapping("/user/pswReset")
    @ResponseBody
    public Object pswRest(Integer id) {
        Map<String, Object> map = new HashMap<>();
        if (userService.pswRest(id)) {
            map.put("data", "true");
            map.put("msg", "密码重置成功，原始密码为111111");
        } else {
            map.put("data", "false");
            map.put("msg", "密码重置失败，请重试");
        }
        return JSON.toJSONString(map);
    }

    @PostMapping("/user/del")
    @ResponseBody
    public Object userDel(Integer id) {
        Map<String, Object> map = new HashMap<>(2);
        if (userService.delUser(id)) {
            map.put("data", "true");
            map.put("msg", "用户删除成功");
        } else {
            map.put("data", "false");
            map.put("msg", "用户删除成功失败，请重试");
        }
        return JSON.toJSONString(map);
    }

    @GetMapping("/user/roleDispatch")
    public String roleDispatch(Integer userId, Map<String, Object> map) {
        if (userId == null) {
            throw new NullPointerException("用户ID为空");
        }
        map.put("uid", userId);
        return "/system/user_roleassign.btl";
    }

    @PostMapping("/user/answerNum")
    @ResponseBody
    public Object userAnswerNum(Map<String, Object> map) {
        List<UserInfo> userInfos = userService.selectUserInfoByAnswerNum();
        String avatar = "default_handsome.jpg";
        for (UserInfo userInfo : userInfos) {
            if (null != userInfo.getAvatar() && !"".equals(userInfo.getAvatar())) {
                avatar = userInfo.getAvatar();
            }
            userInfo.setAvatar(OssUtil.getImgUrl(avatar));
        }
        map.put("data", "true");
        map.put("list", userInfos);
        return JSON.toJSONString(map);
    }

    @GetMapping("/user/home")
    public String userHome(Integer uid, Integer eid, Map<String, Object> map) {
        User user = null;
        //根据uid查看个人信息
        if (uid != null) {
            user = userService.findUserById(uid);
        }
        //根据eid查看个人信息
        if (eid != null) {
            uid = userService.selectUidByEid(eid);
            user = userService.findUserById(uid);
        }
        String userName = user.getUserName();
        UserInfo userInfo = userService.findUserInfoByName(userName);
        Subject subject = SecurityUtils.getSubject();
        userName = (String) subject.getPrincipal();
        User curUser = userService.findUserByName(userName);
        map.put("curUser", curUser);
        userInfo.setUid(user.getId());
        map.put("user", userInfo);
        return "/face/other_set.btl";
    }

    @GetMapping("/user/home/index")
    public String userHomeIndex(Integer uid, Map<String, Object> map) {
        User user = userService.findUserById(uid);
        String userName = user.getUserName();
        UserInfo userInfo = userService.findUserInfoByName(userName);
        Subject subject = SecurityUtils.getSubject();
        userName = (String) subject.getPrincipal();
        User curUser = userService.findUserByName(userName);
        userInfo.setUid(user.getId());
        map.put("curUser", curUser);
        map.put("user", userInfo);
        return "/face/other_index.btl";
    }
}

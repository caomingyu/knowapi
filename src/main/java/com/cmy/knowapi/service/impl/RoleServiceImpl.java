package com.cmy.knowapi.service.impl;

import com.cmy.knowapi.mapper.RoleMapper;
import com.cmy.knowapi.mapper.UserMapper;
import com.cmy.knowapi.model.Role;
import com.cmy.knowapi.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@CacheConfig(cacheNames = "userCache")
public class RoleServiceImpl implements RoleService {
    @Autowired
    RoleMapper roleMapper;
    @Autowired
    UserMapper userMapper;

    @Override
    @Cacheable(key = "'roleList_'+#uid", unless = "#result==null")
    public Map<String, Object> findRoleByUid(Integer uid) {
        Map<String, Object> map = new HashMap<>();
        List<Integer> choseRid = roleMapper.choseRid(uid);
        List<Role> roleList = roleMapper.selectAll();
        map.put("roles", roleList);
        map.put("chose", choseRid);
        return map;
    }

    @Override
    @Cacheable(key = "'role_'+#code", unless = "#result==null")
    public Role findRoleByCode(String code) {
        Example example = new Example(Role.class);
        example.createCriteria().andEqualTo("role", code);
        return roleMapper.selectOneByExample(example);
    }

    @Override
    public void insertUserRoleAfterCheck(Integer uid, Integer rid) {
        Integer id = userMapper.selectUserRole(uid, rid);
        if (id == null) {
            userMapper.insertUserRole(uid, rid);
        }
    }

    @Override
    public boolean insertUserRole(Integer uid, String rid) {
        boolean isSuccess = false;
        if (uid == null || rid == null || "".equals(rid)) {
            return isSuccess;
        }
        if (!rid.contains(",")) {
            insertUserRoleAfterCheck(uid, Integer.valueOf(rid));
            isSuccess = true;
        } else {
            String[] rids = rid.split(",");
            int size = rids.length;
            int count = 0;
            for (String id : rids) {
                insertUserRoleAfterCheck(uid, Integer.valueOf(id));
                count++;
            }
            if (count == size) {
                isSuccess = true;
            }
        }
        return isSuccess;
    }
}

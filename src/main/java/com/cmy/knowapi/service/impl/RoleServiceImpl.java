package com.cmy.knowapi.service.impl;

import com.alibaba.fastjson.JSON;
import com.cmy.knowapi.mapper.PermissionMapper;
import com.cmy.knowapi.mapper.RoleMapper;
import com.cmy.knowapi.mapper.UserMapper;
import com.cmy.knowapi.model.Permission;
import com.cmy.knowapi.model.Role;
import com.cmy.knowapi.model.TreeNode;
import com.cmy.knowapi.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
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
    @Autowired
    PermissionMapper permissionMapper;

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
    @Cacheable(key = "'role_tree_'+#description", unless = "#result==null")
    public Role findRoleByDescription(String description) {
        Example example = new Example(Role.class);
        example.createCriteria().andEqualTo("description", description);
        return roleMapper.selectOneByExample(example);
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

    @Override
    @Cacheable(key = "'roleTree_All'", unless = "#result==null")
    public String getRoleTree() {
        List<Role> roleList = roleMapper.selectAll();
        List<TreeNode> treeNodeList = new ArrayList<>();
        for (Role role : roleList) {
            TreeNode node = new TreeNode();
//            if (role.getPid()==0){
            node.setId(String.valueOf(role.getId()));
//            }else {
//                node.setId(String.valueOf(role.getPid())+role.getId());
//            }
            node.setTitle(role.getDescription());
            node.setParentId(String.valueOf(role.getPid()));
            node.setDescription(role.getDescription());
            treeNodeList.add(node);
        }
        StringBuilder sb = new StringBuilder("{\"status\":{\"code\":200,\"message\":\"操作成功\"},\"data\":").append(JSON.toJSONString(treeNodeList)).append("}");
        return sb.toString();
    }

    @Override
    @Cacheable(key = "'roleInfo_'+#rid", unless = "#result==null")
    public String getRoleInfo(Integer rid) {
        Role role = roleMapper.selectByPrimaryKey(rid);
        StringBuilder sb = new StringBuilder("{\"title\":\"");
        sb.append(role.getRole()).append("\",\"description\":\"").append(role.getDescription()).append("\"}");
        Map<String, Object> map = new HashMap<>();
        map.put("data", sb.toString());
        return JSON.toJSONString(map);
    }

    @Override
    @Cacheable(key = "'permissionInfo_'+#pid", unless = "#result==null")
    public String getPermissionInfo(Integer pid) {
        Permission permission = permissionMapper.selectByPrimaryKey(pid);
        StringBuilder sb = new StringBuilder("{\"title\":\"");
        sb.append(permission.getName()).append("\",\"description\":\"").append(permission.getPermission()).append("\"}");
        Map<String, Object> map = new HashMap<>();
        map.put("data", sb.toString());
        return JSON.toJSONString(map);
    }

    @Override
    @Cacheable(key = "'permissionTree_'+#rid", unless = "#result==null")
    public String getPermissionTree(Integer rid) {
        List<Permission> permissionListAll = permissionMapper.selectAll();
        List<Permission> permissionList = roleMapper.findPermission(rid);
        List<TreeNode> treeNodeList = new ArrayList<>();
        for (Permission permission : permissionListAll) {
            Map<String, Object> map = new HashMap<>();
            map.put("type", "0");
            if (isCheck(permissionList, permission)) {
                map.put("checked", "1");
            } else {
                map.put("checked", "0");
            }
            List<Map<String, Object>> check = new ArrayList<>();
            check.add(map);
            TreeNode treeNode = new TreeNode();
            treeNode.setId(String.valueOf(permission.getId()));
            treeNode.setTitle(permission.getName());
            treeNode.setDescription(permission.getPermission());
            treeNode.setParentId(String.valueOf(permission.getParentId()));
            treeNode.setCheckArr(check);
            treeNodeList.add(treeNode);
        }
        StringBuilder sb = new StringBuilder("{\"status\":{\"code\":200,\"message\":\"操作成功\"},\"data\":").append(JSON.toJSONString(treeNodeList)).append("}");
        return sb.toString();
    }

    public boolean isCheck(List<Permission> permissions, Permission permission) {
        for (Permission permission1 : permissions) {
            if (permission.getId().equals(permission1.getId())) {
                return true;
            }
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateRolePermission(Integer rid, List<Permission> permissions) {
        Example example=new Example(Permission.class);
        example.createCriteria().andEqualTo("id");
        permissionMapper.deleteByRid(rid);
        for (Permission permission:permissions){
            Integer pid=permission.getId();
            permissionMapper.insertRoleAndPermission(rid,pid);
        }
        return true;
    }
}

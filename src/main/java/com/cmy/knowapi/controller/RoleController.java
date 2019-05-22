package com.cmy.knowapi.controller;

import com.alibaba.fastjson.JSON;
import com.cmy.knowapi.mapper.PermissionMapper;
import com.cmy.knowapi.mapper.RoleMapper;
import com.cmy.knowapi.model.Permission;
import com.cmy.knowapi.model.Role;
import com.cmy.knowapi.service.RoleService;
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
public class RoleController {
    @Autowired
    RoleMapper roleMapper;
    @Autowired
    RoleService roleService;
    @Autowired
    PermissionMapper permissionMapper;
    @RequestMapping("/role/roleList")
    @ResponseBody
    public Object roleList(Integer uid) {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> result = roleService.findRoleByUid(uid);
        map.put("result", result);
        return JSON.toJSONString(map);
    }

    @PostMapping("/role/bind_user_role")
    @ResponseBody
    public Object bindUserRole(Integer uid, String rid) {
        Map<String, Object> map = new HashMap<>(2);
        if (roleService.insertUserRole(uid, rid)) {
            map.put("data", "true");
            map.put("msg", "用户增加角色成功");
        } else {
            map.put("data", "false");
            map.put("msg", "用户增加角色失败，请重试");
        }
        return JSON.toJSONString(map);
    }

    @RequestMapping("/json/role/tree")
    @ResponseBody
    public String toRoleJson() {
        return roleService.getRoleTree();
    }

    @RequestMapping("/json/role/info")
    @ResponseBody
    public String toRoleInfo(Integer rid) {
        return roleService.getRoleInfo(rid);
    }

    @RequestMapping("/json/permission/info")
    @ResponseBody
    public String toPermissionInfo(Integer pid) {
        return roleService.getPermissionInfo(pid);
    }

    @PostMapping("/role/get/description")
    @ResponseBody
    public Object getRoleByName(String description) {
        Role role = roleService.findRoleByDescription(description);
        Map<String, Object> map = new HashMap<>();
        map.put("role", role);
        map.put("data", "true");
        return JSON.toJSONString(map);
    }

    @PostMapping("/permission/tree")
    @ResponseBody
    public Object getPermissionTree(Integer rid, Map<String, Object> map) {
        return roleService.getPermissionTree(rid);
    }

    @GetMapping("/permission/tree/byRole")
    @ResponseBody
    public Object getPermissionTreeByRole(Integer rid, Map<String, Object> map) {
        return roleService.getPermissionTree(rid);
    }

    @PostMapping("/role/add")
    @ResponseBody
    public Object roleAdd(String title, String description, String parentId, Map<String, Object> map) {
        Role role = new Role();
        role.setRole(title);
        role.setDescription(description);
        if (parentId!=null&&!"".equals(parentId)){
            role.setPid(Integer.valueOf(parentId));
        }else {
            role.setPid(0);
        }
        int row = roleMapper.insert(role);
        if (row == 1) {
            map.put("data", "true");
            map.put("msg", "新增角色成功");
        } else {
            map.put("data", "false");
            map.put("msg", "新增角色失败");
        }
        return JSON.toJSONString(map);
    }
    @PostMapping("/role/edit")
    @ResponseBody
    public Object roleEdit(String nodeId,String title, String description, String parentId, Map<String, Object> map) {
        Role role = new Role();
        role.setId(Integer.valueOf(nodeId));
        role.setRole(title);
        role.setDescription(description);
        role.setPid(Integer.valueOf(parentId));
        int row = roleMapper.updateByPrimaryKeySelective(role);
        if (row == 1) {
            map.put("data", "true");
            map.put("msg", "新增角色成功");
        } else {
            map.put("data", "false");
            map.put("msg", "新增角色失败");
        }
        return JSON.toJSONString(map);
    }
    @PostMapping("/permission/add")
    @ResponseBody
    public Object permisssionAdd(String title, String description, String parentId, Map<String, Object> map) {
        Permission permission=new Permission();
        permission.setName(title);
        permission.setPermission(description);
        if (parentId!=null&&!"".equals(parentId)){
            permission.setParentId(Integer.valueOf(parentId));
        }else {
            permission.setParentId(0);
        }
        int row = permissionMapper.insert(permission);
        if (row == 1) {
            map.put("data", "true");
            map.put("msg", "新增角色成功");
        } else {
            map.put("data", "false");
            map.put("msg", "新增角色失败");
        }
        return JSON.toJSONString(map);
    }
    @PostMapping("/permission/edit")
    @ResponseBody
    public Object permisssionEdit(String nodeId,String title, String description, String parentId, Map<String, Object> map) {
        Permission permission=new Permission();
        permission.setId(Integer.valueOf(nodeId));
        permission.setName(title);
        permission.setPermission(description);
        int row = permissionMapper.updateByPrimaryKeySelective(permission);
        if (row == 1) {
            map.put("data", "true");
            map.put("msg", "新增角色成功");
        } else {
            map.put("data", "false");
            map.put("msg", "新增角色失败");
        }
        return JSON.toJSONString(map);
    }
    @PostMapping("/role/permission/update")
    @ResponseBody
    public Object updateRolePermission(Integer rid, String permissions,Map<String,Object>map){
        List<Permission> permissionList=JSON.parseArray(permissions,Permission.class);
        if (roleService.updateRolePermission(rid,permissionList)) {
            map.put("data", "true");
            map.put("msg", "角色赋权成功");
        } else {
            map.put("data", "false");
            map.put("msg", "角色赋权失败");
        }
        return JSON.toJSONString(map);
    }
}

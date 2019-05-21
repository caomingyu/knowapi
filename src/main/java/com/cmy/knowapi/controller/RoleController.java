package com.cmy.knowapi.controller;

import com.alibaba.fastjson.JSON;
import com.cmy.knowapi.mapper.RoleMapper;
import com.cmy.knowapi.model.Role;
import com.cmy.knowapi.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class RoleController {
    @Autowired
    RoleMapper roleMapper;
    @Autowired
    RoleService roleService;

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
}

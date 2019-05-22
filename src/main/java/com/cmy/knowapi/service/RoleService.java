package com.cmy.knowapi.service;

import com.cmy.knowapi.model.Permission;
import com.cmy.knowapi.model.Role;

import java.util.List;
import java.util.Map;

public interface RoleService {
    Role findRoleByCode(String code);

    Role findRoleByDescription(String description);

    Map<String, Object> findRoleByUid(Integer uid);

    boolean insertUserRole(Integer uid, String rid);

    void insertUserRoleAfterCheck(Integer uid, Integer rid);

    String getRoleTree();

    String getRoleInfo(Integer rid);

    String getPermissionInfo(Integer pid);

    String getPermissionTree(Integer rid);

    boolean updateRolePermission(Integer rid,List<Permission>permissions);
}

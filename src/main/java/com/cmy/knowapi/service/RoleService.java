package com.cmy.knowapi.service;

import com.cmy.knowapi.model.Role;

import java.util.List;
import java.util.Map;

public interface RoleService {
    Role findRoleByCode(String code);

    Map<String, Object> findRoleByUid(Integer uid);

    boolean insertUserRole(Integer uid, String rid);

    void insertUserRoleAfterCheck(Integer uid, Integer rid);
}

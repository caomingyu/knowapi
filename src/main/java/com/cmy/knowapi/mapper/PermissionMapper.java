package com.cmy.knowapi.mapper;

import com.cmy.knowapi.basemapper.MyMapper;
import com.cmy.knowapi.model.Permission;
import com.cmy.knowapi.model.Role;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermissionMapper extends MyMapper<Permission> {
}

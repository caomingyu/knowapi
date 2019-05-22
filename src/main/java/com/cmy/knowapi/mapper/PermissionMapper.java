package com.cmy.knowapi.mapper;

import com.cmy.knowapi.basemapper.MyMapper;
import com.cmy.knowapi.model.Permission;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionMapper extends MyMapper<Permission> {
    @Delete("DELETE FROM sys_role_permission WHERE rid =#{rid}")
    void deleteByRid(@Param("rid") Integer rid);

    @Insert("insert into sys_role_permission (rid,pid) values(#{rid},#{pid})")
    void insertRoleAndPermission(@Param("rid") Integer rid, @Param("pid") Integer pid);
}

package com.cmy.knowapi.mapper;

import com.cmy.knowapi.basemapper.MyMapper;
import com.cmy.knowapi.model.Permission;
import com.cmy.knowapi.model.Role;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleMapper extends MyMapper<Role> {
    List<Permission> findPermission(Integer rid);

    @Select("SELECT ur.rid FROM sys_user_role as ur LEFT JOIN sys_user as u ON u.id=ur.uid where u.id=#{uid};")
    List<Integer> choseRid(@Param("uid") Integer uid);
}

package com.cmy.knowapi.mapper;

import com.cmy.knowapi.basemapper.MyMapper;
import com.cmy.knowapi.model.Role;
import com.cmy.knowapi.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper extends MyMapper<User> {
    List<Role> findRole(Integer uid);

    @Insert("insert into sys_user_role (uid,rid) values (#{uid},#{rid});")
    void insertUserRole(@Param("uid") Integer uid, @Param("rid") Integer rid);

    @Select("SELECT ur.id FROM sys_user_role as ur WHERE ur.uid=#{uid} AND ur.rid=#{rid};")
    Integer selectUserRole(@Param("uid") Integer uid, @Param("rid") Integer rid);

    @Select("SELECT uid from sys_user_exception WHERE eid=#{eid}")
    Integer selectUidByEid(Integer eid);
}

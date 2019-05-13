package com.cmy.knowapi.mapper;

import com.cmy.knowapi.basemapper.MyMapper;
import com.cmy.knowapi.model.Role;
import com.cmy.knowapi.model.User;
import com.cmy.knowapi.model.UserInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserInfoMapper extends MyMapper<UserInfo> {
    @Insert("insert into sys_user_userdetail (uid,udid) values (#{uid},#{udid});")
    void insertUserUserDetail(@Param("uid") Integer uid, @Param("udid") Integer udid);

    @Select("SELECT u.id as uid,u.username as account ,d.id as id,d.name as name,d.sex as sex,d.email as email,d.phone as phone,u.state as state FROM sys_user as u LEFT JOIN sys_user_userdetail as ud ON ud.uid=u.id LEFT JOIN sys_user_detail as d ON d.id=ud.udid where state<>#{state}")
    List<UserInfo> selectUserInfo(@Param("state") Integer state);

    @Select("SELECT u.id as uid,u.username as account ,d.id as id,d.name as name,d.sex as sex,d.email as email,d.phone as phone,u.state as state FROM sys_user as u LEFT JOIN sys_user_userdetail as ud ON ud.uid=u.id LEFT JOIN sys_user_detail as d ON d.id=ud.udid where state<>#{state} and (u.username=#{param} or d.name=#{param} or d.phone=#{param})")
    List<UserInfo> selectUserInfoByName(@Param("state") Integer state, @Param("param") String param);

    @Select("select udid from sys_user_userdetail where uid=#{uid}")
    Integer selectUdidByUid(@Param("uid") Integer uid);
}

package com.cmy.knowapi.mapper;

import com.cmy.knowapi.basemapper.MyMapper;
import com.cmy.knowapi.model.Menu;
import com.cmy.knowapi.model.Role;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuMapper extends MyMapper<Menu> {
    List<Menu> findMenuByRoleId(Integer rid);
}

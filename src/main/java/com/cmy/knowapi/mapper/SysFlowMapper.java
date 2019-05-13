package com.cmy.knowapi.mapper;

import com.cmy.knowapi.basemapper.MyMapper;
import com.cmy.knowapi.model.SysFlow;
import com.cmy.knowapi.model.UserInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysFlowMapper extends MyMapper<SysFlow> {
    @Select("update sys_exception_exceptionFlow set fid=${fid} where eid=${eid}")
    void updateByEid(@Param("eid") Integer eid,@Param("fid") Integer fid);
}

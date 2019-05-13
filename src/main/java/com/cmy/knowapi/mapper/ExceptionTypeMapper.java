package com.cmy.knowapi.mapper;

import com.cmy.knowapi.basemapper.MyMapper;
import com.cmy.knowapi.model.ExceptionInfo;
import com.cmy.knowapi.model.ExceptionType;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface ExceptionTypeMapper extends MyMapper<ExceptionType> {
    @Select("update sys_exception_exceptionType set etid=${etid} where eid=${eid}")
    void updateByEid(@Param("eid") Integer eid, @Param("etid") Integer etid);
}

package com.cmy.knowapi.mapper;

import com.cmy.knowapi.basemapper.MyMapper;
import com.cmy.knowapi.model.ExceptionInfo;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface ExceptionInfoMapper extends MyMapper<ExceptionInfo> {
//    @Select("SELECT e.*,ef.fid as fid,et.etid as tid FROM sys_exception_info e LEFT JOIN sys_exception_exceptionFlow ef ON e.id=ef.eid LEFT JOIN sys_exception_exceptionType et ON e.id=et.eid where e.id=#{eid};")
//    ExceptionInfo selectExceptionInfoAllById_(Integer eid);
}

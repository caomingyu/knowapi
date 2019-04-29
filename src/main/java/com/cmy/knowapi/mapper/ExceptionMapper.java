package com.cmy.knowapi.mapper;

import com.cmy.knowapi.basemapper.MyMapper;
import com.cmy.knowapi.model.ExceptionInfo;
import com.cmy.knowapi.model.SystemException;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExceptionMapper extends MyMapper<SystemException> {
    @Select("SELECT e.id as eid,e.code as exceptionCode,e.flow_code as flowType,e.exception_type as exceptionType,e.state,ei.id,ei.content,ei.author,ei.create_time,ei.pass_time,ei.answer_count FROM sys_exception_info as ei LEFT JOIN sys_exception_exceptionInfo as ee ON ee.eiid=ei.id LEFT JOIN sys_exception as e ON ee.eid=e.id WHERE e.state<>#{state}")
    List<ExceptionInfo> findExceptionList(@Param("state") Integer state);

    @Select("SELECT e.id as eid,e.code as exceptionCode,e.flow_code as flowType,e.exception_type as exceptionType,e.state,ei.id,ei.content,ei.author,ei.create_time,ei.pass_time,ei.answer_count FROM sys_exception_info as ei LEFT JOIN sys_exception_exceptionInfo as ee ON ee.eiid=ei.id LEFT JOIN sys_exception as e ON ee.eid=e.id WHERE e.state<>#{state} and(ei.author=#{param} or e.code like '%${param}%')")
    List<ExceptionInfo> findExceptionListByParam(@Param("state") Integer state, @Param("param") String param);
}

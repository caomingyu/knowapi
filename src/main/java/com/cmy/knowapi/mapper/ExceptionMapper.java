package com.cmy.knowapi.mapper;

import com.cmy.knowapi.basemapper.MyMapper;
import com.cmy.knowapi.model.ExceptionInfo;
import com.cmy.knowapi.model.SystemException;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExceptionMapper extends MyMapper<SystemException> {
    @Select("SELECT e.id as eid,e.code as exceptionCode,e.flow_code as flowType,e.exception_type as exceptionType,e.state,ei.id,ei.content,ei.author,ei.create_time,ei.pass_time,ei.answer_count,ei.title FROM sys_exception_info as ei LEFT JOIN sys_exception_exceptionInfo as ee ON ee.eiid=ei.id LEFT JOIN sys_exception as e ON ee.eid=e.id WHERE e.state<>#{state}")
    List<ExceptionInfo> findExceptionList(@Param("state") Integer state);

    @Select("SELECT e.id as eid,e.code as exceptionCode,e.flow_code as flowType,e.exception_type as exceptionType,e.state,ei.id,ei.content,ei.author,ei.create_time,ei.pass_time,ei.answer_count,ei.title FROM sys_exception_info as ei LEFT JOIN sys_exception_exceptionInfo as ee ON ee.eiid=ei.id LEFT JOIN sys_exception as e ON ee.eid=e.id WHERE e.state=#{state} and e.isFinish=#{param} order by ${orderBy} desc")
    List<ExceptionInfo> findExceptionListOrderBy(@Param("state") Integer state, @Param("orderBy") String orderBy, @Param("param") String param);

    @Select("SELECT e.id as eid,e.code as exceptionCode,e.flow_code as flowType,e.exception_type as exceptionType,e.state,ei.id,ei.content,ei.author,ei.create_time ,ei.pass_time,ei.answer_count,ei.title FROM sys_exception_info as ei LEFT JOIN sys_exception_exceptionInfo as ee ON ee.eiid=ei.id LEFT JOIN sys_exception as e ON ee.eid=e.id WHERE e.state=#{state} order by ${orderBy} desc")
    List<ExceptionInfo> findExceptionListOrderByNoParam(@Param("state") Integer state, @Param("orderBy") String orderBy);


    @Select("SELECT e.id as eid,e.code as exceptionCode,e.flow_code as flowType,e.exception_type as exceptionType,e.state,ei.id,ei.content,ei.author,ei.create_time,ei.pass_time,ei.answer_count,ei.title FROM sys_exception_info as ei LEFT JOIN sys_exception_exceptionInfo as ee ON ee.eiid=ei.id LEFT JOIN sys_exception as e ON ee.eid=e.id WHERE e.state<>#{state} and(ei.author=#{param} or e.code like '%${param}%')")
    List<ExceptionInfo> findExceptionListByParam(@Param("state") Integer state, @Param("param") String param);

    @Select("SELECT e.id as eid,e.code as exceptionCode,e.flow_code as flowType,e.exception_type as exceptionType,e.state,ei.id,ei.content,ei.author,ei.create_time,ei.pass_time,ei.answer_count,ei.title,ef.fid as fid,et.etid as tid FROM sys_exception_info as ei LEFT JOIN sys_exception_exceptionInfo as ee ON ee.eiid=ei.id LEFT JOIN sys_exception as e ON ee.eid=e.id LEFT JOIN sys_exception_exceptionFlow ef ON e.id=ef.eid LEFT JOIN sys_exception_exceptionType et ON e.id=et.eid WHERE e.state<>#{state} and e.id=${eid}")
    ExceptionInfo findExceptionById(@Param("state") Integer state, @Param("eid") Integer eid);

    @Insert("insert into sys_exception_exceptionFlow(eid,fid) values(#{eid},#{fid})")
    void insertExceptionAndFlow(@Param("eid") Integer eid, @Param("fid") Integer fid);

    @Insert("insert into sys_exception_exceptionType(eid,etid) values(#{eid},#{etid})")
    void insertExceptionAndType(@Param("eid") Integer eid, @Param("etid") Integer etid);

    @Insert("insert into sys_user_exception(uid,eid) values(#{uid},#{eid})")
    void insertExceptionAndUser(@Param("eid") Integer eid, @Param("uid") Integer uid);

    @Insert("insert into sys_exception_exceptionInfo(eid,eiid) values(#{eid},#{eiid})")
    void insertExceptionAndInfo(@Param("eid") Integer eid, @Param("eiid") Integer eiid);

    @Select("select eiid from sys_exception_exceptionInfo where eid=#{eid}")
    Integer selectIdByEid(@Param("eid") Integer eid);

    @Select("SELECT e.id as eid,e.code as exceptionCode,e.flow_code as flowType,e.exception_type as exceptionType,e.state,ei.id,ei.content,ei.author,ei.create_time,ei.pass_time,ei.answer_count,ei.title FROM sys_exception_info as ei LEFT JOIN sys_exception_exceptionInfo as ee ON ee.eiid=ei.id LEFT JOIN sys_exception as e ON ee.eid=e.id WHERE e.state=#{state} and pass_time BETWEEN current_date ()-7 and sysdate() ORDER BY answer_count DESC LIMIT 8")
    List<ExceptionInfo> selectExceptionInfoByWeek(@Param("state") Integer state);
}

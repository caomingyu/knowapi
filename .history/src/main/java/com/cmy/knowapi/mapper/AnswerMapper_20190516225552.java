package com.cmy.knowapi.mapper;

import com.cmy.knowapi.basemapper.MyMapper;
import com.cmy.knowapi.model.Answer;
import com.cmy.knowapi.model.AnswerInfo;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerMapper extends MyMapper<Answer> {
        @Select("SELECT a.tittle,a.state,a.id as aid,ai.*,e.code as exceptionCode,e.flow_code as flowType,e.exception_type as exceptionType FROM sys_answer as a LEFT JOIN sys_answer_answerInfo as aa ON aa.aid=a.id LEFT JOIN sys_answer_info as ai ON ai.id=aa.aiid \n"
                        + "LEFT JOIN sys_exception_answer as ea ON ea.aid=a.id LEFT JOIN sys_exception as e ON e.id=ea.eid where a.state<>#{state}")
        List<AnswerInfo> findAnswerList(@Param("state") Integer state);

        @Select("SELECT a.tittle,a.state,a.id as aid,ai.*,e.code as exceptionCode,e.flow_code as flowType,e.exception_type as exceptionType FROM sys_answer as a LEFT JOIN sys_answer_answerInfo as aa ON aa.aid=a.id LEFT JOIN sys_answer_info as ai ON ai.id=aa.aiid \n"
                        + "LEFT JOIN sys_exception_answer as ea ON ea.aid=a.id LEFT JOIN sys_exception as e ON e.id=ea.eid where a.state<>#{state} and(ai.author=#{param} or a.tittle like '%${param}%' or e.code like '%${param}%')")
        List<AnswerInfo> findAnswerListByParam(@Param("state") Integer state, @Param("param") String param);

        @Insert("insert into sys_exception_answer(eid,aid) values(#{eid},#{aid})")
        void insertAnswerAndException(@Param("aid") Integer aid, @Param("eid") Integer eid);
        @Insert("insert into sys_answer_answerInfo(aid,aiid) values(#{aid},#{aid})")
        void insertAnswerAndInfo(@Param("aid") Integer aid, @Param("aiid") Integer aiid);
        @Insert("insert into sys_exception_answer(eid,aid) values(#{eid},#{aid})")
        void insertAnswerAndUser(@Param("aid") Integer aid, @Param("eid") Integer eid);
        

}


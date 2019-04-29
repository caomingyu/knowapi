package com.cmy.knowapi.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Table(name = "sys_exception_info")
public class ExceptionInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "exception_code")
    private String exceptionCode;
    @Column(name = "content")
    private String content;
    @Column(name = "author")
    private String author;
    @Column(name = "create_time")
    private Date createTime;
    @Column(name = "pass_time")
    private Date passTime;
    @Column(name = "answer_count")
    private Integer answerCount;
    @Transient
    private String flowType;
    @Transient
    private String exceptionType;
    @Transient
    private Integer state;
    @Transient
    private Integer eid;
    @Transient
    private List<AnswerInfo> answerInfoList;
}

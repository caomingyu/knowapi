package com.cmy.knowapi.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Table(name = "sys_answer_info")
public class AnswerInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "content")
    private String content;
    @Column(name = "author")
    private String author;
    @Column(name = "create_time")
    private Date createTime;
    @Column(name = "pass_time")
    private Date passTime;
    @Column(name = "agree_count")
    private Integer agreeCount;
    @Transient
    private String tittle;
    @Transient
    private Integer state;
    @Transient
    private Integer aid;
    @Transient
    private String flowType;
    @Transient
    private String exceptionType;
    @Transient
    private String exceptionCode;
}

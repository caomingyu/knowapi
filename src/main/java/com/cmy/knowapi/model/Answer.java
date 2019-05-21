package com.cmy.knowapi.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Table(name = "sys_answer")
public class Answer {
    public static final Integer NEED_AUDITE_STATE = 0;
    public static final Integer NORMAL_STATE = 1;
    public static final Integer DELETE_STATE = 2;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "tittle")
    private String title;
    @Column(name = "state")
    private Integer state;
}

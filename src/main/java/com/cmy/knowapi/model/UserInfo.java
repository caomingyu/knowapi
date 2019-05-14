package com.cmy.knowapi.model;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Data
@Table(name = "sys_user_detail")
public class UserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "username")
    private String account;
    @Column(name = "sex")
    private String sex;
    @Column(name = "email")
    private String email;
    @Column(name = "name")
    private String name;
    @Column(name = "birthday")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthDay;
    @Column(name = "phone")
    private String phone;

    @Column(name = "answerNum")
    private Integer answerNum;
    @Transient
    private Integer state;
    @Transient
    private Integer uid;
}

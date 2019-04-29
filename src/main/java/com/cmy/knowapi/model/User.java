package com.cmy.knowapi.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Table(name = "sys_user")
public class User {
    public static final Integer FREEZE_STATE = 0;
    public static final Integer NORMAL_STATE = 1;
    public static final Integer DELETE_STATE = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "username")
    private String userName;
    @Column(name = "password")
    private String password;
    @Column(name = "salt")
    private String salt;
    @Column(name = "state")
    private Integer state;
    @Transient
    private List<Role> roleList;

    /**
     * 密码盐.
     *
     * @return
     */
    @Transient
    public String getCredentialsSalt() {
        return this.userName + this.salt;
    }
}

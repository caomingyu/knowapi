package com.cmy.knowapi.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Data
@Table(name = "sys_role")
public class Role {
    public static final String NORMAL_USER = "user";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "role")
    private String role;
    @Column(name = "description")
    private String description;
    @Transient
    private List<Permission> permissionList;
    @Transient
    Set<Menu> menuList;
    @Transient
    private boolean chose;
}

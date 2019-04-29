package com.cmy.knowapi.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Table(name = "sys_permission")
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "name")
    private String name;
    @Column(name = "parent_id")
    private Integer parentId;
    @Column(name = "parent_ids")
    private String parentIds;
    @Column(name = "permission")
    private String permission;

}

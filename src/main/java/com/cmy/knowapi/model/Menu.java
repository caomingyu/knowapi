package com.cmy.knowapi.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Table(name = "sys_menu")
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "name_")
    private String name;
    @Column(name = "url")
    private String url;
    @Column(name = "icon")
    private String icon;
    @Column(name = "parent_id")
    private Integer parentId;
    @Column(name = "path")
    private String path;
    @Column(name = "order_")
    private String order;
    @Transient
    private List<Menu> children;

    @Override
    public boolean equals(Object obj) {
        Menu tmp = (Menu) obj;
        if (tmp.id.equals(this.id)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return this.id;
    }
}

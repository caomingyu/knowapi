package com.cmy.knowapi.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "test")
public class Test_ {
    @Id
    private Integer id;
    @Column(name = "test")
    private String test;
}

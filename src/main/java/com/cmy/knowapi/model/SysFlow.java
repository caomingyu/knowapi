package com.cmy.knowapi.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Table(name = "sys_flow")
public class SysFlow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "flow_code")
    private String flowCode;
    private String description;
    private Integer state;
    @Column(name = "parent_id")
    private Integer parentId;
    @Transient
    private SysFlow parentFlow;
    @Transient
    private List<SysFlow> childFlowLists;
    @Transient
    private boolean isChild;
}

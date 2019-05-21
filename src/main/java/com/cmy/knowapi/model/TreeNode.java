package com.cmy.knowapi.model;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class TreeNode {
    private String id;
    private String title;
    private String parentId;
    private String description;
    private List<Map<String, Object>> checkArr;
}

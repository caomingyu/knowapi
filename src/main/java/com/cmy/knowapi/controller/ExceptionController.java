package com.cmy.knowapi.controller;

import com.alibaba.fastjson.JSON;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.subject.Subject;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ExceptionController {
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public String error(Exception e) {
        Map<String, Object> map = new HashMap<>(2);
        String msg = e.getMessage();
        map.put("msg", msg);
        map.put("data", "false");
        return JSON.toJSONString(map);
    }
}

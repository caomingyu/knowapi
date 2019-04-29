package com.cmy.knowapi.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.subject.Subject;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class ExceptionController {
    @ExceptionHandler(UnauthorizedException.class)
    public String error(UnauthorizedException e, Model model) {
        Subject subject = SecurityUtils.getSubject();
        String userName = String.valueOf(subject.getPrincipal());
        String msg = e.getMessage();
//        map.put("msg", msg);
        model.addAttribute("msg", msg);
//        map.put("user", userName);
        model.addAttribute("user", userName);
        return "error.btl";
    }
}

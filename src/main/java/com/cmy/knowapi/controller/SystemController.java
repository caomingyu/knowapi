package com.cmy.knowapi.controller;

import com.alibaba.fastjson.JSON;
import com.cmy.knowapi.model.*;
import com.cmy.knowapi.service.ExceptionService;
import com.cmy.knowapi.service.FlowService;
import com.cmy.knowapi.service.TypeService;
import com.cmy.knowapi.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
public class SystemController {
    @Autowired
    UserService userService;
    @Autowired
    FlowService flowService;
    @Autowired
    TypeService typeService;
    @Autowired
    ExceptionService exceptionService;

    /**
     * 主题界面
     *
     * @return
     */
    @RequestMapping("/theme")
    public String theme() {
        return "/frame/theme.btl";
    }

    /**
     * 通知界面
     *
     * @return
     */
    @RequestMapping("/system/message")
    public String message() {
        return "/frame/message.btl";
    }

    /**
     * 欢迎界面
     *
     * @return
     */
    @RequiresRoles("admin")
    @RequestMapping("/system/welcome")
    public String welcome() {
        return "/frame/welcome.btl";
    }

    @RequestMapping("psw_change")
    public String pswChange() {
        return "/frame/password.btl";
    }

    @RequestMapping("/system/userList")
    public String userList() {
        return "/system/user.btl";
    }

    @RequestMapping("/system/exception/info")
    public String exceptionList() {
        return "/system/exception_info.btl";
    }

    @RequestMapping("/system/exception/answer")
    public String exceptionAnswerList() {
        return "/system/exception_answer.btl";
    }

    @RequestMapping("/face/user/home")
    public String userHome(Map<String, Object> map) {
        Subject subject = SecurityUtils.getSubject();
        String userName = (String) subject.getPrincipal();
        User user = userService.findUserByName(userName);
        map.put("curUser", user);
        return "/face/home.btl";
    }

    @RequestMapping("/face/user/message")
    public String userMessage(Map<String, Object> map) {
        Subject subject = SecurityUtils.getSubject();
        String userName = (String) subject.getPrincipal();
        User user = userService.findUserByName(userName);
        map.put("curUser", user);
        return "/face/message.btl";
    }

    @RequestMapping("/face/user/set")
    public String userSet(Map<String, Object> map) {
        Subject subject = SecurityUtils.getSubject();
        String userName = (String) subject.getPrincipal();
        User user = userService.findUserByName(userName);
        map.put("curUser", user);
        return "/face/set.btl";
    }

    @RequestMapping("/face/user/index")
    public String userIndex(Map<String, Object> map) {
        Subject subject = SecurityUtils.getSubject();
        String userName = (String) subject.getPrincipal();
        User user = userService.findUserByName(userName);
        map.put("curUser", user);
        return "/face/index.btl";
    }

    @RequestMapping("/face/exception/add")
    public String exceptionAdd(Map<String, Object> map) {
        Subject subject = SecurityUtils.getSubject();
        String userName = (String) subject.getPrincipal();
        User user = userService.findUserByName(userName);
        List<SysFlow> flowLists = flowService.getFlowAll();
        List<ExceptionType> typeLists = typeService.getTypeAll();
        map.put("curUser", user);
        map.put("flows", flowLists);
        map.put("types", typeLists);
        return "/face/exception_add.btl";
    }

    @PostMapping("/home")
    @ResponseBody
    public String toIndex(String finish, String orderBy, Map<String, Object> map) {
        List<ExceptionInfo> exceptionInfos;
        if (finish == null || "".equals(finish)) {
            exceptionInfos = exceptionService.findExceptionListOrderBy(orderBy, finish);
        } else if ("true".equals(finish)) {
            exceptionInfos = exceptionService.findExceptionListOrderBy(orderBy, finish);
        } else {
            exceptionInfos = exceptionService.findExceptionListOrderBy(orderBy, finish);
        }
        map.put("list", exceptionInfos);
        return JSON.toJSONString(map);
    }

}

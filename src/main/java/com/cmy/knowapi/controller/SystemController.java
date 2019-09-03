package com.cmy.knowapi.controller;

import com.alibaba.fastjson.JSON;
import com.cmy.knowapi.model.*;
import com.cmy.knowapi.service.*;
import com.cmy.ossutil.util.OssUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

//import com.cmy.util.OSSOperate;

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
    @Autowired
    AnswerService answerService;
    @Autowired
    RoleService roleService;

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

    @RequestMapping("/face/user/index/collection")
    public String userIndexCollection(Map<String, Object> map) {
        Subject subject = SecurityUtils.getSubject();
        String userName = (String) subject.getPrincipal();
        User user = userService.findUserByName(userName);
        map.put("curUser", user);
        map.put("this", "collection");
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
    public String toIndex(Integer pageNum, Integer pageSize, String finish, String orderBy, Map<String, Object> map) {
        List<ExceptionInfo> exceptionInfos;
        if (pageNum == null) {
            pageNum = 1;
        }
        if (pageSize == null) {
            pageSize = 10;
        }
        Page page = PageHelper.startPage(pageNum, pageSize);
        exceptionInfos = exceptionService.findExceptionListOrderBy(orderBy, finish);
        PageInfo info = new PageInfo<>(page.getResult());
        String avatar = "default_handsome.jpg";
        for (ExceptionInfo exceptionInfo : exceptionInfos) {
            if ("".equals(exceptionInfo.getAvatar())) {
                exceptionInfo.setAvatar(avatar);
            }
            exceptionInfo.setAvatar(OssUtil.getImgUrl(avatar));
        }
        map.put("list", exceptionInfos);
        map.put("pager", info);
        map.put("code", 0);
        return JSON.toJSONString(map);
    }

    @PostMapping("/search")
    public String search(String param, Map<String, Object> map) {
        Subject subject = SecurityUtils.getSubject();
        String userName = (String) subject.getPrincipal();
        User user = userService.findUserByName(userName);
        map.put("param", param);
        map.put("curUser", user);
        return "/face/search.btl";
    }

    @GetMapping("/answer/edit")
    public String answerEdit(Integer aid, Map<String, Object> map) {
        AnswerInfo answerInfo = answerService.getAnswerInfo(aid);
        map.put("a", answerInfo);
        return "/face/answer_edit.btl";
    }

    @RequestMapping("/system/dictionary/role")
    public String roleManage(Map<String, Object> map) {
        return "/system/role.btl";
    }
}

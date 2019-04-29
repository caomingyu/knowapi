package com.cmy.knowapi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SystemController {
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
}

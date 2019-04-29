package com.cmy.knowapi.controller;

import com.alibaba.fastjson.JSON;
import com.cmy.knowapi.model.Menu;
import com.cmy.knowapi.model.Role;
import com.cmy.knowapi.model.User;
import com.cmy.knowapi.model.UserInfo;
import com.cmy.knowapi.service.UserService;
import com.cmy.knowapi.util.Md5Util;
import com.cmy.knowapi.util.ShiroSaltUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Controller
@Slf4j
public class LoginController {
    @Autowired
    UserService userService;

    @GetMapping("/login")
    public String login(Map<String, Object> map) {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            return "redirect:/";
        }
        map.put("msg", "");
        return "login.btl";
    }

    @PostMapping("/login")
    @ResponseBody
    public String login(String userName, String password) {
        Map<String, Object> map = new HashMap<>();
        UsernamePasswordToken token = new UsernamePasswordToken(userName, password);
        Subject subject = SecurityUtils.getSubject();
        String msg = "";
        String data = "false";
        if (!subject.isAuthenticated()) {
            try {
                User user = userService.findUserByName(userName);
                if (User.FREEZE_STATE.equals(user.getState())) {
                    System.out.println("该用户已被冻结，无法登录");
                    msg = "该用户已被冻结，无法登录";
                } else if (User.DELETE_STATE.equals(user.getState())) {
                    System.out.println("该用户已被删除，无法登录");
                    msg = "该用户已被删除，无法登录";
                } else {
                    subject.login(token);
                    data = "true";
                }
            } catch (UnknownAccountException uae) {
                System.out.println("没有用户名为" + token.getPrincipal() + "的用户");
                msg = "没有用户名为" + token.getPrincipal() + "的用户";
            } catch (IncorrectCredentialsException ice) {
                System.out.println("用户名为：" + token.getPrincipal() + "的用户密码不正确");
                msg = "用户名为：" + token.getPrincipal() + "的用户密码不正确";
            } catch (LockedAccountException lae) {
                System.out.println("用户名为：" + token.getPrincipal() + "的用户已被冻结");
                msg = "用户名为：" + token.getPrincipal() + "的用户已被冻结";
            } catch (AuthenticationException e) {
                System.out.println("未知错误！");
                msg = "未知错误！";
            }
        }
        map.put("msg", msg);
        map.put("data", data);
        return JSON.toJSONString(map);
    }

    @GetMapping({"/", "/index"})
    public String index(Map<String, Object> map) {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            String userName = (String) subject.getPrincipal();
            map.put("user", userName);
            User user = userService.findUserByName(userName);
            if (user != null) {
                if (userService.checkisNormalUser(user)) {
                    return "index.btl";
                }
                Set<Menu> menuList = new HashSet<>();
                for (Role role : user.getRoleList()) {
                    menuList.addAll(role.getMenuList());
                }
                map.put("menus", menuList);
                map.put("users", user);
            }
            return "system_index.btl";
        } else {
            return "index.btl";
        }
    }

    @RequestMapping("regist")
    public String regist() {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            return "redirect:/";
        }
        return "user_add.btl";
    }

    @PostMapping("/doRegist")
    @ResponseBody
    public String resgist(String userName, String password) {
        Map<String, String> map = new HashMap<>();
        String salt = ShiroSaltUtil.createSale();
        String md5Password = Md5Util.toMd5(password, userName + salt);
        User user = new User();
        user.setUserName(userName);
        user.setPassword(md5Password);
        user.setSalt(salt);
        user.setState(User.NORMAL_STATE);
        if (!userService.insertUser(user)) {
            map.put("data", "false");
            map.put("msg", "注册失败，请重试");
        } else {
            map.put("data", "true");
            map.put("msg", "注册成功，请登录");
        }
        return JSON.toJSONString(map);
    }

    @PostMapping("/mgr/psw_change")
    @ResponseBody
    public String pswChange(String oldPassword, String newPassword) {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> result = userService.changeNowPsw(oldPassword, newPassword);
        if ("true".equals(result.get("result"))) {
            map.put("data", "true");
            map.put("msg", "密码更新成功");
        } else {
            map.put("data", "false");
            map.put("msg", result.get("msg"));
        }
        return JSON.toJSONString(map);
    }

    @GetMapping("/user_info")
    public String userInfo(Map<String, Object> map) {
        Subject subject = SecurityUtils.getSubject();
        String userName = (String) subject.getPrincipal();
        User user = userService.findUserByName(userName);
        if (user != null) {
            map.put("name", user.getUserName());
            StringBuilder sb = new StringBuilder();
            for (Role role : user.getRoleList()) {
                sb.append(role.getDescription()).append("-");
            }
            String roleNames = sb.toString();
            if (roleNames.contains("-")) {
                roleNames = roleNames.substring(0, roleNames.length() - 1);
            }
            map.put("roleName", roleNames);
        }
        UserInfo userInfo = userService.findUserInfoByName(userName);
        if (userInfo != null) {
            map.put("email", userInfo.getEmail());
            map.put("phone", userInfo.getPhone());
        }
        return "/frame/user_info.btl";
    }

    @PostMapping("/userDetail")
    @ResponseBody
    public Object userDetail() {
        Subject subject = SecurityUtils.getSubject();
        String userName = (String) subject.getPrincipal();
        UserInfo userInfo = userService.findUserInfoByName(userName);
        if (userInfo == null) {
            userInfo = new UserInfo();
            userInfo.setAccount(userName);
        }
        return JSON.toJSONString(userInfo);
    }

    @PostMapping("/userInfoEdit")
    @ResponseBody
    public Object userInfoEdit(UserInfo userInfo) {
        Map<String, Object> map = new HashMap<>();
        if (userService.insertUserInfo(userInfo)) {
            map.put("data", "true");
            map.put("msg", "个人信息更新成功");
        } else {
            map.put("data", "false");
            map.put("msg", "个人信息更新失败");
        }
        return JSON.toJSONString(map);
    }
}
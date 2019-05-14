package com.cmy.knowapi.controller;

import com.alibaba.fastjson.JSON;
import com.cmy.knowapi.model.ExceptionInfo;
import com.cmy.knowapi.model.ExceptionType;
import com.cmy.knowapi.model.SysFlow;
import com.cmy.knowapi.model.User;
import com.cmy.knowapi.service.ExceptionService;
import com.cmy.knowapi.service.FlowService;
import com.cmy.knowapi.service.TypeService;
import com.cmy.knowapi.service.UserService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ExceptionInfoController {
    @Autowired
    ExceptionService exceptionService;
    @Autowired
    UserService userService;
    @Autowired
    FlowService flowService;
    @Autowired
    TypeService typeService;

    @RequestMapping("/exception/list")
    @ResponseBody
    public Object queryList(Integer pageNum, Integer limit, String param) {
        Map<String, Object> map = new HashMap<>();
        try {
            Page page = PageHelper.startPage(pageNum, limit);
            List<ExceptionInfo> exceptionInfoList;
            if (param == null || "".equals(param)) {
                exceptionInfoList = exceptionService.findExceptionList();
            } else {
                exceptionInfoList = exceptionService.findExceptionListByParam(param);
            }
            PageInfo info = new PageInfo<>(page.getResult());
            map.put("code", 0);
            map.put("msg", "用户信息分页查询成功");
            map.put("count", info.getTotal());
            map.put("data", exceptionInfoList);
        } catch (Exception e) {
            map.put("code", 1);
            map.put("msg", "用户信息分页查询失败,错误信息: " + e.getMessage());
            map.put("count", 0);
            map.put("data", null);
        }
        return JSON.toJSONString(map);
    }

    @PostMapping("/exception/del")
    @ResponseBody
    public Object userDel(Integer id) {
        Map<String, Object> map = new HashMap<>(2);
        if (exceptionService.delException(id)) {
            map.put("data", "true");
            map.put("msg", "用户删除成功");
        } else {
            map.put("data", "false");
            map.put("msg", "用户删除成功失败，请重试");
        }
        return JSON.toJSONString(map);
    }

    @PostMapping("/exception/audite")
    @ResponseBody
    public Object tofree(Integer id, Integer state) {
        Map<String, Object> map = new HashMap(2);
        if (exceptionService.updateStateById(id, state)) {
            map.put("data", "true");
            map.put("msg", "审核状态更新成功");
        } else {
            map.put("data", "false");
            map.put("msg", "审核状态更新失败");
        }
        return JSON.toJSONString(map);
    }

    @GetMapping("/exception/detail")
    public String showDetail(Integer infoId, Map<String, Object> map) {
        ExceptionInfo exceptionInfo = exceptionService.findExceptionInfoById(infoId);
        map.put("detail", exceptionInfo);
        return "/system/detail.btl";
    }

    @PostMapping("/exception/add")
    @ResponseBody
    public Object addException(Integer flowId, String code, Integer typeId, String title, String content, Map<String, Object> map) {

        Map<String, Object> retMap = exceptionService.insertException(flowId, code, typeId, title, content);
        if ((boolean) retMap.get("flag")) {
            map.put("data", "true");
            map.put("msg", "发布成功");
            map.put("eid", retMap.get("eid"));
        } else {
            map.put("data", "flase");
            map.put("msg", "发布失败");
        }
        return JSON.toJSONString(map);
    }

    @PostMapping("/exception/update")
    @ResponseBody
    public Object updateException(Integer eid, Integer id, Integer flowId, String code, Integer typeId, String title, String content, Map<String, Object> map) {
        Map<String, Object> retMap = exceptionService.updateException(eid, id, flowId, code, typeId, title, content);
        if ((boolean) retMap.get("flag")) {
            map.put("data", "true");
            map.put("msg", "发布成功");
            map.put("eid", eid);
        } else {
            map.put("data", "flase");
            map.put("msg", "发布失败");
        }
        return JSON.toJSONString(map);
    }

    @RequestMapping("/exception/show")
    public String showException(Integer eid, Map<String, Object> map) {
        ExceptionInfo exceptionInfo = exceptionService.findExceptionById(eid);
        User user = userService.findUserByName((String) SecurityUtils.getSubject().getPrincipal());
        List<SysFlow> flowLists = flowService.getFlowAll();
        List<ExceptionType> typeLists = typeService.getTypeAll();
        map.put("curUser", user);
        map.put("flows", flowLists);
        map.put("types", typeLists);
        map.put("e", exceptionInfo);
        return "/face/exception_show.btl";
    }

    @PostMapping("/week/hot")
    @ResponseBody
    public Object weekHot(Map<String, Object> map) {
        List<ExceptionInfo> exceptionInfos = exceptionService.selectExceptionInfoByWeek();
        map.put("data", "true");
        map.put("list", exceptionInfos);
        return JSON.toJSONString(map);
    }
}

package com.cmy.knowapi.controller;

import com.alibaba.fastjson.JSON;
import com.cmy.knowapi.model.AnswerInfo;
import com.cmy.knowapi.service.AnswerService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class AnswerController {
    @Autowired
    AnswerService answerService;

    @RequestMapping("/answer/list")
    @ResponseBody
    public Object queryList(Integer pageNum, Integer limit, String param) {
        Map<String, Object> map = new HashMap<>();
        try {
            Page page = PageHelper.startPage(pageNum, limit);
            List<AnswerInfo> AnswerInfoList;
            if (param == null || "".equals(param)) {
                AnswerInfoList = answerService.findAnswerInfoList();
            } else {
                AnswerInfoList = answerService.findAnswerInfoListByParam(param);
            }
            PageInfo info = new PageInfo<>(page.getResult());
            map.put("code", 0);
            map.put("msg", "异常方案信息分页查询成功");
            map.put("count", info.getTotal());
            map.put("data", AnswerInfoList);
        } catch (Exception e) {
            map.put("code", 1);
            map.put("msg", "异常方案分页查询失败,错误信息: " + e.getMessage());
            map.put("count", 0);
            map.put("data", null);
        }
        return JSON.toJSONString(map);
    }

    @PostMapping("/answer/del")
    @ResponseBody
    public Object userDel(Integer id) {
        Map<String, Object> map = new HashMap<>(2);
        if (answerService.delAnswer(id)) {
            map.put("data", "true");
            map.put("msg", "用户删除成功");
        } else {
            map.put("data", "false");
            map.put("msg", "用户删除成功失败，请重试");
        }
        return JSON.toJSONString(map);
    }

    @PostMapping("/answer/audite")
    @ResponseBody
    public Object toAudite(Integer id, Integer state) {
        Map<String, Object> map = new HashMap(2);
        if (answerService.updateStateById(id, state)) {
            map.put("data", "true");
            map.put("msg", "异常方案审核状态更新成功");
        } else {
            map.put("data", "false");
            map.put("msg", "异常方案审核状态更新失败");
        }
        return JSON.toJSONString(map);
    }

    @GetMapping("/answer/detail")
    public String showDetail(Integer infoId, Map<String, Object> map) {
        AnswerInfo AnswerInfo = answerService.findAnswerInfoById(infoId);
        map.put("detail", AnswerInfo);
        return "/system/detail.btl";
    }

    @PostMapping("/answer/add")
    @ResponseBody
    publi

}

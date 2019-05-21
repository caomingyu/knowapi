package com.cmy.knowapi.controller;

import com.alibaba.fastjson.JSON;
import com.cmy.knowapi.mapper.SysCollectionMapper;
import com.cmy.knowapi.model.*;
import com.cmy.knowapi.service.ExceptionService;
import com.cmy.knowapi.service.FlowService;
import com.cmy.knowapi.service.TypeService;
import com.cmy.knowapi.service.UserService;
import com.cmy.knowapi.util.SimilarityUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import tk.mybatis.mapper.entity.Example;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
    @Autowired
    SysCollectionMapper collectionMapper;

    private static Pattern p = Pattern.compile("\t|\r|\n");

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
        Example example = new Example(SysCollection.class);
        example.createCriteria().andEqualTo("eid", eid).andEqualTo("uid", user.getId());
        SysCollection collection = collectionMapper.selectOneByExample(example);
        if (collection != null) {
            map.put("isCollection", "true");
        } else {
            map.put("isCollection", "false");
        }
        map.put("curUser", user);
        map.put("flows", flowLists);
        map.put("types", typeLists);
        map.put("e", exceptionInfo);
        Matcher m = p.matcher(exceptionInfo.getContent());
        map.put("showContent", m.replaceAll(""));
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

    @PostMapping("/my/exception")
    @ResponseBody
    public Object MyException(Integer uid, Map<String, Object> map) {
        List<ExceptionInfo> exceptionInfos = exceptionService.findMyExceptionList(uid);
        map.put("data", "true");
        map.put("list", exceptionInfos);
        return JSON.toJSONString(map);
    }

    @PostMapping("/exception/list/recommend")
    @ResponseBody
    public Object recommendException(String param, Map<String, Object> map) {
        long start = System.currentTimeMillis();
        List<ExceptionInfo> exceptionInfos = exceptionService.findExceptionList();
        for (ExceptionInfo exceptionInfo : exceptionInfos) {
            exceptionInfo.setSimilasrity(SimilarityUtil.getSimilarity(Jsoup.parse(exceptionInfo.getTitle() + exceptionInfo.getContent().replace("&nbsp;", "")).body().text(), param));
        }
        Collections.sort(exceptionInfos, (o1, o2) -> (int) ((o2.getSimilasrity() - o1.getSimilasrity()) * 100000));
        List<ExceptionInfo> ret = exceptionInfos.parallelStream().filter((ExceptionInfo e) -> e.getSimilasrity() > 0.2).collect(Collectors.toList());
        System.out.println(System.currentTimeMillis() - start + "ms");
        map.put("list", ret);
        map.put("param", param);
        return JSON.toJSONString(map);
    }

    @PostMapping("/exception/collection")
    @ResponseBody
    public Object collectionException(Integer eid, Integer isCollection, Map<String, Object> map) {
        Subject subject = SecurityUtils.getSubject();
        String userName = (String) subject.getPrincipal();
        User user = userService.findUserByName(userName);
        Integer uid = user.getId();
        if (isCollection == 1) {
            SysCollection collection = new SysCollection();
            collection.setEid(eid);
            collection.setUid(uid);
            Integer num = collectionMapper.insert(collection);
            if (num == 1) {
                map.put("data", "true");
                map.put("msg", "收藏帖子成功");
            } else {
                map.put("data", "false");
                map.put("msg", "收藏帖子失败,请重试");
            }
        } else {
            Example example = new Example(SysCollection.class);
            example.createCriteria().andEqualTo("uid", uid).andEqualTo("eid", eid);
//            SysCollection collection = collectionMapper.selectOneByExample(example);
//            Integer num = collectionMapper.delete(collection);
            Integer num = collectionMapper.deleteByExample(example);
            if (num == 1) {
                map.put("data", "true");
                map.put("msg", "收藏帖子成功");
            } else {
                map.put("data", "false");
                map.put("msg", "取消收藏帖子失败,请重试");
            }
        }
        return JSON.toJSONString(map);
    }

    @PostMapping("/exception/collection/byUid")
    @ResponseBody
    public Object getCollectionByUid(Integer uid, Map<String, Object> map) {
        List<ExceptionInfo> exceptionInfos = exceptionService.getCollectionByUid(uid);
        map.put("data", "true");
        map.put("list", exceptionInfos);
        return JSON.toJSONString(map);
    }
}

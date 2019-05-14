package com.cmy.knowapi.service.impl;

import com.cmy.knowapi.mapper.ExceptionInfoMapper;
import com.cmy.knowapi.mapper.ExceptionMapper;
import com.cmy.knowapi.mapper.ExceptionTypeMapper;
import com.cmy.knowapi.mapper.SysFlowMapper;
import com.cmy.knowapi.model.*;
import com.cmy.knowapi.service.ExceptionService;
import com.cmy.knowapi.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@CacheConfig(cacheNames = "userCache")
public class ExceptionIServiceImpl implements ExceptionService {
    @Autowired
    ExceptionMapper exceptionMapper;
    @Autowired
    ExceptionInfoMapper exceptionInfoMapper;
    @Autowired
    UserService userService;
    @Autowired
    SysFlowMapper flowMapper;
    @Autowired
    ExceptionTypeMapper typeMapper;

    @Override
    public List<ExceptionInfo> findExceptionList() {
        return exceptionMapper.findExceptionList(SystemException.DELETE_STATE);
    }

    @Override
    public List<ExceptionInfo> findExceptionListOrderBy(String orderBy, String isFinish) {
        List<ExceptionInfo> exceptionInfos;
        if (isFinish == null) {
            exceptionInfos = exceptionMapper.findExceptionListOrderByNoParam(SystemException.NORMAL_STATE, orderBy);
        } else {
            exceptionInfos = exceptionMapper.findExceptionListOrderBy(SystemException.NORMAL_STATE, orderBy, isFinish);
        }
        return exceptionInfos;
    }

    @Override
    @Cacheable(key = "'exceptionList_'+#param", unless = "#result==null")
    public List<ExceptionInfo> findExceptionListByParam(String param) {
        return exceptionMapper.findExceptionListByParam(SystemException.DELETE_STATE, param);
    }

    @Override
    @Cacheable(key = "'exception_'+#id", unless = "#result==null")
    public ExceptionInfo findExceptionById(Integer id) {
        return exceptionMapper.findExceptionById(SystemException.DELETE_STATE, id);
    }

    @Override
    public boolean delException(Integer id) {
        boolean isSuccess = false;
        SystemException exception = new SystemException();
        exception.setId(id);
        exception.setState(SystemException.DELETE_STATE);
        if (exceptionMapper.updateByPrimaryKeySelective(exception) == 1) {
            isSuccess = true;
        }
        return isSuccess;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateStateById(Integer id, Integer state) {
        boolean isSucceed = false;
        SystemException exception = new SystemException();
        exception.setId(id);
        exception.setState(state);
        int row = exceptionMapper.updateByPrimaryKeySelective(exception);
        if (row == 1) {
            isSucceed = true;
            ExceptionInfo exceptionInfo = new ExceptionInfo();
            Integer eid = exceptionMapper.selectIdByEid(id);
            if (state.equals(SystemException.NORMAL_STATE)) {
                exceptionInfo.setId(eid);
                exceptionInfo.setPassTime(new Date());
                exceptionInfoMapper.updateByPrimaryKeySelective(exceptionInfo);
            } else {
                exceptionInfo = exceptionInfoMapper.selectByPrimaryKey(eid);
                exceptionInfo.setPassTime(null);
                exceptionInfoMapper.updateByPrimaryKey(exceptionInfo);
            }
        }
        return isSucceed;
    }

    @Override
    @Cacheable(key = "'exceptionDetail_'+#id", unless = "#result==null")
    public ExceptionInfo findExceptionInfoById(Integer id) {
        return exceptionInfoMapper.selectByPrimaryKey(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> insertException(Integer flowId, String code, Integer typeId, String title, String content) {
        boolean ret = false;
        Map<String, Object> map = new HashMap<>(2);
        Subject subject = SecurityUtils.getSubject();
        String userName = (String) subject.getPrincipal();
        User user = userService.findUserByName(userName);
        SysFlow flow = flowMapper.selectByPrimaryKey(flowId);
        ExceptionType type = typeMapper.selectByPrimaryKey(typeId);
        SystemException exception = new SystemException();
        exception.setState(SystemException.NEED_AUDITE_STATE);
        exception.setFlowType(flow.getDescription());
        exception.setExceptionType(type.getCode());
        exception.setExceptionCode(code);
        int row = exceptionMapper.insert(exception);
        if (row != 1) {
            map.put("flag", ret);
            return map;
        }
        row = 0;
        ExceptionInfo exceptionInfo = new ExceptionInfo();
        exceptionInfo.setExceptionCode(code);
        exceptionInfo.setTitle(title);
        exceptionInfo.setContent(content);
        exceptionInfo.setAuthor(userName);
        exceptionInfo.setCreateTime(new Date());
        row = exceptionInfoMapper.insert(exceptionInfo);
        if (row != 1) {
            map.put("flag", ret);
            return map;
        }
        exceptionMapper.insertExceptionAndFlow(exception.getId(), flow.getId());
        exceptionMapper.insertExceptionAndType(exception.getId(), type.getId());
        exceptionMapper.insertExceptionAndUser(exception.getId(), user.getId());
        exceptionMapper.insertExceptionAndInfo(exception.getId(), exceptionInfo.getId());

        ret = true;
        map.put("flag", ret);
        map.put("eid", exception.getId());
        return map;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> updateException(Integer eid, Integer id, Integer flowId, String code, Integer typeId, String title, String content) {
        boolean ret = false;
        Map<String, Object> map = new HashMap<>(2);
        SystemException exception = new SystemException();
        SysFlow flow = flowMapper.selectByPrimaryKey(flowId);
        ExceptionType type = typeMapper.selectByPrimaryKey(typeId);
        exception.setId(eid);
        exception.setExceptionType(type.getDescription());
        exception.setFlowType(flow.getDescription());
        exception.setState(SystemException.NEED_AUDITE_STATE);
        exception.setExceptionCode(code);
        exceptionMapper.updateByPrimaryKeySelective(exception);

        ExceptionInfo exceptionInfo = new ExceptionInfo();
        exceptionInfo.setId(id);
        exceptionInfo.setTitle(title);
        exceptionInfo.setCreateTime(new Date());
        exceptionInfo.setContent(content);
        exceptionInfo.setState(SystemException.NEED_AUDITE_STATE);
        exceptionInfo.setExceptionCode(code);
        exceptionInfoMapper.updateByPrimaryKeySelective(exceptionInfo);

        flowMapper.updateByEid(eid, flowId);
        typeMapper.updateByEid(eid, typeId);
        ret = true;
        map.put("flag", ret);
//        map.put("eid", eid);
        return map;
    }

    @Override
    public List<ExceptionInfo> selectExceptionInfoByWeek() {
        List<ExceptionInfo> exceptionInfos = exceptionMapper.selectExceptionInfoByWeek(SystemException.NORMAL_STATE);
        return exceptionInfos;
    }
}

package com.cmy.knowapi.service.impl;

import com.cmy.knowapi.mapper.*;
import com.cmy.knowapi.model.*;
import com.cmy.knowapi.service.AnswerService;
import com.cmy.knowapi.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@CacheConfig(cacheNames = "userCache")
public class AnswerServiceImpl implements AnswerService {
    @Autowired
    AnswerMapper answerMapper;
    @Autowired
    AnswerInfoMapper answerInfoMapper;
    @Autowired
    ExceptionMapper exceptionMapper;
    @Autowired
    ExceptionInfoMapper exceptionInfoMapper;
    @Autowired
    UserService userService;
    @Autowired
    UserInfoMapper userInfoMapper;

    @Override
    public List<AnswerInfo> findAnswerInfoList() {
        return answerMapper.findAnswerList(Answer.DELETE_STATE);
    }

    @Override
    @Cacheable(key = "'answerList_'+#param", unless = "#result==null")
    public List<AnswerInfo> findAnswerInfoListByParam(String param) {
        return answerMapper.findAnswerListByParam(Answer.DELETE_STATE, param);
    }

    @Override
    public boolean delAnswer(Integer id) {
        boolean isSuccess = false;
        Answer exception = new Answer();
        exception.setId(id);
        exception.setState(Answer.DELETE_STATE);
        if (answerMapper.updateByPrimaryKeySelective(exception) == 1) {
            isSuccess = true;
        }
        return isSuccess;
    }

    @Override
    public boolean updateStateById(Integer id, Integer state) {
        boolean isSucceed = false;
        Answer exception = new Answer();
        exception.setId(id);
        exception.setState(state);
        int row = answerMapper.updateByPrimaryKeySelective(exception);
        if (row == 1) {
            isSucceed = true;
        }
        Integer eid = answerMapper.getEid(id);
        Integer eiid = exceptionMapper.selectIdByEid(eid);
        ExceptionInfo exceptionInfo = exceptionInfoMapper.selectByPrimaryKey(eiid);
        Integer answerCount = exceptionInfo.getAnswerCount() == null ? 0 : exceptionInfo.getAnswerCount();
        Integer aiid = answerMapper.getAiid(id);
        AnswerInfo answerInfo = new AnswerInfo();
        answerInfo.setId(aiid);
        Subject subject = SecurityUtils.getSubject();
        String userName = (String) subject.getPrincipal();
        UserInfo userInfo = userService.findUserInfoByName(userName);
        Integer answerNum = userInfo.getAnswerNum() == null ? 0 : userInfo.getAnswerNum();
        if (state == 1) {
            exceptionInfo.setAnswerCount(answerCount + 1);
            answerInfo.setPassTime(new Date());
            exceptionInfoMapper.updateByPrimaryKeySelective(exceptionInfo);
            userInfo.setAnswerNum(answerNum + 1);
            userInfoMapper.updateByPrimaryKeySelective(userInfo);
        } else {
            exceptionInfo.setAnswerCount(answerCount - 1);
            answerInfo.setPassTime(null);
            exceptionInfoMapper.updateByPrimaryKeySelective(exceptionInfo);
            userInfo.setAnswerNum(answerNum - 1);
            userInfoMapper.updateByPrimaryKeySelective(userInfo);
        }
        answerInfoMapper.updateByPrimaryKeySelective(answerInfo);
        return isSucceed;
    }

    @Override
    @Cacheable(key = "'exceptionDetail_'+#id", unless = "#result==null")
    public AnswerInfo findAnswerInfoById(Integer id) {
        return answerInfoMapper.selectByPrimaryKey(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertAnswer(String title, String content, Integer eid) {
        Answer answer = new Answer();
        answer.setState(SystemException.NEED_AUDITE_STATE);
        answer.setTitle(title);
        answerMapper.insert(answer);
        AnswerInfo answerInfo = new AnswerInfo();
        answerInfo.setContent(content);
        answerInfo.setCreateTime(new Date());
        Subject subject = SecurityUtils.getSubject();
        String userName = (String) subject.getPrincipal();
        answerInfo.setAuthor(userName);
        answerInfoMapper.insert(answerInfo);
        User user = userService.findUserByName(userName);
        answerMapper.insertAnswerAndInfo(answer.getId(), answerInfo.getId());
        answerMapper.insertAnswerAndException(answer.getId(), eid);
        answerMapper.insertUserAndAnswer(user.getId(), answer.getId());
        return true;
    }

    @Override
    public List<AnswerInfo> findAnswerInfoListByEid(Integer eid) {
        List<AnswerInfo> answerInfos = answerMapper.findAnswerInfoList(SystemException.NORMAL_STATE, eid);
        return answerInfos;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateAgreeCountByAid(Integer aid) {
        Integer aiid = answerMapper.getAiid(aid);
        AnswerInfo answerInfo = answerInfoMapper.selectByPrimaryKey(aiid);
        Integer agreeCount = answerInfo.getAgreeCount() == null ? 0 : answerInfo.getAgreeCount();
        answerInfo.setAgreeCount(agreeCount + 1);
        answerInfoMapper.updateByPrimaryKeySelective(answerInfo);
        return true;
    }

    @Override
    @Cacheable(key = "'answerInfo_'+#aid", unless = "#result==null")
    public AnswerInfo getAnswerInfo(Integer aid) {
        AnswerInfo answerInfo = answerMapper.getAnswerInfo(aid, SystemException.DELETE_STATE);
        return answerInfo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateAnswer(String title, String content, Integer aid) {
        Answer answer = answerMapper.selectByPrimaryKey(aid);
        if (!SystemException.NEED_AUDITE_STATE.equals(answer.getState())) {
            Integer eid = answerMapper.getEid(aid);
            Integer eiid = exceptionMapper.selectIdByEid(eid);
            ExceptionInfo exceptionInfo = exceptionInfoMapper.selectByPrimaryKey(eiid);
            Integer answerCount = exceptionInfo.getAnswerCount() == null ? 0 : exceptionInfo.getAnswerCount();
            exceptionInfo.setAnswerCount(answerCount - 1);
            exceptionInfoMapper.updateByPrimaryKeySelective(exceptionInfo);
            Subject subject = SecurityUtils.getSubject();
            String userName = (String) subject.getPrincipal();
            UserInfo userInfo = userService.findUserInfoByName(userName);
            Integer answerNum = userInfo.getAnswerNum() == null ? 0 : userInfo.getAnswerNum();
            userInfo.setAnswerNum(answerNum - 1);
            userInfoMapper.updateByPrimaryKeySelective(userInfo);
        }
        answer.setState(SystemException.NEED_AUDITE_STATE);
        answer.setTitle(title);
        answerMapper.updateByPrimaryKeySelective(answer);
        Integer aiid = answerMapper.getAiid(aid);
        AnswerInfo answerInfo = answerInfoMapper.selectByPrimaryKey(aiid);
        answerInfo.setPassTime(null);
        answerInfo.setContent(content);
        answerInfo.setCreateTime(new Date());
        answerInfoMapper.updateByPrimaryKeySelective(answerInfo);
        return true;
    }
}

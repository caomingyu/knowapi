package com.cmy.knowapi.service.impl;

import com.cmy.knowapi.mapper.AnswerInfoMapper;
import com.cmy.knowapi.mapper.AnswerMapper;
import com.cmy.knowapi.model.Answer;
import com.cmy.knowapi.model.AnswerInfo;
import com.cmy.knowapi.model.SystemException;
import com.cmy.knowapi.model.User;
import com.cmy.knowapi.service.AnswerService;
import com.cmy.knowapi.service.UserService;

import org.apache.ibatis.javassist.expr.NewArray;
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
    UserService userService;

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
        AnswerInfo answerInfo=new AnswerInfo();
        answerInfo.setContent(content);
        answerInfo.setCreateTime(new Date());
        Subject subject=SecurityUtils.getSubject();
        String userName=(String)subject.getPrincipal();
        answerInfo.setAuthor(userName);
        answerInfoMapper.insert(answerInfo);
        User user=userService.findUserByName(userName);
        answerMapper.insertAnswerAndInfo(answer.getId(), answerInfo.getId());
        answerMapper.insertAnswerAndException(answer.getId(),eid);
        return true;
    }
}

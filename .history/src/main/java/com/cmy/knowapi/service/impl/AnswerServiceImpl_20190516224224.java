package com.cmy.knowapi.service.impl;

import com.cmy.knowapi.mapper.AnswerInfoMapper;
import com.cmy.knowapi.mapper.AnswerMapper;
import com.cmy.knowapi.model.Answer;
import com.cmy.knowapi.model.AnswerInfo;
import com.cmy.knowapi.service.AnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@CacheConfig(cacheNames = "userCache")
public class AnswerServiceImpl implements AnswerService {
    @Autowired
    AnswerMapper answerMapper;
    @Autowired
    AnswerInfoMapper answerInfoMapper;

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
    @Transactional(rollbackFor = Exce)
    public boolean insertAnswer(String title, String content, Integer eid) {
        return false;
    }
}

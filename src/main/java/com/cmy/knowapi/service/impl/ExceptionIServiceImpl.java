package com.cmy.knowapi.service.impl;

import com.cmy.knowapi.mapper.ExceptionInfoMapper;
import com.cmy.knowapi.mapper.ExceptionMapper;
import com.cmy.knowapi.model.ExceptionInfo;
import com.cmy.knowapi.model.SystemException;
import com.cmy.knowapi.model.User;
import com.cmy.knowapi.service.ExceptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig(cacheNames = "userCache")
public class ExceptionIServiceImpl implements ExceptionService {
    @Autowired
    ExceptionMapper exceptionMapper;
    @Autowired
    ExceptionInfoMapper exceptionInfoMapper;

    @Override
    public List<ExceptionInfo> findExceptionList() {
        return exceptionMapper.findExceptionList(SystemException.DELETE_STATE);
    }

    @Override
    @Cacheable(key = "'exceptionList_'+#param", unless = "#result==null")
    public List<ExceptionInfo> findExceptionListByParam(String param) {
        return exceptionMapper.findExceptionListByParam(SystemException.DELETE_STATE, param);
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
    public boolean updateStateById(Integer id, Integer state) {
        boolean isSucceed = false;
        SystemException exception = new SystemException();
        exception.setId(id);
        exception.setState(state);
        int row = exceptionMapper.updateByPrimaryKeySelective(exception);
        if (row == 1) {
            isSucceed = true;
        }
        return isSucceed;
    }

    @Override
    @Cacheable(key = "'exceptionDetail_'+#id", unless = "#result==null")
    public ExceptionInfo findExceptionInfoById(Integer id) {
        return exceptionInfoMapper.selectByPrimaryKey(id);
    }
}

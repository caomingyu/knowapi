package com.cmy.knowapi.service.impl;

import com.cmy.knowapi.mapper.ExceptionTypeMapper;
import com.cmy.knowapi.mapper.SysFlowMapper;
import com.cmy.knowapi.model.ExceptionType;
import com.cmy.knowapi.model.SysFlow;
import com.cmy.knowapi.service.FlowService;
import com.cmy.knowapi.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@CacheConfig(cacheNames = "userCache")
public class TypeServiceImpl implements TypeService {
    @Autowired
    ExceptionTypeMapper typeMapper;


    @Override
    @Cacheable(key = "'typeAll'", unless = "#result==null")
    public List<ExceptionType> getTypeAll() {
        Example example = new Example(ExceptionType.class);
        example.createCriteria().andEqualTo("state", 1);
        List<ExceptionType> typeLists = typeMapper.selectByExample(example);
        return typeLists;
    }
}

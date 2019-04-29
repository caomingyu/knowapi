package com.cmy.knowapi.service.impl;

import com.cmy.knowapi.mapper.TestMapper;
import com.cmy.knowapi.model.Test_;
import com.cmy.knowapi.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TestServiceImpl implements TestService {
    @Autowired
    TestMapper testMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void testIn() {
        Test_ test = new Test_();
        Test_ test1 = new Test_();
        test.setId(1);
        test.setTest("111");
        test1.setId(1);
        test1.setTest("222");
        testMapper.insert(test);
        testMapper.insert(test1);
    }
}

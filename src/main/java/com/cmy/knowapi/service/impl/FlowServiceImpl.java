package com.cmy.knowapi.service.impl;

import com.cmy.knowapi.mapper.SysFlowMapper;
import com.cmy.knowapi.model.SysFlow;
import com.cmy.knowapi.service.FlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

@Service
@CacheConfig(cacheNames = "userCache")
public class FlowServiceImpl implements FlowService {
    @Autowired
    SysFlowMapper flowMapper;

    @Override
    @Cacheable(key = "'flowAll'", unless = "#result==null")
    public List<SysFlow> getFlowAll() {
        Example example = new Example(SysFlow.class);
        example.createCriteria().andEqualTo("state", "1");
        List<SysFlow> flowLists = flowMapper.selectByExample(example);
        List<SysFlow> retList = new ArrayList<>();
        if (flowLists != null && flowLists.size() != 0) {
            for (SysFlow flow : flowLists) {
                if (!flow.isChild()) {
                    setChild(flowLists, flow);
                    retList.add(flow);
                }
            }
        }
        return retList;
    }

    private void setChild(List<SysFlow> flowLists, SysFlow flow) {
        List<SysFlow> childList = new ArrayList<>();
        for (SysFlow sysFlow : flowLists) {
            if (flow.getId().equals(sysFlow.getParentId())) {
                childList.add(sysFlow);
                sysFlow.setChild(true);
            }
        }
        flow.setChildFlowLists(childList);
    }
}

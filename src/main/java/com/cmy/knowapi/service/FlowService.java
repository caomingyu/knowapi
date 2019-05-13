package com.cmy.knowapi.service;

import com.cmy.knowapi.model.Role;
import com.cmy.knowapi.model.SysFlow;
import com.cmy.knowapi.model.User;
import com.cmy.knowapi.model.UserInfo;

import java.util.List;
import java.util.Map;

public interface FlowService {
    List<SysFlow> getFlowAll();
}

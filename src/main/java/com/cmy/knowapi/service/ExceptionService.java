package com.cmy.knowapi.service;

import com.cmy.knowapi.model.ExceptionInfo;

import java.util.List;

public interface ExceptionService {
    List<ExceptionInfo> findExceptionList();

    List<ExceptionInfo> findExceptionListByParam(String param);

    boolean delException(Integer id);

    boolean updateStateById(Integer id, Integer state);

    ExceptionInfo findExceptionInfoById(Integer id);

}

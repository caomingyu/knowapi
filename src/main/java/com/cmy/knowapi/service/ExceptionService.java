package com.cmy.knowapi.service;

import com.cmy.knowapi.model.ExceptionInfo;

import java.util.List;
import java.util.Map;

public interface ExceptionService {
    List<ExceptionInfo> findExceptionList();

    List<ExceptionInfo> findMyExceptionList(Integer id);

    List<ExceptionInfo> findExceptionListOrderBy(String orderBy, String isFinish);

    List<ExceptionInfo> findExceptionListByParam(String param);

    List<ExceptionInfo> getCollectionByUid(Integer uid);

    ExceptionInfo findExceptionById(Integer id);

    boolean delException(Integer id);

    boolean updateStateById(Integer id, Integer state);

    ExceptionInfo findExceptionInfoById(Integer id);

    Map<String, Object> insertException(Integer flowId, String code, Integer typeId, String title, String content);

    Map<String, Object> updateException(Integer eid, Integer id, Integer flowId, String code, Integer typeId, String title, String content);

    List<ExceptionInfo> selectExceptionInfoByWeek();
}

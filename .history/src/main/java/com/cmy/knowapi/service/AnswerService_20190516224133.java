package com.cmy.knowapi.service;

import com.cmy.knowapi.model.AnswerInfo;

import java.util.List;

public interface AnswerService {
    List<AnswerInfo> findAnswerInfoList();

    List<AnswerInfo> findAnswerInfoListByParam(String param);

    boolean delAnswer(Integer id);

    boolean updateStateById(Integer id, Integer state);

    AnswerInfo findAnswerInfoById(Integer id);

    boolean insert
}

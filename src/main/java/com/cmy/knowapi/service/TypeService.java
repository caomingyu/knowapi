package com.cmy.knowapi.service;

import com.cmy.knowapi.model.ExceptionType;
import com.cmy.knowapi.model.SysFlow;

import java.util.List;

public interface TypeService {
    List<ExceptionType> getTypeAll();
}

package com.cmy.knowapi.service;

import com.cmy.knowapi.model.Menu;
import com.cmy.knowapi.model.Role;
import org.w3c.dom.ls.LSInput;

import java.util.List;

public interface MenuService {
    List<Menu> finfMenusByRole(Role role);
}

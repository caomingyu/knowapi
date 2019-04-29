package com.cmy.knowapi.service.impl;

import com.cmy.knowapi.mapper.MenuMapper;
import com.cmy.knowapi.model.Menu;
import com.cmy.knowapi.model.Role;
import com.cmy.knowapi.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

@Service
@CacheConfig(cacheNames = "userCache")
public class MenuServiceImpl implements MenuService {
    @Autowired
    MenuMapper menuMapper;

    @Override
    @Cacheable(key = "'menus_'+#role.id", unless = "#result==null")
    public List<Menu> finfMenusByRole(Role role) {
        if (role == null) {
            return null;
        }
        List<Menu> menuList = menuMapper.findMenuByRoleId(role.getId());
        for (Menu menu : menuList) {
            List<Menu> subMenuList = new ArrayList<>();
            Example example = new Example(Menu.class);
            example.createCriteria().andEqualTo("parentId", menu.getId());
            List<Menu> menus = menuMapper.selectByExample(example);
            for (Menu menu1 : menus) {
                if (!menu.getId().equals(menu1.getId())) {
                    subMenuList.add(menu1);
                }
            }
            menu.setChildren(subMenuList);
        }
        return menuList;
    }
}

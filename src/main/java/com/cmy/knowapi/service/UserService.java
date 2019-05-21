package com.cmy.knowapi.service;

import com.cmy.knowapi.model.Role;
import com.cmy.knowapi.model.User;
import com.cmy.knowapi.model.UserInfo;

import java.util.List;
import java.util.Map;

public interface UserService {
    User findUserByName(String userName);

    User findUserById(Integer uid);

    Integer selectUidByEid(Integer eid);

    boolean insertUser(User user);

    Map<String, Object> changeNowPsw(String oldPassword, String password);

    UserInfo findUserInfoByName(String userName);

    boolean insertUserInfo(UserInfo userInfo);

    List<UserInfo> queryByName(String name);

    boolean updateStateById(Integer id, Integer state);

    boolean pswRest(Integer uid);

    boolean delUser(Integer uid);

    boolean checkisNormalUser(User user);

    List<Role> roleList(Integer uid);

    List<UserInfo> selectUserInfoByAnswerNum();
}

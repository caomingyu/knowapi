package com.cmy.knowapi;

import com.cmy.knowapi.mapper.PermissionMapper;
import com.cmy.knowapi.mapper.RoleMapper;
import com.cmy.knowapi.mapper.UserMapper;
import com.cmy.knowapi.model.Permission;
import com.cmy.knowapi.model.Role;
import com.cmy.knowapi.model.User;
import com.cmy.knowapi.service.TestService;
import com.cmy.knowapi.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Iterator;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@EnableTransactionManagement
@Slf4j
public class KnowapiApplicationTests {
    @Autowired
    UserMapper userMapper;
    @Autowired
    UserService userService;
    @Autowired
    RoleMapper roleMapper;
    @Autowired
    PermissionMapper permissionMapper;

    @Test
    public void contextLoads() {
        log.info(userMapper.selectAll() + "");
    }

    @Test
    public void test() {
        String hashAlgorithmName = "MD5";//加密方式
        Object crdentials = "cmy1";//密码原值
        Object salt = "dadcb231fd4a4a7fa588da0473d602e1";//盐值
        int hashIterations = 2;//加密1024次
        Object result = new SimpleHash(hashAlgorithmName, crdentials, salt, hashIterations);
        System.out.println(result);
    }

    @Test
    public void ehcache() {
        long start = System.currentTimeMillis();
        log.info("第一次");
        log.warn(userService.findUserByName("cmy1") + "\n" + (System.currentTimeMillis() - start) + "ms");
        start = System.currentTimeMillis();
        log.info("第二次");
        log.warn(userService.findUserByName("cmy1") + "\n" + (System.currentTimeMillis() - start) + "ms");
//        log.info("测试通用Mapper");
//        log.warn(mapper.selectAll() + "");
//        log.info("测试pagehelp分页");
//        final PageInfo<Test_> pageInfo = PageHelper.offsetPage(1, 10).doSelectPageInfo(() -> mapper.selectAll());
//        log.info(pageInfo + "");
    }

    @Autowired
    SecurityManager securityManager;

    @Test
    public void testLogin() {
        UsernamePasswordToken token = new UsernamePasswordToken();
        token.setUsername("admin");
        token.setPassword("1234567".toCharArray());
        SecurityUtils.setSecurityManager(securityManager);
        Subject subject = SecurityUtils.getSubject();
        subject.login(token);
    }

    @Test
    public void Test() {
        Example example = new Example(User.class);
        example.createCriteria().andEqualTo("userName", null);
        User user = userMapper.selectOneByExample(example);
        if (user == null) {
            log.info("null");
        } else {
            log.info(user.toString());
        }
    }

    @Test
    public void testSalt() {
//        User user = userService.findUserByName("admin");
        User user = userMapper.selectByPrimaryKey(1);
        List<Role> roleList = userMapper.findRole(1);
        Iterator<Role> it = roleList.iterator();
        while (it.hasNext()) {
            Role role = it.next();
            List<Permission> permissionList = roleMapper.findPermission(role.getId());
            role.setPermissionList(permissionList);
        }
        user.setRoleList(roleList);
        log.info(user.toString() + "");
    }

    @Autowired
    TestService testService;

    @Test
    public void tets() {
        testService.testIn();
    }
}


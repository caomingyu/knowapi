package com.cmy.knowapi.configure;

import com.cmy.knowapi.model.Permission;
import com.cmy.knowapi.model.Role;
import com.cmy.knowapi.model.User;
import com.cmy.knowapi.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class MyRealm extends AuthorizingRealm {
    @Autowired
    UserService userService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("权限配置-->MyShiroRealm.doGetAuthorizationInfo()");
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        String userName = String.valueOf(principalCollection.getPrimaryPrincipal());
        User user = userService.findUserByName(userName);
        if (user == null) {
            return authorizationInfo;
        }
        for (Role role : user.getRoleList()) {
            authorizationInfo.addRole(role.getRole());
            for (Permission permission : role.getPermissionList()) {
                authorizationInfo.addStringPermission(permission.getPermission());
            }
        }
        if (log.isInfoEnabled()) {
            log.info("当前角色:{} 的权限:-------------{}", user.getUserName(), authorizationInfo.getStringPermissions().stream().toArray());
        }
        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        System.out.println("MyShiroRealm.doGetAuthenticationInfo()");
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        String userName = token.getUsername();
        if (userName == null) {
            userName = "";
        }
        User user = userService.findUserByName(userName);
        if (user == null) {
            throw new UnknownAccountException();
        } else {
            log.info("当前用户{}", user.toString());
            return new SimpleAuthenticationInfo(userName, user.getPassword(), ByteSource.Util.bytes(user.getCredentialsSalt()), getName());
        }
    }
}

package com.harry.auth.service.impl;

import com.harry.auth.service.SysMenuService;
import com.harry.auth.service.SysUserService;
import com.harry.model.system.SysUser;
import com.harry.security.custom.CustomUser;
import com.harry.security.custom.UserDetailsService;
import com.harry.vo.system.RouterVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysMenuService sysMenuService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser sysUser = sysUserService.getUserByUserName(username);
        if(null == sysUser) {
            throw new UsernameNotFoundException("Username does not exist！");
        }

        if(sysUser.getStatus() == 0) {
            throw new RuntimeException("This account is no longer active");
        }
        //get permission data from user id
        List<String> userPermList = sysMenuService.findUserPermsByUserId(sysUser.getId());
        List<SimpleGrantedAuthority> authList = new ArrayList<>();
        for (String perm: userPermList){
            authList.add(new SimpleGrantedAuthority(perm.trim()));
        }
        return new CustomUser(sysUser, authList);
    }
}

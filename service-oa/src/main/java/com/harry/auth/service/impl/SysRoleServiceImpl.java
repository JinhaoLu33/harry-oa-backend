package com.harry.auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harry.auth.mapper.SysRoleMapper;
import com.harry.auth.service.SysRoleService;
import com.harry.model.system.SysRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {
//    @Autowired
//    private SysRoleMapper sysRoleMapper;
}

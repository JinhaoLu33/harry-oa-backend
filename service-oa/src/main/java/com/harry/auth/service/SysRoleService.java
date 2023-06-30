package com.harry.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.harry.model.system.SysRole;
import com.harry.vo.system.AssignRoleVo;

import java.util.Map;

public interface SysRoleService extends IService<SysRole> {

    //search all roles and current user role
    Map<String, Object> findRoleDataByUserId(Long userId);

    //assign user a role
    void doAssign(AssignRoleVo assignRoleVo);
}

package com.harry.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.harry.model.system.SysUser;

/**
 * <p>
 * User Table Service
 * </p>
 *
 * @author Harry
 * @since 2023-06-28
 */
public interface SysUserService extends IService<SysUser> {

    void updateStatus(Long id, Integer status);

    SysUser getUserByUserName(String username);

}

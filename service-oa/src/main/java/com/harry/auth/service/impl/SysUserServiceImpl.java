package com.harry.auth.service.impl;

import com.harry.auth.mapper.SysUserMapper;
import com.harry.auth.service.SysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harry.model.system.SysUser;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2023-06-28
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    /**
     * update status by id
     * @param id user id
     * @param status 0 or 1
     */
    @Override
    public void updateStatus(Long id, Integer status) {
        //search user by id
        SysUser sysUser = baseMapper.selectById(id);
        //set status value
        sysUser.setStatus(status);
        //update
        baseMapper.updateById(sysUser);
    }
}

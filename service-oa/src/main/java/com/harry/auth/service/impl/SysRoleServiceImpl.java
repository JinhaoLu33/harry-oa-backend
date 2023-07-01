package com.harry.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harry.auth.mapper.SysRoleMapper;
import com.harry.auth.service.SysRoleService;
import com.harry.auth.service.SysUserRoleService;
import com.harry.model.system.SysRole;
import com.harry.model.system.SysUserRole;
import com.harry.vo.system.AssignRoleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    @Autowired
    private SysUserRoleService sysUserRoleService;

    //search all roles and current user role
    @Override
    public Map<String, Object> findRoleDataByUserId(Long userId) {

        //search all roles, return a list
        List<SysRole> allRoleList = baseMapper.selectList(null);

        //search by uid in user-role table
        LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUserRole::getUserId, userId);
        List<SysUserRole> existUserRoleList = sysUserRoleService.list(wrapper);

        //select the all role ids from the user id
        List<Long> existRoleIdList =
                existUserRoleList.stream().map(SysUserRole::getRoleId).collect(Collectors.toList());

        //find the matching role information
        List<SysRole> assignRoleList = new ArrayList<>();
        for (SysRole sysRole:allRoleList){
            if (existRoleIdList.contains(sysRole.getId())){
                assignRoleList.add(sysRole);
            }
        }

        //put the data into a map
        Map<String,Object> roleMap = new HashMap<>();
        roleMap.put("assignRoleList",assignRoleList);
        roleMap.put("allRolesList",allRoleList);
        return roleMap;
    }

    //assign user a role
    @Override
    public void doAssign(AssignRoleVo assignRoleVo) {

        //delete existing roles in user
        LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUserRole::getUserId,assignRoleVo.getUserId());
        sysUserRoleService.remove(wrapper);

        //reassign roles
        List<Long> roleIdList = assignRoleVo.getRoleIdList();
        for (Long roleId: roleIdList){
            if (StringUtils.isEmpty(roleId)){
                continue;
            }
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setUserId(assignRoleVo.getUserId());
            sysUserRole.setRoleId(roleId);
            sysUserRoleService.save(sysUserRole);
        }

    }

}

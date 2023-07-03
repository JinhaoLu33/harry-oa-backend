package com.harry.auth.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.harry.auth.mapper.SysMenuMapper;
import com.harry.auth.service.SysMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harry.auth.service.SysRoleMenuService;
import com.harry.auth.utils.MenuHelper;
import com.harry.common.config.exception.DeleteMenuException;
import com.harry.model.system.SysMenu;
import com.harry.model.system.SysRoleMenu;
import com.harry.vo.system.AssignMenuVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * menu service implementation
 * </p>
 *
 * @author Harry
 * @since 2023-06-30
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    @Autowired
    private SysRoleMenuService sysRoleMenuService;

    @Override
    public List<SysMenu> findNodes() {
        //1. search all menu data
        List<SysMenu> sysMenuList = baseMapper.selectList(null);
        //2. construct tree structure
        return MenuHelper.buildTree(sysMenuList);
    }

    @Override
    public void removeMenuById(Long id) {
        //check if current menu has sub menu
        LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
        //check whose parent id equals to current id
        wrapper.eq(SysMenu::getParentId, id);
        //count how many sub menu this current menu has
        Integer count = baseMapper.selectCount(wrapper);
        if (count > 0) {
            throw new DeleteMenuException(201, "cannot delete current menu");
        }
        baseMapper.deleteById(id);
    }

    @Override
    public List<SysMenu> findMenuByRoleId(Long roleId) {
        //search all menus
        LambdaQueryWrapper<SysMenu> wrapperSysMenu = new LambdaQueryWrapper<>();
        wrapperSysMenu.eq(SysMenu::getStatus, 1);
        List<SysMenu> allSysMenuList = baseMapper.selectList(wrapperSysMenu);
        //search menu id matched by role id
        LambdaQueryWrapper<SysRoleMenu> wrapperSysRoleMenu = new LambdaQueryWrapper<>();
        wrapperSysRoleMenu.eq(SysRoleMenu::getRoleId, roleId);
        List<SysRoleMenu> sysRoleMenuList = sysRoleMenuService.list(wrapperSysRoleMenu);
        //get menu ids
        List<Long> sysRoleMenuIdList = sysRoleMenuList.stream().map(SysRoleMenu::getMenuId).collect(Collectors.toList());
        allSysMenuList.forEach(sysMenuItem -> {
            sysMenuItem.setSelect(sysRoleMenuIdList.contains(sysMenuItem.getId()));
        });

        return MenuHelper.buildTree(allSysMenuList);
    }

    @Override
    public void doAssign(AssignMenuVo assignMenuVo) {
        //delete records in role-menu table according to role id
        LambdaQueryWrapper<SysRoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysRoleMenu:: getRoleId,assignMenuVo.getRoleId());
        sysRoleMenuService.remove(wrapper);
        //iterate menuIdList in assignMenuVo, add all id to role-menu table
        List<Long> menuIdList = assignMenuVo.getMenuIdList();
        for (Long menuId : menuIdList){
            if (StringUtils.isEmpty(menuId)){
                continue;
            }
            SysRoleMenu sysRoleMenu = new SysRoleMenu();
            sysRoleMenu.setMenuId(menuId);
            sysRoleMenu.setRoleId(assignMenuVo.getRoleId());
            sysRoleMenuService.save(sysRoleMenu);
        }
    }
}

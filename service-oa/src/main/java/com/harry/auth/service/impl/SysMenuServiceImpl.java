package com.harry.auth.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.harry.auth.mapper.SysMenuMapper;
import com.harry.auth.service.SysMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harry.auth.utils.MenuHelper;
import com.harry.common.config.exception.DeleteMenuException;
import com.harry.model.system.SysMenu;
import org.springframework.stereotype.Service;

import java.util.List;

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
        wrapper.eq(SysMenu::getParentId,id);
        //count how many sub menu this current menu has
        Integer count = baseMapper.selectCount(wrapper);
        if (count>0){
            throw new DeleteMenuException(201,"cannot delete current menu");
        }
        baseMapper.deleteById(id);
    }
}

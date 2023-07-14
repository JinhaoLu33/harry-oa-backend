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
import com.harry.vo.system.MetaVo;
import com.harry.vo.system.RouterVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
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
        allSysMenuList.forEach(item -> {
            item.setSelect(sysRoleMenuIdList.contains(item.getId()));
        });

        return MenuHelper.buildTree(allSysMenuList);
    }

    @Override
    public void doAssign(AssignMenuVo assignMenuVo) {
        //delete records in role-menu table according to role id
        LambdaQueryWrapper<SysRoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysRoleMenu::getRoleId, assignMenuVo.getRoleId());
        sysRoleMenuService.remove(wrapper);
        //iterate menuIdList in assignMenuVo, add all id to role-menu table
        List<Long> menuIdList = assignMenuVo.getMenuIdList();
        for (Long menuId : menuIdList) {
            if (StringUtils.isEmpty(menuId)) {
                continue;
            }
            SysRoleMenu sysRoleMenu = new SysRoleMenu();
            sysRoleMenu.setMenuId(menuId);
            sysRoleMenu.setRoleId(assignMenuVo.getRoleId());
            sysRoleMenuService.save(sysRoleMenu);
        }
    }

    @Override
    public List<RouterVo> findUserMenuListByUserId(Long userId) {
        List<SysMenu> sysMenuList = null;
        //check if current user is admin
        if (userId == 1) {
            LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(SysMenu::getStatus, 1);
            wrapper.orderByAsc(SysMenu::getSortValue);
            sysMenuList = baseMapper.selectList(wrapper);
        } else {
            sysMenuList = baseMapper.findMenuListByUserId(userId);
        }
        //construct menu list to router format
        List<SysMenu> sysMenuTreeList = MenuHelper.buildTree(sysMenuList);
        return this.buildRouter(sysMenuTreeList);
    }

    @Override
    public List<String> findUserPermsByUserId(Long userId) {
        List<SysMenu> sysMenuList;
        //check if current user is admin
        if (userId == 1) {
            LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(SysMenu::getStatus, 1);
            sysMenuList = baseMapper.selectList(wrapper);
        } else {
            sysMenuList = baseMapper.findMenuListByUserId(userId);
        }
        return sysMenuList.stream().filter(item -> item.getType() == 2)
                .map(SysMenu::getPerms).collect(Collectors.toList());
    }

    private List<RouterVo> buildRouter(List<SysMenu> menus) {
        //创建list集合，存储最终数据
        List<RouterVo> routers = new ArrayList<>();
        //menus遍历
        for(SysMenu menu : menus) {
            RouterVo router = new RouterVo();
            router.setHidden(false);
            router.setAlwaysShow(false);
            router.setPath(getRouterPath(menu));
            router.setComponent(menu.getComponent());
            router.setMeta(new MetaVo(menu.getName(), menu.getIcon()));
            //下一层数据部分
            List<SysMenu> children = menu.getChildren();
            if(menu.getType().intValue() == 1) {
                //加载出来下面隐藏路由
                List<SysMenu> hiddenMenuList = children.stream()
                        .filter(item -> !StringUtils.isEmpty(item.getComponent()))
                        .collect(Collectors.toList());
                for(SysMenu hiddenMenu : hiddenMenuList) {
                    RouterVo hiddenRouter = new RouterVo();
                    //true 隐藏路由
                    hiddenRouter.setHidden(true);
                    hiddenRouter.setAlwaysShow(false);
                    hiddenRouter.setPath(getRouterPath(hiddenMenu));
                    hiddenRouter.setComponent(hiddenMenu.getComponent());
                    hiddenRouter.setMeta(new MetaVo(hiddenMenu.getName(), hiddenMenu.getIcon()));

                    routers.add(hiddenRouter);
                }

            } else {
                if(!CollectionUtils.isEmpty(children)) {
                    if(children.size() > 0) {
                        router.setAlwaysShow(true);
                    }
                    //递归
                    router.setChildren(buildRouter(children));
                }
            }
            routers.add(router);
        }
        return routers;
    }

    public String getRouterPath(SysMenu menu) {
        String routerPath = "/" + menu.getPath();
        if (menu.getParentId().intValue() != 0) {
            routerPath = menu.getPath();
        }
        return routerPath;
    }
}

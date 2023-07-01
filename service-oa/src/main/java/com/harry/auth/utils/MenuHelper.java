package com.harry.auth.utils;

import com.harry.model.system.SysMenu;

import java.util.ArrayList;
import java.util.List;

public class MenuHelper {

    public static List<SysMenu> buildTree(List<SysMenu> sysMenuList) {
        List<SysMenu> trees = new ArrayList<>();
        for (SysMenu sysMenu : sysMenuList) {
            if (sysMenu.getParentId() == 0) {
                trees.add(getChildren(sysMenu, sysMenuList));
            }
        }
        return trees;
    }

    /**
     * iteratively find sub menus
     * @param sysMenu level 0 menu
     * @param sysMenuList all menus
     * @return level 0 menu contains all sub menus
     */
    private static SysMenu getChildren(SysMenu sysMenu, List<SysMenu> sysMenuList) {
        sysMenu.setChildren(new ArrayList<SysMenu>());
        for (SysMenu sm : sysMenuList) {
            if (sm.getParentId().longValue() == sysMenu.getId().longValue()) {
                if (sysMenu.getChildren() == null) {
                    sysMenu.setChildren(new ArrayList<>());
                }
                sysMenu.getChildren().add(getChildren(sm, sysMenuList));
            }
        }
        return sysMenu;
    }
}

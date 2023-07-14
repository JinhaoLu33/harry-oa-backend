package com.harry.auth.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.harry.model.system.SysMenu;
import com.harry.vo.system.AssignMenuVo;
import com.harry.vo.system.RouterVo;

import java.util.List;

/**
 * <p>
 * 菜单表 服务类
 * </p>
 *
 * @author Harry
 * @since 2023-06-30
 */
public interface SysMenuService extends IService<SysMenu> {

    List<SysMenu> findNodes();

    void removeMenuById(Long id);

    List<SysMenu> findMenuByRoleId(Long roleId);

    void doAssign(AssignMenuVo assignMenuVo);

    List<RouterVo> findUserMenuListByUserId(Long userId);

    List<String> findUserPermsByUserId(Long userId);

}

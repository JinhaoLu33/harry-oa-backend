package com.harry.auth.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.harry.model.system.SysMenu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * menu table Mapper interface
 * </p>
 *
 * @author Harry
 * @since 2023-06-30
 */
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    List<SysMenu> findMenuListByUserId(@Param("userId") Long userId);
}

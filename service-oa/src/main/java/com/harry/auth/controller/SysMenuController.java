package com.harry.auth.controller;


import com.harry.auth.service.SysMenuService;
import com.harry.common.result.Result;
import com.harry.model.system.SysMenu;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * menu table controller
 * </p>
 *
 * @author Harry
 * @since 2023-06-30
 */
@Api(tags = "menu management api")
@RestController
@RequestMapping("/admin/system/sysMenu")
public class SysMenuController {
    @Autowired
    private SysMenuService sysMenuService;

    @ApiOperation(value = "find menu")
    @GetMapping("findNodes")
    public Result findNodes() {
        List<SysMenu> list = sysMenuService.findNodes();
        return Result.success(list);
    }

    @ApiOperation(value = "create menu")
    @PostMapping("save")
    public Result save(@RequestBody SysMenu permission) {
        sysMenuService.save(permission);
        return Result.success();
    }

    @ApiOperation(value = "change menu")
    @PutMapping("update")
    public Result updateById(@RequestBody SysMenu permission) {
        sysMenuService.updateById(permission);
        return Result.success();
    }

    @ApiOperation(value = "remove menu")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id) {
        sysMenuService.removeMenuById(id);
        return Result.success();
    }
}


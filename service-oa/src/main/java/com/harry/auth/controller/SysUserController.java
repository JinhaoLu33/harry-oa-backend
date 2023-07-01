package com.harry.auth.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.harry.auth.service.SysUserService;
import com.harry.common.result.Result;
import com.harry.model.system.SysUser;
import com.harry.vo.system.SysUserQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * User Table Controller
 * </p>
 *
 * @author Harry
 * @since 2023-06-28
 */
@Api(tags = "User management")
@RestController
@RequestMapping("/admin/system/sysUser")
public class SysUserController {
    @Autowired
    private SysUserService service;

    //Conditional paginated queries
    @ApiOperation("Conditional Paginated Query")
    @GetMapping("{page}/{limit}")
    public Result index(@PathVariable Long page,
                        @PathVariable Long limit,
                        SysUserQueryVo sysUserQueryVo) {
        //create page object
        Page<SysUser> pageParam = new Page<>(page, limit);
        // encapsulate condition, check not null
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        String username = sysUserQueryVo.getKeyword();
        String createTimeBegin = sysUserQueryVo.getCreateTimeBegin();
        String createTimeEnd = sysUserQueryVo.getCreateTimeEnd();

        if (!StringUtils.isEmpty(username)) {
            wrapper.like(SysUser::getName, username);
        }
        if (!StringUtils.isEmpty(createTimeBegin)) {
            wrapper.ge(SysUser::getCreateTime, createTimeBegin);
        }
        if (!StringUtils.isEmpty(createTimeEnd)) {
            wrapper.le(SysUser::getCreateTime, createTimeEnd);
        }
        //use MP methods for pagination
        Page<SysUser> pageModel = service.page(pageParam, wrapper);
        return Result.success(pageModel);
    }

    @ApiOperation(value = "Search user by id")
    @GetMapping("get/{id}")
    public Result get(@PathVariable Long id) {
        SysUser user = service.getById(id);
        return Result.success(user);
    }

    @ApiOperation(value = "Save user")
    @PostMapping("save")
    public Result save(@RequestBody SysUser user) {
        service.save(user);
        return Result.success();
    }

    @ApiOperation(value = "Update user")
    @PutMapping("update")
    public Result updateById(@RequestBody SysUser user) {
        service.updateById(user);
        return Result.success();
    }

    @ApiOperation(value = "Delete user by id")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id) {
        service.removeById(id);
        return Result.success();
    }

    @ApiOperation(value = "Update user status by id")
    @GetMapping("updateStatus/{id}/{status}")
    public Result updateStatus(@PathVariable Long id,@PathVariable Integer status) {
        service.updateStatus(id,status);
        return Result.success();
    }
}


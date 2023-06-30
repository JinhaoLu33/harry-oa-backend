package com.harry.auth.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.harry.auth.mapper.SysRoleMapper;
import com.harry.auth.service.SysRoleService;
import com.harry.common.result.Result;
import com.harry.model.system.SysRole;
import com.harry.vo.system.AssignRoleVo;
import com.harry.vo.system.SysRoleQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api(tags = "role management api")
@RestController//controller, responseBody
@RequestMapping("/admin/system/sysRole") //url
public class SysRoleController {
    //inject service
    @Autowired
    private SysRoleService sysRoleService;

    //search all roles and current user role
    @ApiOperation("get roles by id")
    @GetMapping("/toAssign/{userId}")
    public Result toAssign(@PathVariable Long userId){
        Map<String,Object> map = sysRoleService.findRoleDataByUserId(userId);
        return Result.success(map);
    }
    //assign user a role
    @ApiOperation("assign user roles")
    @PostMapping("/doAssign")
    public Result doAssign(@RequestBody AssignRoleVo assignRoleVo){
        sysRoleService.doAssign(assignRoleVo);
        return Result.success();
    }



    //search all roles
    @ApiOperation("find all roles")
    @GetMapping("/findAll")
    public Result findAll() {
        List<SysRole> list = sysRoleService.list();
        return Result.success(list);
    }

    //Conditional paginated queries
    @ApiOperation("Conditional Paginated Query")
    @GetMapping("{page}/{limit}")
    public Result pageQueryRole(@PathVariable Long page,
                                @PathVariable Long limit,
                                SysRoleQueryVo sysRoleQueryVo) {

        Page<SysRole> pageParam = new Page<>(page, limit);

        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        String roleName = sysRoleQueryVo.getRoleName();
        if (!StringUtils.isEmpty(roleName)) {
            wrapper.like(SysRole::getRoleName, roleName);
        }

        IPage<SysRole> pageModel = sysRoleService.page(pageParam, wrapper);
        return Result.success(pageModel);
    }

    //add roles
    @ApiOperation("Add roles")
    @PostMapping("save")
    public Result save(@RequestBody SysRole role) {//transmit data by json
        //use method in service
        boolean isSuccess = sysRoleService.save(role);
        if (isSuccess) {
            return Result.success();
        } else {
            return Result.fail();
        }
    }

    //search role by id
    @ApiOperation("Search role by id")
    @GetMapping("get/{id}")
    public Result get(@PathVariable Long id) {
        SysRole role = sysRoleService.getById(id);
        return Result.success(role);
    }

    //update role
    @ApiOperation("Update role")
    @PutMapping("update")
    public Result update(@RequestBody SysRole role) {
        boolean isSuccess = sysRoleService.updateById(role);
        if (isSuccess) {
            return Result.success();
        } else {
            return Result.fail();
        }
    }

    //delete role by id
    @ApiOperation("Delete role by id")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id) {
        boolean isSuccess = sysRoleService.removeById(id);
        if (isSuccess) {
            return Result.success();
        } else {
            return Result.fail();
        }
    }

    //delete in batches
    @ApiOperation("Delete in batches")
    @DeleteMapping("batchRemove")
    public Result batchRemove(@RequestBody List<Long> idList) {//jason array-> java list
        boolean isSuccess = sysRoleService.removeByIds(idList);
        if (isSuccess) {
            return Result.success();
        } else {
            return Result.fail();
        }
    }


}

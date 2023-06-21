package com.harry.auth.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.harry.auth.mapper.SysRoleMapper;
import com.harry.auth.service.SysRoleService;
import com.harry.common.result.Result;
import com.harry.model.system.SysRole;
import com.harry.vo.system.SysRoleQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "role management api")
@RestController//controller, responseBody
@RequestMapping("/admin/system/sysRole") //url
public class SysRoleController {
    //inject service
    @Autowired
    private SysRoleService sysRoleService;

    //search all roles
//    @GetMapping("/findAll")
//    public List<SysRole> findAll(){
//        List<SysRole> list = sysRoleService.list();
//        return list;
//    }

    //search all roles
    @ApiOperation("find all roles")
    @GetMapping("/findAll")
    public Result findAll() {
        List<SysRole> list = sysRoleService.list();
        return Result.success(list);
    }

    //Conditional paginated queries
    @ApiOperation("Conditional paginated queries")
    @GetMapping("{page}/{limit}")
    public Result pageQueryRole(@PathVariable Long page,
                                @PathVariable Long limit,
                                SysRoleQueryVo sysRoleQueryVo) {
        //use method in service
        //1.page parameters
        Page<SysRole> pageParam = new Page<>(page, limit);
        //2. encapsulate conditions
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        String roleName = sysRoleQueryVo.getRoleName();
        if (!StringUtils.isEmpty(roleName)) {
            wrapper.like(SysRole::getRoleName, roleName);
        }

        IPage<SysRole> pageModel = sysRoleService.page(pageParam, wrapper);
        return Result.success(pageModel);
    }


}

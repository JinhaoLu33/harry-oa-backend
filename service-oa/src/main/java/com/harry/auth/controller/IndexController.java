package com.harry.auth.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.harry.auth.service.SysMenuService;
import com.harry.auth.service.SysUserService;
import com.harry.common.config.exception.DeleteMenuException;
import com.harry.common.config.exception.UserException;
import com.harry.common.jwt.JwtHelper;
import com.harry.common.result.Result;
import com.harry.common.utils.MD5;
import com.harry.model.system.SysMenu;
import com.harry.model.system.SysUser;
import com.harry.vo.system.LoginVo;
import com.harry.vo.system.RouterVo;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.RouteMatcher;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 后台登录登出
 * </p>
 */
@Api(tags = "后台登录管理")
@RestController
@RequestMapping("/admin/system/index")
public class IndexController {
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysMenuService sysMenuService;

    @PostMapping("login")
    public Result login(@RequestBody LoginVo loginVo) {
        //use username to check user info in database
        String username = loginVo.getUsername();
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUsername, username);
        SysUser sysUser = sysUserService.getOne(wrapper);
        //check null
        if (sysUser == null) {
            throw new UserException(201, "user does not exist");
        }
        //check password
        String passwordDB = sysUser.getPassword();
        String passwordInput = MD5.encrypt(loginVo.getPassword());
        if(!passwordDB.equals(passwordInput)) {
            throw new UserException(201, "password is incorrect");
        }
        //check user status
        if (sysUser.getStatus().intValue() == 0) {
            throw new UserException(201, "user is disabled");
        }

        String token = JwtHelper.createToken(sysUser.getId(), sysUser.getUsername());
        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        return Result.success(map);
    }

    //info
    @GetMapping("info")
    public Result info(HttpServletRequest request) {
        //1  Get user information from the request header (get the request header token string)
        String token = request.getHeader("token");

        //2 Get the user ID or username from the token string
        Long userId = JwtHelper.getUserId(token);

        //3 Query the database according to the user ID to obtain the user information
        SysUser sysUser = sysUserService.getById(userId);

        //4 Get a list of menus that users can operate based on user ID
        //The query database dynamically constructs the routing structure for display
        List<RouterVo> routerList = sysMenuService.findUserMenuListByUserId(userId);

        //5 Get the list of buttons that the user can operate according to the user id
        List<String> permsList = sysMenuService.findUserPermsByUserId(userId);

        //6 Returns the data
        Map<String, Object> map = new HashMap<>();
        map.put("roles","[admin]");
        map.put("name",sysUser.getName());
        map.put("avatar","https://oss.aliyuncs.com/aliyun_id_photo_bucket/default_handsome.jpg");
        map.put("routers",routerList);
        map.put("buttons",permsList);
        return Result.success(map);
    }

    @PostMapping("logout")
    public Result logout() {
        return Result.success();
    }

}

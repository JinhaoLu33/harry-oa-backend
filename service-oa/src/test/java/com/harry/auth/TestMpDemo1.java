package com.harry.auth;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.harry.auth.mapper.SysRoleMapper;
import com.harry.model.system.SysRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.management.Query;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class TestMpDemo1 {

    //inject
    @Autowired
    private SysRoleMapper mapper;

    @Test
    public void getAll() {
        List<SysRole> list = mapper.selectList(null);
        System.out.println(list);
    }

    @Test
    public void add() {
        SysRole role = new SysRole();
        role.setRoleName("CC");
        role.setRoleCode("role");
        role.setDescription("CC");
        mapper.insert(role);
    }

    @Test
    public void update() {
        //search by id
        SysRole role = mapper.selectById(9);
        //set value
        role.setRoleName("changed");
        //change value
        mapper.updateById(role);
    }

    @Test
    public void deleteId() {
       mapper.deleteById(9);
    }

    @Test
    public void deleteBatchIds() {
        mapper.deleteBatchIds(Arrays.asList(1,2));
    }

    @Test
    public void testQuery1(){
        QueryWrapper<SysRole> wrapper = new QueryWrapper<>();
        wrapper.eq("role_name","AA");
        List<SysRole> sysRoles = mapper.selectList(wrapper);
        System.out.println(sysRoles);
    }

    @Test
    public void testQuery2(){
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysRole::getRoleName,"AA");
        List<SysRole> sysRoles = mapper.selectList(wrapper);
        System.out.println(sysRoles);
    }

}

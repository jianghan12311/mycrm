package com.wu.crm.dao;

import com.wu.crm.base.BaseMapper;
import com.wu.crm.vo.Permission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PermissionMapper extends BaseMapper<Permission,Integer> {

    //通过角色id查询权限记录
    Integer countPermissionByRoleId(Integer roleId);

    //通过角色id删除权限记录
    void deletePermissionByRoleId(Integer roleId);

    //查询角色拥有的所有资源id的集合
    List<Integer> queryRoleHasModuleIdByRoleId(Integer roleId);

    //通过用户id查询对应的资源列表(资源权限码)
    List<String> queryUserHasRolePermissionByUserId(Integer userId);

    //通过资源id查询权限记录
    Integer countPermissionByModuleId(@Param("id")Integer id);

    //通过资源id删除权限记录
    Integer deletePermissionByModuleId(@Param("id")Integer id);
}
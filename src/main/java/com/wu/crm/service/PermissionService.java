package com.wu.crm.service;

import com.wu.crm.base.BaseService;
import com.wu.crm.dao.PermissionMapper;
import com.wu.crm.vo.Permission;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class PermissionService extends BaseService<Permission,Integer> {

    @Resource
    private PermissionMapper permissionMapper;

    /**
     * 通过查询用户所拥有的角色，角色拥有的资源，得到用户拥有的资源列表 （资源权限码）
     * @param userId
     * @return
     */
    public List<String> queryUserHasRolePermissionByUserId(Integer userId) {
        return permissionMapper.queryUserHasRolePermissionByUserId(userId);
    }
}
package com.wu.crm.service;

import com.wu.crm.base.BaseService;
import com.wu.crm.dao.UserRoleMapper;
import com.wu.crm.vo.UserRole;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserRoleService extends BaseService<UserRole,Integer> {

    @Resource
    private UserRoleMapper userRoleMapper;

}

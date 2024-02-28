package com.wu.crm.controller;

import com.wu.crm.base.BaseController;
import com.wu.crm.service.UserRoleService;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

@Controller
public class UserRoleController extends BaseController {

    @Resource
    private UserRoleService userRoleService;
}

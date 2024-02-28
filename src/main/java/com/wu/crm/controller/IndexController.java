package com.wu.crm.controller;


import com.wu.crm.base.BaseController;
import com.wu.crm.service.PermissionService;
import com.wu.crm.service.UserService;
import com.wu.crm.utils.LoginUserUtil;
import com.wu.crm.vo.Permission;
import com.wu.crm.vo.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 乐字节：专注线上IT培训
 * 答疑老师微信：lezijie
 */
@Controller
public class IndexController extends BaseController {

    @Resource
    private UserService userService;
    @Resource
    private PermissionService permissionService;

    /**
     * 系统登录页
     *
     *
     * 乐字节：专注线上IT培训
     * 答疑老师微信：lezijie
     * @param
     * @return java.lang.String
     */
    @RequestMapping("index")
    public String index(){
        return "index";
    }


    /**
     * 系统界面欢迎页
     *
     *
     * 乐字节：专注线上IT培训
     * 答疑老师微信：lezijie
     * @param
     * @return java.lang.String
     */
    @RequestMapping("welcome")
    public String welcome(){
        return "welcome";
    }

    /**
     * 后端管理主页面
     *
     *
     * 乐字节：专注线上IT培训
     * 答疑老师微信：lezijie
     * @param
     * @return java.lang.String
     */
    @RequestMapping("main")
    public String main(HttpServletRequest request){

        int i;
        // 获取cookie中的用户Id
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
        // 查询用户对象，设置session作用域
        User user = userService.selectByPrimaryKey(userId);
        request.getSession().setAttribute("user",user);


        //通过当前登录用户id查询当前用户拥有的资源列表(查询对应资源的授权码)
        List<String> permissions = permissionService.queryUserHasRolePermissionByUserId(userId);
        //将集合设置到session作用域中
        request.getSession().setAttribute("permissions",permissions);


        return "main";
    }

}

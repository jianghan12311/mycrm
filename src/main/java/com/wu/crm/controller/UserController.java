package com.wu.crm.controller;

import com.wu.crm.base.BaseController;
import com.wu.crm.base.ResultInfo;
import com.wu.crm.exceptions.ParamsException;
import com.wu.crm.model.UserModel;
import com.wu.crm.query.UserQuery;
import com.wu.crm.service.UserService;
import com.wu.crm.utils.LoginUserUtil;
import com.wu.crm.vo.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("user")
public class UserController extends BaseController {

    @Resource
    private UserService userService;

    /**
     * 用户登录
     *
     * @param userName
     * @param userPwd
     * @return
     */
    @PostMapping("login")
    @ResponseBody
    public ResultInfo userLogin(String userName,String userPwd){

        ResultInfo resultInfo = new ResultInfo();

        //调用service层的方法
        UserModel userModel = userService.userLogin(userName,userPwd);

        //设置resultInfo的result的值 将数据返回给请求
        resultInfo.setResult(userModel);
        return resultInfo;
    }


    /**
     * 用户修改密码
     * @param request
     * @param oldPassword
     * @param newPassword
     * @param repeatPassword
     * @return
     */
    @PostMapping("updatePwd")
    @ResponseBody
    public ResultInfo updateUserPassword(HttpServletRequest request,String oldPassword,String newPassword,String repeatPassword){

        ResultInfo resultInfo = new ResultInfo();

        //获取cookie中的userId
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
        //调用service层修改密码方法
        userService.updatePassWord(userId,oldPassword,newPassword,repeatPassword);

        return resultInfo;

    }

    /**
     * 修改密码的页面
     * @return
     */
    @RequestMapping("toPasswordPage")
    public String toPasswordPage(){
        return "user/password";
    }

    /**
     * 查询所有的销售人员
     * @return
     */
    @RequestMapping("queryAllSales")
    @ResponseBody
    public List<Map<String,Object>> queryAllSales(){
        return userService.queryAllSales();
    }

    /**
     * 分页多条件查询用户列表
     * @param userQuery
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> selectByParams(UserQuery userQuery){
        return userService.queryByParamsForTable(userQuery);
    }

    /**
     * 进入用户列表页面
     * @return
     */
    @RequestMapping("index")
    public String index(){
        return "user/user";
    }


    /**
     * 添加用户
     * @param user
     * @return
     */
    @ResponseBody
    @RequestMapping("add")
    public ResultInfo addUser(User user){
        userService.addUser(user);
        return success("用户添加成功");
    }

    /**
     * 打开添加或修改用户的界面
     * @return
     */
    @RequestMapping("toAddOrUpdateUserPage")
    public String toAddOrUpdateUserPage(Integer id,HttpServletRequest request){
        //判断id是否为空，不为空表示更新操作，查询用户对象
        if (id != null){
            //通过id查询用户对象
            User user = userService.selectByPrimaryKey(id);
            //将数据设置到请求域
            request.setAttribute("userInfo",user);
        }

        return "user/add_update";
    }

    /**
     * 更新用户
     * @param user
     * @return
     */
    @ResponseBody
    @RequestMapping("update")
    public ResultInfo updateUser(User user){
        userService.updateUser(user);
        return success("用户更新成功");
    }

    /**
     * 用户删除
     * @param ids
     * @return
     */
    @ResponseBody
    @RequestMapping("delete")
    public ResultInfo deleteUser(Integer[] ids){

        userService.deleteByIds(ids);

        return success("用户删除成功");
    }
}
























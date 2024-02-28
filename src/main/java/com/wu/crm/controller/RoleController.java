package com.wu.crm.controller;

import com.wu.crm.base.BaseController;
import com.wu.crm.base.ResultInfo;
import com.wu.crm.query.RoleQuery;
import com.wu.crm.service.RoleService;
import com.wu.crm.vo.Role;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("role")
public class RoleController extends BaseController {

    @Resource
    private RoleService roleService;

    /**
     * 查询所有的角色列表
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("queryAllRoles")
    public List<Map<String, Object>> queryAllRoles(Integer userId) {
        return roleService.queryAllRoles(userId);
    }

    /**
     * 分页查询角色列表
     *
     * @param roleQuery
     * @return
     */
    @GetMapping("list")
    @ResponseBody
    public Map<String, Object> selectByParams(RoleQuery roleQuery) {
        return roleService.queryByParamsForTable(roleQuery);
    }

    /**
     * 进入角色关联页面
     *
     * @return
     */
    @RequestMapping("index")
    public String index() {
        return "role/role";
    }

    /**
     * 添加角色
     *
     * @param role
     * @return
     */
    @RequestMapping("add")
    @ResponseBody
    public ResultInfo addRole(Role role) {
        roleService.addRole(role);
        return success("角色添加成功");
    }

    /**
     * 进入添加修改角色界面
     *
     * @return
     */
    @RequestMapping("toAddOrUpdateRoleDialog")
    public String toAddOrUpdateRoleDialog(Integer roleId, HttpServletRequest request) {
        if (roleId != null) {
            Role role = roleService.selectByPrimaryKey(roleId);
            request.setAttribute("role", role);
        }
        return "role/add_update";
    }

    /**
     * 修改角色
     *
     * @param role
     * @return
     */
    @RequestMapping("update")
    @ResponseBody
    public ResultInfo updateRole(Role role) {
        roleService.updateRole(role);
        return success("角色修改成功");
    }

    /**
     * 删除角色
     * @param roleId
     * @return
     */
    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo deleteRole(Integer roleId) {
        roleService.deleteRole(roleId);
        return success("角色删除成功");
    }

    /**
     * 角色授权
     * @param roleId
     * @param mIds
     * @return
     */
    @PostMapping("addGrant")
    @ResponseBody
    public ResultInfo addGrant(Integer roleId,Integer[] mIds){

        roleService.addGrant(roleId,mIds);

        return success("角色授权成功!");
    }


}
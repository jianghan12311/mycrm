package com.wu.crm.controller;

import com.wu.crm.base.BaseController;
import com.wu.crm.base.ResultInfo;
import com.wu.crm.model.TreeModel;
import com.wu.crm.service.ModuleService;
import com.wu.crm.vo.Module;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RequestMapping("module")
@Controller
public class ModuleController extends BaseController {

    @Resource
    private ModuleService moduleService;

    @ResponseBody
    @RequestMapping("list")
    public Map<String,Object> queryModuleList(){
        return moduleService.queryModuleList();
    }

    /**
     * 添加资源
     * @param module
     * @return
     */
    @ResponseBody
    @RequestMapping("add")
    public ResultInfo addModule(Module module){
        moduleService.addModule(module);
        return success("添加资源成功!");
    }

    /**
     * 修改资源
     * @param module
     * @return
     */
    @ResponseBody
    @RequestMapping("update")
    public ResultInfo updateModule(Module module){
        moduleService.updateModule(module);
        return success("修改资源成功!");
    }


    /**
     * 进入资源管理页面
     * @return
     */
    @RequestMapping("index")
    public String index(){
        return "module/module";
    }

    /**
     * 查询所有的资源列表
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("queryAllModules")
    public List<TreeModel> queryAllModules(Integer roleId) {
        return moduleService.queryAllModules(roleId);
    }

    /**
     * 打开修改资源页面
     * @param id
     * @return
     */
    @RequestMapping("toUpdateModulePage")
    public String toUpdateModulePage(Integer id, Model model){
        //将要修改的资源对象设置到请求域中
        model.addAttribute("module",moduleService.selectByPrimaryKey(id));
        return "module/update";
    }

    /**
     * 进入授权页面
     *
     * @param roleId
     * @return
     */
    @RequestMapping("toAddGrantPage")
    public String toAddGrantPage(Integer roleId, HttpServletRequest request) {
        //将需要授权的角色id设置到请求域中
        request.setAttribute("roleId", roleId);
        return "role/grant";
    }

    /**
     *  打开添加资源页面
     * @param grade 层级
     * @param parentId  父菜单id
     * @return
     */
    @RequestMapping("toAddModulePage")
    public String toAddModulePage(Integer grade,Integer parentId,HttpServletRequest request){
        //将数据设置到请求域
        request.setAttribute("grade",grade);
        request.setAttribute("parentId",parentId);

        return "module/add";
    }

    /**
     * 删除资源
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping("delete")
    public ResultInfo deleteModule(Integer id){
        moduleService.deleteModule(id);
        return success("删除资源成功!");
    }
}

package com.wu.crm.controller;

import com.wu.crm.annotation.RequiredPermission;
import com.wu.crm.base.BaseController;
import com.wu.crm.base.ResultInfo;
import com.wu.crm.enums.StateStatus;
import com.wu.crm.query.SaleChanceQuery;
import com.wu.crm.service.SaleChanceService;
import com.wu.crm.utils.CookieUtil;
import com.wu.crm.utils.LoginUserUtil;
import com.wu.crm.vo.SaleChance;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("sale_chance")
public class SaleChanceController extends BaseController {

    @Resource
    private SaleChanceService saleChanceService;

    /**
     * 营销机会数据查询（分页多条件查询）    101001
     * 如果flag的值不为空且值为一，则表示当前查询的是客户开发计划，否则查询营销机会数据
     *
     * @param saleChanceQuery
     * @return
     */
    @RequiredPermission(code = "101001")
    @RequestMapping("list")
    @ResponseBody
    public Map<String, Object> querySaleChanceByParams(SaleChanceQuery saleChanceQuery, Integer flag, HttpServletRequest request) {

        //判断flag的值
        if (flag != null && flag == 1) {
            //查询的是客户开发计划
            //设置分配状态
            saleChanceQuery.setState(StateStatus.STATED.getType());
            //设置指派人（当前登录用户的id）
            //从cookie中获取当前登录用户的id
            Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
            saleChanceQuery.setAssignMan(userId);
        }

        return saleChanceService.querySaleChanceByParams(saleChanceQuery);
    }

    /**
     * 进入营销机会管理的页面  1010
     *
     * @return
     */
    @RequiredPermission(code = "1010")
    @RequestMapping("index")
    public String index() {
        return "saleChance/sale_chance";
    }

    /**
     * 添加营销机会   101002
     *
     * @param saleChance
     * @return
     */
    @RequiredPermission(code = "101002")
    @PostMapping("add")
    @ResponseBody
    public ResultInfo addSaleChance(SaleChance saleChance, HttpServletRequest request) {
        //从cookie中获取当前登录用户名
        String userName = CookieUtil.getCookieValue(request, "userName");
        //设置用户名到营销机会对象
        saleChance.setCreateMan(userName);
        //调用service层添加方法
        saleChanceService.addSaleChance(saleChance);
        return success("营销机会数据添加成功");
    }

    /**
     * 更新营销机会   101004
     *
     * @param saleChance
     * @return
     */
    @RequiredPermission(code = "101004")
    @PostMapping("update")
    @ResponseBody
    public ResultInfo updateSaleChance(SaleChance saleChance) {
        //调用service层添加方法
        saleChanceService.updateSaleChance(saleChance);
        return success("营销机会更新成功");
    }

    /**
     * 进入添加或修改营销机会数据页面
     *
     * @return
     */
    @RequestMapping("toSaleChancePage")
    public String toSaleChancePage(Integer saleChanceId, HttpServletRequest request) {

        //判断saleChanceId是否为空
        if (saleChanceId != null) {
            //通过ID查询营销机会数据
            SaleChance saleChance = saleChanceService.selectByPrimaryKey(saleChanceId);
            //将数据设置到请求域中
            request.setAttribute("saleChance", saleChance);
        }

        return "saleChance/add_update";
    }


    /**
     * 删除营销机会   101003
     *
     * @param ids
     * @return
     */
    @RequiredPermission(code = "101003")
    @PostMapping("delete")
    @ResponseBody
    public ResultInfo deleteSaleChance(Integer[] ids) {
        saleChanceService.deleteBatch(ids);
        return success("营销机会数据删除成功");
    }

    /**
     * 更新营销机会的开发状态
     *
     * @param id
     * @param devResult
     * @return
     */
    @PostMapping("updateSaleChanceDevResult")
    @ResponseBody
    public ResultInfo updateSaleChanceDevResult(Integer id, Integer devResult) {
        saleChanceService.updateSaleChanceDevResult(id,devResult);
        return success("开发状态更新成功");
    }

}




























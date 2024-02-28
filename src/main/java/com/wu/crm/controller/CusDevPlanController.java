package com.wu.crm.controller;

import com.wu.crm.base.BaseController;
import com.wu.crm.base.ResultInfo;
import com.wu.crm.query.CusDevPlanQuery;
import com.wu.crm.query.SaleChanceQuery;
import com.wu.crm.service.CusDevPlanService;
import com.wu.crm.service.SaleChanceService;
import com.wu.crm.utils.LoginUserUtil;
import com.wu.crm.vo.CusDevPlan;
import com.wu.crm.vo.SaleChance;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RequestMapping("cus_dev_plan")
@Controller
public class CusDevPlanController extends BaseController {

    @Resource
    private SaleChanceService saleChanceService;

    @Resource
    private CusDevPlanService cusDevPlanService;

    @RequestMapping("index")
    public String index() {
        return "cusDevPlan/cus_dev_plan";
    }

    /**
     * 打开计划项开发与详情页面
     *
     * @param id
     * @return
     */
    @RequestMapping("toCusDevPlanPage")
    public String toCusDevPlanPage(Integer id, HttpServletRequest request) {

        //通过id查询营销机会对象
        SaleChance saleChance = saleChanceService.selectByPrimaryKey(id);

        //将对象设置到请求域中
        request.setAttribute("saleChance", saleChance);

        return "cusDevPlan/cus_dev_plan_data";
    }

    /**
     * 客户开发计划数据查询（分页多条件查询）
     * @param cusDevPlanQuery
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> queryCusDevPlanByParams(CusDevPlanQuery cusDevPlanQuery){

        return cusDevPlanService.queryCusDevPlanByParams(cusDevPlanQuery);
    }

    /**
     * 添加计划项
     * @param cusDevPlan
     * @return
     */
    @PostMapping("add")
    @ResponseBody
    public ResultInfo addCusDevPlan(CusDevPlan cusDevPlan){
        cusDevPlanService.addCusDevPlan(cusDevPlan);
        return success("计划项添加成功");
    }

    /**
     * 更新计划项
     * @param cusDevPlan
     * @return
     */
    @PostMapping("update")
    @ResponseBody
    public ResultInfo updateCusDevPlan(CusDevPlan cusDevPlan){
        cusDevPlanService.updateCusDevPlan(cusDevPlan);
        return success("计划项更新成功");
    }

    /**
     * 进入添加或修改计划项
     * @return
     */
    @RequestMapping("toAddOrUpdateCusDevPlanPage")
    public String toAddOrUpdateCusDevPlanPage(Integer sId,HttpServletRequest request,Integer id){
        //将营销机会id设置到请求域中给计划项页面获取
        request.setAttribute("sId",sId);

        CusDevPlan cusDevPlan = cusDevPlanService.selectByPrimaryKey(id);
        request.setAttribute("cusDevPlan",cusDevPlan);

        return "cusDevPlan/add_update";
    }

    /**
     * 删除计划项
     * @param id
     * @return
     */
    @PostMapping("delete")
    @ResponseBody
    public ResultInfo deleteCusDevPlan(Integer id){
        cusDevPlanService.deleteCusDevPlan(id);
        return success("删除成功");
    }

}











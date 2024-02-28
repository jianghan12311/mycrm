package com.wu.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wu.crm.base.BaseService;
import com.wu.crm.dao.SaleChanceMapper;
import com.wu.crm.enums.DevResult;
import com.wu.crm.enums.StateStatus;
import com.wu.crm.query.SaleChanceQuery;
import com.wu.crm.utils.AssertUtil;
import com.wu.crm.utils.PhoneUtil;
import com.wu.crm.vo.SaleChance;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SaleChanceService extends BaseService<SaleChance, Integer> {

    @Resource
    private SaleChanceMapper saleChanceMapper;

    /**
     * 多条件分页查询营销机会 (返回的数据格式必须满足LayUI中数据表格要求的格式)
     *
     * @param saleChanceQuery
     * @return
     */
    public Map<String, Object> querySaleChanceByParams(SaleChanceQuery saleChanceQuery) {

        Map<String, Object> map = new HashMap<>();

        //开启分页
        PageHelper.startPage(saleChanceQuery.getPage(), saleChanceQuery.getLimit());
        //得到对应的分页对象
        PageInfo<SaleChance> pageInfo = new PageInfo<>(saleChanceMapper.selectByParams(saleChanceQuery));

        //设置map对象
        map.put("code", 0);
        map.put("msg", "success");
        map.put("count", pageInfo.getTotal());
        //设置分页好的列表
        map.put("data", pageInfo.getList());

        return map;
    }

    /**
     * 添加营销机会
     * 1.参数校验
     * customerName客户名称 非空
     * linkMan联系人 非空
     * linkPhone联系号码 非空,手机号格式正确
     * 2.设置相关参数的默认值
     * createMan创建人 当前登录用户名
     * assignMan指派人
     * 如果未设置指派人默认
     * state分配状态 （0=未分配，1=已分配）
     * 0=未分配
     * assignTime指派时间
     * 设置为null
     * devResult开发状态 （0=未开发，1=已开发，2=开发成功，3=开发失败）
     * 0=未开发 （默认）
     * 如果设置了指派人
     * state分配状态 （0=未分配，1=已分配）
     * 1=已分配
     * assignTime指派时间
     * 系统当前时间
     * devResult开发状态 （0=未开发，1=已开发，2=开发成功，3=开发失败）
     * 1=开发中
     * isValue是否有效 （0=无效，1=有效）
     * 设置为有效
     * createDate创建时间
     * 默认系统当前时间
     * updateDate更新时间
     * 默认系统当前时间
     * 3.执行添加操作，判断受影响的行数
     *
     * @param saleChance
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addSaleChance(SaleChance saleChance) {

        //1. 校验参数
        checkSaleChanceParams(saleChance.getCustomerName(), saleChance.getLinkMan(), saleChance.getLinkPhone());

        //2. 设置相关字段的默认值
        //isValue是否有效 （0=无效，1=有效）
        //设置为有效
        saleChance.setIsValid(1);
        saleChance.setCreateDate(new Date());
        saleChance.setUpdateDate(new Date());
        //判断是否设置了指派人
        if (StringUtils.isBlank(saleChance.getAssignMan())) {
            //如果为空则表示未设置指派人
            saleChance.setState(StateStatus.UNSTATE.getType());
            saleChance.setAssignTime(null);
            saleChance.setDevResult(DevResult.UNDEV.getStatus());
        } else {
            //如果不为空则表示设置了指派人
            saleChance.setState(StateStatus.STATED.getType());
            saleChance.setAssignTime(new Date());
            saleChance.setDevResult(DevResult.DEVING.getStatus());
        }

        //3. 执行添加操作，判断受影响的行数
        AssertUtil.isTrue(saleChanceMapper.insertSelective(saleChance) != 1, "添加营销机会失败");
    }

    //参数校验   判断非空
    private void checkSaleChanceParams(String customerName, String linkMan, String linkPhone) {
        AssertUtil.isTrue(StringUtils.isBlank(customerName), "客户名称不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(linkMan), "联系人不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(linkPhone), "联系号码不能为空");
        //判断手机号码格式是否正确
        AssertUtil.isTrue(!PhoneUtil.isMobile(linkPhone), "联系号码格式不正确");
    }

    /**
     * 更新营销机会
     *
     * @param saleChance
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateSaleChance(SaleChance saleChance) {
        //参数校验
        AssertUtil.isTrue(null == saleChance.getId(), "待更新记录不存在");
        //通过主键查询对象
        SaleChance temp = saleChanceMapper.selectByPrimaryKey(saleChance.getId());
        //判断数据库对应记录是否存在
        AssertUtil.isTrue(temp == null, "待更新记录不存在");
        //参数校验
        checkSaleChanceParams(saleChance.getCustomerName(), saleChance.getLinkMan(), saleChance.getLinkPhone());

        //设置相关参数的默认值
        saleChance.setUpdateDate(new Date());
        //判断修改原始数据是否存在
        if (StringUtils.isBlank(temp.getAssignMan())) {//不存在

            //判断修改后的值是否存在
            if (!StringUtils.isBlank(saleChance.getAssignMan())) { //修改前为空，修改后有值
                saleChance.setAssignTime(new Date());
                saleChance.setState(StateStatus.STATED.getType());
                saleChance.setDevResult(DevResult.DEVING.getStatus());

            }


        } else { //存在
            if (StringUtils.isBlank(saleChance.getAssignMan())) { //修改前有值修改后无值
                saleChance.setAssignTime(null);
                saleChance.setState(StateStatus.UNSTATE.getType());
                saleChance.setDevResult(DevResult.UNDEV.getStatus());
            } else { //修改前有值，修改后有值
                //判断修改前后是否是同一个用户
                if (!saleChance.getAssignMan().equals(temp.getAssignMan())) {
                    //更新指派时间
                    saleChance.setAssignTime(new Date());
                } else {
                    //设置指派时间为修改前的时间
                    saleChance.setAssignTime(temp.getAssignTime());
                }

            }
        }
        //执行更新操作判断受影响的行数
        AssertUtil.isTrue(saleChanceMapper.updateByPrimaryKeySelective(saleChance) != 1, "更新营销机会失败");
    }


    /**
     * 删除营销机会
     *
     * @param ids
     */

    public void deleteSaleChance(Integer[] ids) {
        //判断id是否为空
        AssertUtil.isTrue(null == ids || ids.length < 1, "待删除记录不存在");

        //执行删除操作,判断受影响的行数
        AssertUtil.isTrue(saleChanceMapper.deleteBatch(ids) != ids.length, "营销机会数据删除失败");
    }

    /**
     * 更新营销机会的开发状态
     *
     * @param id
     * @param devResult
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateSaleChanceDevResult(Integer id, Integer devResult) {
        AssertUtil.isTrue(null == id, "待更新记录不存在");
        SaleChance saleChance = saleChanceMapper.selectByPrimaryKey(id);
        AssertUtil.isTrue(null == saleChance, "待更新记录不存在");

        saleChance.setDevResult(devResult);

        AssertUtil.isTrue(saleChanceMapper.updateByPrimaryKeySelective(saleChance) != 1,"开发状态更新失败");
    }

}






















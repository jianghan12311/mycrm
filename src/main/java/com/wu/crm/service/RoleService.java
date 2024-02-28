package com.wu.crm.service;

import com.wu.crm.base.BaseService;
import com.wu.crm.dao.ModuleMapper;
import com.wu.crm.dao.PermissionMapper;
import com.wu.crm.dao.RoleMapper;
import com.wu.crm.utils.AssertUtil;
import com.wu.crm.vo.Permission;
import com.wu.crm.vo.Role;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class RoleService extends BaseService<Role, Integer> {

    @Resource
    private RoleMapper roleMapper;
    @Resource
    private PermissionMapper permissionMapper;
    @Resource
    private ModuleMapper moduleMapper;


    /**
     * 查询所有的角色列表
     *
     * @return
     */
    public List<Map<String, Object>> queryAllRoles(Integer userId) {
        return roleMapper.queryAllRoles(userId);
    }

    /**
     * 添加角色
     * 1.参数校验
     * 角色名称        非空 名称唯一
     * 2.设置参数的默认值
     * 是否有效
     * 创建时间
     * 更新时间
     * 3.执行添加操作，判断受影响的行数
     *
     * @param role
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addRole(Role role) {
        //参数校验
        AssertUtil.isTrue(StringUtils.isBlank(role.getRoleName()), "角色名称不能为空");
        //通过角色名查找角色记录
        Role temp = roleMapper.selectByRoleName(role.getRoleName());
        //判断角色记录是否存在 添加操作时，如果角色记录存在则表示名称不可用
        AssertUtil.isTrue(temp != null, "角色名称已存在请重新输入");

        //设置参数默认值
        role.setIsValid(1);
        role.setCreateDate(new Date());
        role.setUpdateDate(new Date());

        //执行添加操作，判断受影响的行数
        AssertUtil.isTrue(roleMapper.insertSelective(role) < 1, "角色添加失败");
    }

    /**
     * 更新角色
     *
     * @param role
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateRole(Role role) {
        //参数判断 id非空 且数据存在
        AssertUtil.isTrue(null == role.getRoleName(), "待更新记录不存在");
        //通过角色id查询用户记录
        Role temp = roleMapper.selectByPrimaryKey(role.getId());
        AssertUtil.isTrue(null == temp, "待更新记录不存在");
        //角色名称  非空 名称唯一
        AssertUtil.isTrue(StringUtils.isBlank(role.getRoleName()), "角色名称不能为空");
        temp = roleMapper.selectByRoleName(role.getRoleName());
        AssertUtil.isTrue(null != temp && (!temp.getId().equals(role.getId())), "角色名已存在，请重新输入");

        //设置默认值
        role.setUpdateDate(new Date());

        //执行更新操作判断受影响的行数
        AssertUtil.isTrue(roleMapper.updateByPrimaryKeySelective(role) < 1, "修改角色失败");
    }

    /**
     * 删除角色
     * @param rileId
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteRole(Integer rileId){
        AssertUtil.isTrue(null == rileId,"待删除记录不存在");
        Role role = roleMapper.selectByPrimaryKey(rileId);
        AssertUtil.isTrue(null == role,"待删除记录不存在");

        role.setIsValid(0);
        role.setUpdateDate(new Date());

        AssertUtil.isTrue(roleMapper.updateByPrimaryKeySelective(role) < 1,"角色删除失败");
    }

    /**
     * 角色授权
     *  将对应的角色id与资源id添加到对应的权限表中
     * @param roleId
     * @param mIds
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addGrant(Integer roleId, Integer[] mIds) {
        //1.通过角色id查询对应的权限记录
        Integer count = permissionMapper.countPermissionByRoleId(roleId);

        //2.如果权限记录存在，则删除对应的角色拥有的权限记录
        if (count > 0){
            //删除权限记录
            permissionMapper.deletePermissionByRoleId(roleId);
        }

        //3.如果有权限记录，则添加权限记录
        if (mIds != null && mIds.length > 0){
            //定义permission集合
            List<Permission> permissionsList = new ArrayList<>();

            //遍历资源id数组
            for (Integer mId: mIds){
                Permission permission = new Permission();
                permission.setModuleId(mId);
                permission.setRoleId(roleId);
                permission.setAclValue(moduleMapper.selectByPrimaryKey(mId).getOptValue());
                permission.setCreateDate(new Date());
                permission.setUpdateDate(new Date());
                //将对象设置到集合中
                permissionsList.add(permission);

            }

            //执行批量添加的存在，判断受影响的行数
            AssertUtil.isTrue(permissionMapper.insertBatch(permissionsList) != permissionsList.size(),"角色授权失败");
        }
    }

}

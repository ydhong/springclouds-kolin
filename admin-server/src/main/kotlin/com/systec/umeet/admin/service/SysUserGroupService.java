package com.systec.umeet.admin.service;

import com.systec.umeet.admin.mapper.SysUserGroupMapper;
import com.systec.umeet.admin.mapper.SysUserGroupPkMapper;
import com.systec.umeet.admin.mapper.SysUserGroupRolePkMapper;
import com.systec.umeet.admin.model.SysUserGroup;
import com.systec.umeet.admin.model.SysUserGroupPk;
import com.systec.umeet.admin.model.SysUserGroupRolePk;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @description:
 * @author: super.wu
 * @date: Created in 2018/5/9 0009
 * @modified By:
 * @version: 1.0
 **/
@Service
@Transactional(rollbackFor = Exception.class)
public class SysUserGroupService extends BaseService<SysUserGroupMapper, SysUserGroup> {

    @Autowired
    private SysUserGroupPkMapper sysUserGroupPkMapper;

    @Autowired
    private SysUserGroupRolePkMapper sysUserGroupRolePkMapper;

    @Override
    public void deleteById(Object id) {
        int groupId = Integer.valueOf(id.toString());
        // 删除用户组和用户关联
        SysUserGroupPk userGroupPk = new SysUserGroupPk();
        userGroupPk.setGroupId(groupId);
        sysUserGroupPkMapper.delete(userGroupPk);
        // 删除用户组和角色关联
        SysUserGroupRolePk userGroupRolePk = new SysUserGroupRolePk();
        userGroupRolePk.setGroupId(Integer.valueOf(id.toString()));
        sysUserGroupRolePkMapper.delete(userGroupRolePk);
        // 删除用户组
        super.deleteById(id);
    }

}

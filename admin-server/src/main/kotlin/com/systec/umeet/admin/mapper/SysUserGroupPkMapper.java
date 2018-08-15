package com.systec.umeet.admin.mapper;

import com.systec.umeet.admin.model.SysUserGroupPk;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface SysUserGroupPkMapper extends Mapper<SysUserGroupPk> {

    Integer insertGroupUsers(List<SysUserGroupPk> list);

    Integer deleteGroupUsers(Map<String, Object> map);

    Integer updateGroupUser(Map<String, Object> map);

}

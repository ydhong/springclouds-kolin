package com.systec.umeet.admin.mapper;

import com.systec.umeet.admin.model.SysUserRolePk;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: super.wu
 * @date: Created in 2018/5/14 0014
 * @modified By:
 * @version: 1.0
 **/
public interface SysUserRolePkMapper extends Mapper<SysUserRolePk> {

    Integer insertUserRoles(List<SysUserRolePk> list);

    Integer deleteUserRoles(Map<String, Object> map);


}

package com.systec.umeet.admin.mapper;

import com.systec.umeet.admin.model.SysUser;
import com.systec.umeet.admin.vo.SysUserInfo;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface SysUserMapper extends Mapper<SysUser> {

    SysUserInfo selectUserGroup(Long userId);

    List<SysUser> selectGroupNoUsers(Map<String, Object> map);

    List<SysUser> selectGroupUsers(Map<String, Object> map);

}

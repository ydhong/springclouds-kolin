package com.systec.umeet.admin.mapper;

import com.systec.umeet.admin.model.SysPermissionMenuPk;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface SysPermissionMenuPkMapper extends Mapper<SysPermissionMenuPk> {

    Integer insertPermissionMenus(List<SysPermissionMenuPk> list);

    Integer deletePermissionMenus(Map<String, Object> map);
}

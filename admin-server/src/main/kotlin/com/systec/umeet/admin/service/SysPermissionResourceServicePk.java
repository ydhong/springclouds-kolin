package com.systec.umeet.admin.service;

import com.systec.umeet.admin.dto.PermissionResourceDto;
import com.systec.umeet.admin.mapper.SysPermissionResourcePkMapper;
import com.systec.umeet.admin.mapper.SysResourceMapper;
import com.systec.umeet.admin.model.SysPermissionResourcePk;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: super.wu
 * @date: Created in 2018/5/15 0015
 * @modified By:
 * @version: 1.0
 **/
@Service
@Transactional(rollbackFor = Exception.class)
public class SysPermissionResourceServicePk extends BaseService<SysPermissionResourcePkMapper, SysPermissionResourcePk> {

    @Autowired
    private SysResourceMapper sysResourceMapper;

    public void setSysResourceMapper(SysResourceMapper sysResourceMapper) {
        this.sysResourceMapper = sysResourceMapper;
    }

    private Map<String, Object> getQueryParams(PermissionResourceDto dto) {
        return new HashMap<String, Object>(1){{
            put("permissionId", dto.getPermissionId());
            if (StringUtils.isNotBlank(dto.getName())) {
                put("name", dto.getName());
            }
        }};
    }

    public Integer batchInsert(List<SysPermissionResourcePk> list) {
        return this.mapper.insertPermissionResources(list);
    }

    public Integer batchDelete(Integer permissionId, Long [] resourceIds) {
        Map<String, Object> map = new HashMap<String, Object>(2){{
            put("permissionId", permissionId);
            put("resourceIds", resourceIds);
        }};
        return this.mapper.deletePermissionResources(map);
    }
}

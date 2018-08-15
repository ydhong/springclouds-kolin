package com.systec.umeet.admin.service;

import com.systec.umeet.admin.mapper.SysMenuMapper;
import com.systec.umeet.admin.model.MenuTree;
import com.systec.umeet.admin.model.SysMenu;
import com.systec.umeet.admin.util.MenuTreeUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * @description:
 * @author: super.wu
 * @date: Created in 2018/5/10 0010
 * @modified By:
 * @version: 1.0
 **/
@Service
@Transactional(rollbackFor = Exception.class)
public class SysMenuService extends BaseService<SysMenuMapper, SysMenu> {

    public List<MenuTree> loadAllMenuTree() {
        List<MenuTree> list = this.mapper.selectByAll(1);
        return MenuTreeUtil.generateMenuTree(list);
    }

}

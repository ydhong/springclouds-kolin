package com.systec.umeet.service
import com.github.pagehelper.PageInfo
import com.systec.umeet.base.BaseServiceImpl
import com.systec.umeet.contants.ResponsePage
import com.systec.umeet.dao.SysMenuDao
import com.systec.umeet.model.MenuTree
import com.systec.umeet.model.SysMenu
import com.systec.umeet.utills.MenuTreeUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import tk.mybatis.mapper.common.Mapper

@Service
class SysMenuService : BaseServiceImpl<SysMenu> (){

    @Autowired
    var sysMenuDao : SysMenuDao? = null;

    override fun getMapper(): Mapper<SysMenu> {
        return sysMenuDao!!;
    }

    /**
     * 根据条件分页查询用户
     */
    fun getPage(record: SysMenu): ResponsePage<SysMenu> {
        return super.selectPage(record.page , record.size ,record)
    }

    fun loadAllMenuTree(): List<MenuTree> {
        val list = sysMenuDao!!.selectByAll(1)
        return MenuTreeUtil.generateMenuTree(list)
    }

}

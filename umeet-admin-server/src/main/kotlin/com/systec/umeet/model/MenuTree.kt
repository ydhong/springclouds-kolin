package com.systec.umeet.model

class MenuTree(

        /**
         * 角色名称
         */
        var parentName :String? = null,
        /**
         * 扩展属性 子级列表
         */
        var children: List<MenuTree>? = null,
        /**
         * 是否找到当前父节点，MenuTreeUtil类遍历节点的时候用，保证所有父节点只会被查找一次，不会重复查询
         */

        var findParentNode: Boolean = false

) :SysMenu(){}

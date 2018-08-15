package com.systec.umeet.contants

import com.github.pagehelper.PageInfo
import lombok.Data

/**
 * @description:
 * @author: super.wu
 * @date: Created in 2018/5/9 0009
 * @modified By:
 * @version: 1.0
 */
@Data
class ResponsePage<T>(list: List<T>) {
     val page: Int
     val pages: Int
     val size: Int
     val total: Long
     val list: List<T>

    init {
        val pageInfo = PageInfo(list)
        this.pages = pageInfo.pages
        this.page = pageInfo.nextPage
        this.size = pageInfo.pageSize
        this.total = pageInfo.total
        this.list = pageInfo.list
    }
}

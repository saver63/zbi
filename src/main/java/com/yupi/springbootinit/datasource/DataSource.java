package com.yupi.springbootinit.datasource;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * 数据源接口(新接入数据源必须实现)
 * 适配器模式:定义其它用户接入系统时的规范
 * 应用场景：给不认识的人用的系统
 */
public interface DataSource <T> {

    /**
     * 搜索
     * @param searchText
     * @param pageNum
     * @param pageSize
     * @return
     */
    Page<T> doSearch(String searchText, long pageNum,long pageSize);
}

package com.yupi.springbootinit.model.vo;

import lombok.Data;

/**
 * Bi 的返回结果
 */
@Data
public class BiResponse {

    /**
     * 生成的图表代码
     */
    private String genChart;

    /**
     * 图表分析结果
     */
    private String genResult;

    /**
     * 图表的id
     */
    private Long chartId;
}

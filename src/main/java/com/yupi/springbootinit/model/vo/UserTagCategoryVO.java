package com.yupi.springbootinit.model.vo;

import lombok.Data;

import java.util.List;

@Data
public class UserTagCategoryVO {

    private long id;

    private String name;

    private List<UserTagVO> tags;
}

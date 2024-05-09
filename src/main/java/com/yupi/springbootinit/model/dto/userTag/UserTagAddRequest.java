package com.yupi.springbootinit.model.dto.userTag;

import lombok.Data;

@Data
public class UserTagAddRequest {

    /**
     * 父级id
     */
    private Long parentId;

    /**
     * 名称
     *
     */
    private String name;

}

package com.zxr.bsoj.model.dto.posts;


import lombok.Data;

import java.util.List;

/**
 * 创建帖子
 */
@Data
public class AddPostRequest {

    /**
     * 帖子标题，不能为空
     */
    private String title;

    /**
     * 帖子内容，不能为空
     */
    private String content;


    /**
     * 编程语言（题解专用），可为空
     */
    private String language;

    /**
     * 代码内容（题解专用），可为空
     */
    private String code;

    /**
     * 题解版本（题解专用），可为空
     */
    private String version;


    /**
     * 帖子类型，不能为空，默认值为 solution_post
     */
    private String type;

    /**
     * 标签（JSON格式），可为空
     */
    private List<String> tags;

}

package com.zxr.bsoj.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 帖子表，用于存储用户发布的帖子信息
 * @TableName posts
 */
@TableName(value ="posts")
@Data
public class Posts implements Serializable {
    /**
     * 帖子ID（主键），自增
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 发布者ID（外键），不能为空
     */
    private Long userId;

    /**
     * 帖子标题，不能为空
     */
    private String title;

    /**
     * 帖子内容，不能为空
     */
    private String content;

    /**
     * 题目ID（外键，可为空）
     */
    private Long problemId;

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
     * 帖子状态，不能为空，默认值为 "draft"。draft,published,archived
     */
    private String status;

    /**
     * 发布时间，不能为空，默认值为当前时间
     */
    private Date publishTime;

    /**
     * 最后更新时间，不能为空，默认值为当前时间，并在更新时自动更新
     */
    private Date updateTime;

    /**
     * 浏览量，可为空，默认值为 0
     */
    private Integer views;

    /**
     * 点赞量，可为空，默认值为 0
     */
    private Integer likes;

    /**
     * 评论数量，可为空，默认值为 0
     */
    private Integer commentsCount;

    /**
     * 帖子类型，不能为空，默认值为 solution_post
     */
    private String type;

    /**
     * 标签（JSON格式），可为空
     */
    private String tags;

    /**
     * 逻辑删除标志(0:未删除,1:已删除)
     */
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
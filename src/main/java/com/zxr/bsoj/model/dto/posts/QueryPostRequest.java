package com.zxr.bsoj.model.dto.posts;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zxr.bsoj.common.PageRequest;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


@TableName(value ="posts")
@Data
public class QueryPostRequest extends PageRequest implements Serializable {
    /**
     * 帖子ID（主键），自增
     */
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
    private Integer problemId;

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

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;

    /**
     * 帖子类型，不能为空，默认值为 solution_post
     */
    private String type;

    /**
     * 标签（JSON格式），可为空
     */
    private List<String> tags;


    private static final long serialVersionUID = 1L;
}
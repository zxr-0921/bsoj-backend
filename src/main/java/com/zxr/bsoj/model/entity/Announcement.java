package com.zxr.bsoj.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 公告表
 * @TableName announcement
 */
@TableName(value ="announcement")
@Data
@ApiModel(value = "公告表")
public class Announcement implements Serializable {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 公告类别(system_notice, system_announcement)
     */
    private String category;

    /**
     * 公告标题
     */
    private String title;

    /**
     * 公告内容
     */
    private String content;

    /**
     * 发布人用户ID
     */
    private Long publisherId;

    /**
     * 发布人姓名
     */
    private String publisherName;

    /**
     * 公告状态(draft, published, deleted)
     */
    private String status;

    /**
     * 发布时间
     */
    private Date releaseTime;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 逻辑删除标志(0:未删除,1:已删除)
     */
    private Integer isDelete;

    /**
     * 权重，用于排序显示（数值越大越靠前）
     */
    private Integer weight;

    /**
     * 排序字段，用户手动调整顺序
     */
    private Integer sortOrder;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
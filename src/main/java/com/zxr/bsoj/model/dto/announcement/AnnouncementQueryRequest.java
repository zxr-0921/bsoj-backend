package com.zxr.bsoj.model.dto.announcement;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zxr.bsoj.common.PageRequest;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class AnnouncementQueryRequest extends PageRequest implements Serializable {

    private static final long serialVersionUID = 1L;
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
     * 发布人姓名
     */
    private String publisherName;
    /**
     * 公告状态(draft, published, deleted)
     */
    private String status;
    /**
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date  startTime;

    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;
    /**
     * 权重，用于排序显示（数值越大越靠前）
     */
    private Integer weight;
    /**
     * 排序字段，用户手动调整顺序
     */
    private Integer sortOrder;
}

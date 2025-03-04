package com.zxr.bsoj.model.dto.notification;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class NotificationUpdateRequest implements Serializable {
    /**
     * id
     */
    private Long id;
    /**
     * 公告标题
     */
    private String title;
    /**
     * 公告内容
     */
    private String content;
    /**
     * 开始时间
     */
    private Date startTime;
    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 0: 关闭，1: 启用
     */
    private Integer status;

    private static final long serialVersionUID = 1L;
}
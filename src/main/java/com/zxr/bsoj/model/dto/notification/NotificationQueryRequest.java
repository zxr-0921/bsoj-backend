package com.zxr.bsoj.model.dto.notification;

import com.zxr.bsoj.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class NotificationQueryRequest extends PageRequest implements Serializable {

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
     * 通知类型
     */
    private String type;


    /**
     * 0: 关闭，1: 启用
     */
    private Integer status;




    private static final long serialVersionUID = 1L;
}

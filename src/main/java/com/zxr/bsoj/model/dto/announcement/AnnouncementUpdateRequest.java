package com.zxr.bsoj.model.dto.announcement;

import lombok.Data;

@Data
public class AnnouncementUpdateRequest {

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
     * 公告状态(draft, published, deleted)
     */
    private String status;

}
package com.zxr.bsoj.model.vo;

import cn.hutool.json.JSONUtil;
import com.zxr.bsoj.model.dto.question.JudgeConfig;
import com.zxr.bsoj.model.entity.Announcement;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Date;
import java.util.List;

/**
 * 前端公告展示对象
 */
@Data
public class AnnouncementVO {
    /**
     * 主键ID
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
     * 发布人姓名
     */
    private String publisherName;


    /**
     * 发布时间
     */
    private Date releaseTime;

    /**
     * 包装类
     */
    public static AnnouncementVO objToVo(Announcement announcement) {
        if (announcement == null) {
            return null;
        }
        AnnouncementVO announcementVO = new AnnouncementVO();
        BeanUtils.copyProperties(announcement, announcementVO);

        return announcementVO;
    }

}
package com.zxr.bsoj.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zxr.bsoj.model.dto.announcement.AnnouncementAddRequest;
import com.zxr.bsoj.model.dto.announcement.AnnouncementQueryRequest;
import com.zxr.bsoj.model.entity.Announcement;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zxr.bsoj.model.vo.AnnouncementVO;

import javax.servlet.http.HttpServletRequest;

/**
* @author zxr0921
* @description 针对表【announcement(公告表)】的数据库操作Service
* @createDate 2025-02-11 12:59:06
*/
public interface AnnouncementService extends IService<Announcement> {
    // 创建公告
    long createAnnouncement(AnnouncementAddRequest announcementAddRequest, HttpServletRequest request);

    // 发布公告
    boolean publishAnnouncement(long id);

    // 撤销公告
    boolean revokeAnnouncement(long id);

    // 获取查询条件
    QueryWrapper<Announcement> getQueryWrapper(AnnouncementQueryRequest announcementQueryRequest);

    Page<AnnouncementVO> getAnnouncementVOPage(Page<Announcement> announcementPage, HttpServletRequest request);
}

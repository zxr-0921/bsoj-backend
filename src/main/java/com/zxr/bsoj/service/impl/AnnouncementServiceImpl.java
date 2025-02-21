package com.zxr.bsoj.service.impl;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zxr.bsoj.constant.AnnouncementConstant;
import com.zxr.bsoj.constant.CommonConstant;
import com.zxr.bsoj.mapper.AnnouncementMapper;
import com.zxr.bsoj.model.dto.announcement.AnnouncementAddRequest;
import com.zxr.bsoj.model.dto.announcement.AnnouncementQueryRequest;
import com.zxr.bsoj.model.entity.Announcement;
import com.zxr.bsoj.model.entity.User;
import com.zxr.bsoj.model.vo.AnnouncementVO;
import com.zxr.bsoj.service.AnnouncementService;
import com.zxr.bsoj.service.UserService;
import com.zxr.bsoj.utils.SqlUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.zxr.bsoj.constant.AnnouncementConstant.*;

/**
 * @author zxr0921
 * @description 针对表【announcement(公告表)】的数据库操作Service实现
 * @createDate 2025-02-11 12:59:06
 */
@Service
public class AnnouncementServiceImpl extends ServiceImpl<AnnouncementMapper, Announcement>
        implements AnnouncementService {
    @Resource
    private AnnouncementMapper announcementMapper;

    @Resource
    private UserService userService;

    /**
     * 创建公告
     *
     * @param announcementAddRequest
     * @param request
     * @return
     */
    @Override
    public long createAnnouncement(AnnouncementAddRequest announcementAddRequest, HttpServletRequest request) {
        //1. 将请求参数转换为实体类
        Announcement announcement = new Announcement();
        announcement.setCategory(announcementAddRequest.getCategory());
        announcement.setTitle(announcementAddRequest.getTitle());
        announcement.setContent(announcementAddRequest.getContent());
        String status = announcementAddRequest.getStatus();
        if (Objects.equals(status, STATUS_PUBLISHED)) {
            announcement.setStatus(STATUS_PUBLISHED);
            announcement.setReleaseTime(new DateTime());
        } else {
            announcement.setStatus(STATUS_DRAFT);
        }
        //2. 插入数据库
        User loginUser = userService.getLoginUser(request);
        announcement.setPublisherId(loginUser.getId());
        announcement.setPublisherName(loginUser.getUserName());
        announcementMapper.insert(announcement);
        return announcement.getId();

    }

    /**
     * 发布公告
     *
     * @param id
     * @return
     */
    @Override
    public boolean publishAnnouncement(long id) {
        // 根据id发布公告
        Announcement announcement = announcementMapper.selectById(id);
        announcement.setStatus(STATUS_PUBLISHED);
        announcement.setReleaseTime(new DateTime());
        // 更新数据库
        int result = announcementMapper.updateById(announcement);
        if (result > 0) {
            return true;
        }
        return false;
    }

    /**
     * 撤销公告
     *
     * @param id
     * @return
     */
    @Override
    public boolean revokeAnnouncement(long id) {
        // 根据id撤销公告
        Announcement announcement = announcementMapper.selectById(id);
        announcement.setStatus(STATUS_REVOKE);
        // 更新数据库
        int result = announcementMapper.updateById(announcement);
        if (result > 0) {
            return true;
        }
        return false;
    }

    /**
     * 获取查询条件
     *
     * @param announcementQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<Announcement> getQueryWrapper(AnnouncementQueryRequest announcementQueryRequest) {
        QueryWrapper<Announcement> queryWrapper = new QueryWrapper<>();
        if (announcementQueryRequest == null) {
            return queryWrapper;
        }
        String category = announcementQueryRequest.getCategory();
        String status = announcementQueryRequest.getStatus();
        String title = announcementQueryRequest.getTitle();
        String publisherName = announcementQueryRequest.getPublisherName();
        Date startTime = announcementQueryRequest.getStartTime();
        Date endTime = announcementQueryRequest.getEndTime();
        // 类别
        queryWrapper.eq(StringUtils.isNotBlank(category), "category", category);
        // 状态
        queryWrapper.eq(StringUtils.isNotBlank(status), "status", status);
        // 标题
        queryWrapper.like(StringUtils.isNotBlank(title), "title", title);
        // 发布人
        queryWrapper.like(StringUtils.isNotBlank(publisherName), "publisherName", publisherName);

        if (startTime != null && endTime != null) {
            queryWrapper.between("releaseTime", startTime, endTime);
        }
        queryWrapper.eq("isDelete", false);

        queryWrapper.orderBy(SqlUtils.validSortField(announcementQueryRequest.getSortField()), announcementQueryRequest.getOrder().equals(CommonConstant.SORT_ORDER_ASC), announcementQueryRequest.getSortField());

        return queryWrapper;
    }

    @Override
    public Page<AnnouncementVO> getAnnouncementVOPage(Page<Announcement> announcementPage, HttpServletRequest request) {
        // 1. 获取分页信息
        long pageSize = announcementPage.getSize();
        long current = announcementPage.getCurrent();
        List<Announcement> announcementList = announcementPage.getRecords();
        Page<AnnouncementVO> announcementVOPage = new Page<>(current, pageSize, announcementPage.getTotal());
        //如果没有数据，直接返回
        if (announcementList.isEmpty()) {
            return announcementVOPage;
        }
        // 获取内容
        // 2. 获取公告列表
        // 流式处理
        List<AnnouncementVO> announcementVOList = announcementList
                .stream()
                .map(AnnouncementVO::objToVo
                ).collect(Collectors.toList());
        announcementVOPage.setRecords(announcementVOList);
        return announcementVOPage;
    }
}





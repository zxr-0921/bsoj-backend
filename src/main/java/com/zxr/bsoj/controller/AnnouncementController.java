package com.zxr.bsoj.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zxr.bsoj.annotation.AuthCheck;
import com.zxr.bsoj.common.BaseResponse;
import com.zxr.bsoj.common.ErrorCode;
import com.zxr.bsoj.common.ResultUtils;
import com.zxr.bsoj.constant.AnnouncementConstant;
import com.zxr.bsoj.constant.UserConstant;
import com.zxr.bsoj.exception.BusinessException;
import com.zxr.bsoj.exception.ThrowUtils;
import com.zxr.bsoj.model.dto.announcement.AnnouncementAddRequest;
import com.zxr.bsoj.model.dto.announcement.AnnouncementQueryRequest;
import com.zxr.bsoj.model.dto.announcement.AnnouncementUpdateRequest;
import com.zxr.bsoj.model.entity.Announcement;
import com.zxr.bsoj.model.vo.AnnouncementVO;
import com.zxr.bsoj.service.AnnouncementService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/announcement")
@Slf4j
public class AnnouncementController {
    @Resource
    private AnnouncementService announcementService;

    // region 公告相关(管理员)

    /**
     * 新建公告
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Announcement> createAnnouncement(@RequestBody AnnouncementAddRequest announcementAddRequest,
                                                         HttpServletRequest request) {
        if (announcementAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //1. 参数校验
        String category = announcementAddRequest.getCategory();
        String title = announcementAddRequest.getTitle();
        String content = announcementAddRequest.getContent();
        // 如果有空值，返回参数错误
        if (StringUtils.isAnyBlank(category, title, content)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 创建公告
        long result = announcementService.createAnnouncement(announcementAddRequest, request);
        // 根据返回结果判断是否成功
        if (result <= 0) {
            throw new BusinessException(ErrorCode.CREATE_ANNOUNCEMENT_ERROR);
        }
        // 根据id返回公告
        Announcement announcement = announcementService.getById(result);
        return ResultUtils.success(announcement);
    }

    /**
     * 根据id获取公告
     */
    @GetMapping("/get/{id}")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Announcement> getAnnouncement(@PathVariable("id") long id) {
        // 根据id获取公告
        Announcement announcement = announcementService.getById(id);
        // 如果公告不存在，返回错误
        if (announcement == null) {
            throw new BusinessException(ErrorCode.ANNOUNCEMENT_NOT_EXIST);
        }
        return ResultUtils.success(announcement);
    }
    /**
     * 发布公告
     */
    @PostMapping("/publish")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> publishAnnouncement(long id) {
        // 根据id发布公告
        boolean result = announcementService.publishAnnouncement(id);

        // 根据返回结果判断是否成功
        if (!result) {
            throw new BusinessException(ErrorCode.PUBLISH_ANNOUNCEMENT_ERROR);
        }
        return ResultUtils.success(true, "发布成功");
    }

    /**
     * 撤销公告
     */
    @PostMapping("/revoke")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> revokeAnnouncement(long id) {
        // 根据id撤销公告
        boolean result = announcementService.revokeAnnouncement(id);

        // 根据返回结果判断是否成功
        if (!result) {
            throw new BusinessException(ErrorCode.REVOKE_ANNOUNCEMENT_ERROR);
        }
        return ResultUtils.success(true, "撤销成功");
    }

    /**
     * 删除公告
     */
    @DeleteMapping("/delete/{id}")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteAnnouncement(@PathVariable("id") long id) {
        // 根据id删除公告
        Announcement announcement = announcementService.getById(id);
        announcement.setStatus(AnnouncementConstant.STATUS_DELETED);
        announcementService.updateById(announcement);
        boolean result = announcementService.removeById(id);
        // 根据返回结果判断是否成功
        if (!result) {
            throw new BusinessException(ErrorCode.DELETE_ANNOUNCEMENT_ERROR);
        }
        return ResultUtils.success(true, "删除成功");
    }

    /**
     * 获取公告列表
     */
    @PostMapping("/list")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<Announcement>> listAnnouncement(@RequestBody AnnouncementQueryRequest announcementQueryRequest, HttpServletRequest request) {
        // 获取分页信息
        long pageSize = announcementQueryRequest.getPageSize();
        long current = announcementQueryRequest.getCurrent();
        // 限制爬虫
        ThrowUtils.throwIf(pageSize > 20, ErrorCode.PARAMS_ERROR);
        Page<Announcement> announcementPage = announcementService.page(new Page<>(current, pageSize),
                announcementService.getQueryWrapper(announcementQueryRequest));
        return ResultUtils.success(announcementPage);

    }

    /**
     * 编辑公告
     */
    @PostMapping("/edit")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Announcement> editAnnouncement(@RequestBody AnnouncementUpdateRequest announcementUpdateRequest, HttpServletRequest request) {
        //1. 参数校验
        Long id = announcementUpdateRequest.getId();
        String category = announcementUpdateRequest.getCategory();
        String title = announcementUpdateRequest.getTitle();
        String content = announcementUpdateRequest.getContent();
        // 如果有空值，返回参数错误
        if (StringUtils.isAnyBlank(category, title, content)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 编辑公告
        Announcement announcement = announcementService.getById(id);
        announcement.setCategory(category);
        announcement.setTitle(title);
        announcement.setContent(content);
        announcementService.updateById(announcement);
        return ResultUtils.success(announcement, "更新成功");
    }


    // endregion

    // region 公告相关(用户)

    /**
     * 获取公告列表
     */
    @PostMapping("/list/user")
    public BaseResponse<Page<AnnouncementVO>> listAnnouncementForUser(@RequestBody AnnouncementQueryRequest announcementQueryRequest, HttpServletRequest request) {
        // 获取分页信息
        long pageSize = announcementQueryRequest.getPageSize();
        long current = announcementQueryRequest.getCurrent();
        // 限制爬虫
        ThrowUtils.throwIf(pageSize > 20, ErrorCode.PARAMS_ERROR);
        //获取发布的公告
        LambdaQueryWrapper<Announcement> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Announcement::getStatus, AnnouncementConstant.STATUS_PUBLISHED);
        Page<Announcement> announcementPage = announcementService.page(new Page<>(current, pageSize),
                queryWrapper);

        return ResultUtils.success(announcementService.getAnnouncementVOPage(announcementPage, request));
    }

    // endregion

}

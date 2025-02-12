package com.zxr.bsoj.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.zxr.bsoj.annotation.AuthCheck;
import com.zxr.bsoj.common.BaseResponse;
import com.zxr.bsoj.common.DeleteRequest;
import com.zxr.bsoj.common.ErrorCode;
import com.zxr.bsoj.common.ResultUtils;
import com.zxr.bsoj.constant.UserConstant;
import com.zxr.bsoj.exception.BusinessException;
import com.zxr.bsoj.exception.ThrowUtils;
import com.zxr.bsoj.model.dto.notification.NotificationAddRequest;
import com.zxr.bsoj.model.dto.notification.NotificationQueryRequest;
import com.zxr.bsoj.model.dto.notification.NotificationUpdateRequest;
import com.zxr.bsoj.model.entity.Notification;
import com.zxr.bsoj.model.entity.User;
import com.zxr.bsoj.model.vo.NotificationVO;
import com.zxr.bsoj.service.NotificationService;
import com.zxr.bsoj.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 帖子接口
 */
@RestController
@RequestMapping("/notificition")
@Slf4j
public class NotificationController {

    private final static Gson GSON = new Gson();
    @Resource
    private NotificationService notificationService;
    @Resource
    private UserService userService;

    // region 增删改查

    /**
     * 创建公告
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addNotification(@RequestBody NotificationAddRequest notificationAddRequest,
                                              HttpServletRequest request) {
        // 参数校验
        if (notificationAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Notification notification = new Notification();
        BeanUtils.copyProperties(notificationAddRequest, notification);

        // 校验
        notificationService.validNotification(notification, true);
        User loginUser = userService.getLoginUser(request);
        notification.setUserId(String.valueOf(loginUser.getId()));

        boolean result = notificationService.save(notification);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        long newNotificationId = notification.getId();
        return ResultUtils.success(newNotificationId);
    }


    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteNotification(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        Notification oldNotification = notificationService.getById(id);
        ThrowUtils.throwIf(oldNotification == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldNotification.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = notificationService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新（仅管理员）
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateNotification(@RequestBody NotificationUpdateRequest notificationUpdateRequest) {
        //1.判断是否存在当前公告
        if (notificationUpdateRequest == null || notificationUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 2. 封装对象
        Notification notification = new Notification();
        BeanUtils.copyProperties(notificationUpdateRequest, notification);

        // 3. 判断当前是否是创建状态
        notificationService.validNotification(notification, false);
        long id = notificationUpdateRequest.getId();
        // 判断是否存在
//        Notification oldNotification = notificationService.getById(id);
        long count = notificationService.count(new QueryWrapper<Notification>().eq("id", id));
        ThrowUtils.throwIf(count == 0, ErrorCode.NOT_FOUND_ERROR);
        boolean result = notificationService.updateById(notification);
        return ResultUtils.success(result);
    }

    /**
     * 前端获取发布的公告列表
     */
    @PostMapping("/list")
    public BaseResponse<Page<NotificationVO>> listNotification(@RequestBody NotificationQueryRequest notificationQueryRequest) {
        // 获取分页
        long current = notificationQueryRequest.getCurrent();
        long pageSize = notificationQueryRequest.getPageSize();
        ThrowUtils.throwIf(pageSize > 20, ErrorCode.PARAMS_ERROR);
        // 获取所有公告
        // 获取所有状态为1的公告
        QueryWrapper<Notification> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1);
        Page<Notification> notificationPage = notificationService.page(new Page<>(current, pageSize), queryWrapper);
        // 将Notification实体列表转换为NotificationVO对象列表
        List<Notification> notificationList = notificationPage.getRecords();
        List<NotificationVO> notificationVOList = notificationList.stream()
                .map(notification -> {
                    NotificationVO notificationVO = new NotificationVO();
                    BeanUtils.copyProperties(notification, notificationVO);
                    return notificationVO;
                })
                .collect(Collectors.toList());
        // 返回分页对象
        Page<NotificationVO> notificationVOPage = new Page<>(current, pageSize, notificationPage.getTotal());
        notificationVOPage.setRecords(notificationVOList);
        return ResultUtils.success(notificationVOPage);

    }

    /**
     * 根据id获取通知
     */
    @GetMapping("/get")
    public BaseResponse<Notification> getNotificationById(@RequestParam long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Notification notification = notificationService.getById(id);
        if (notification == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return ResultUtils.success(notification);
    }

    /**
     * 改变状态
     */
    @PostMapping("/change/status")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> changeNotificationStatus(@RequestParam long id, @RequestParam int status) {
        if (id <= 0 || status < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Notification notification = new Notification();
        notification.setId(id);
        notification.setStatus(status);
        boolean result = notificationService.updateById(notification);
        return ResultUtils.success(result);
    }

    /**
     * 获取通知列表(管理员)
     */
    @PostMapping("/list/admin")
    public BaseResponse<Page<Notification>> listNotificationAdmin(@RequestBody NotificationQueryRequest notificationQueryRequest) {
        // 获取所有公告
        long current = notificationQueryRequest.getCurrent();
        long pageSize = notificationQueryRequest.getPageSize();
        Page<Notification> notificationPage = notificationService.page(new Page<>(current, pageSize));
        return ResultUtils.success(notificationPage);
    }
}

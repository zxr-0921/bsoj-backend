package com.zxr.bsoj.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
    @GetMapping("/list")
    public BaseResponse<List<NotificationVO>> listNotification() {
        List<Notification> notificationList = notificationService.list(new QueryWrapper<Notification>().eq("status", 1));
        // 将Notification实体列表转换为NotificationVO对象列表
        List<NotificationVO> notificationVOList = notificationList.stream()
                .map(notification -> {
                    NotificationVO notificationVO = new NotificationVO();
                    BeanUtils.copyProperties(notification, notificationVO);
                    return notificationVO;
                })
                .collect(Collectors.toList());

        return ResultUtils.success(notificationVOList);
    }


//    @GetMapping("/get/vo")
//    public BaseResponse<NotificationVO> getNotificationVOById(HttpServletRequest request) {
//        if (id <= 0) {
//            throw new BusinessException(ErrorCode.PARAMS_ERROR);
//        }
//        Notification notificition = notificationService.getById(id);
//        if (notificition == null) {
//            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
//        }
//        return ResultUtils.success(notificationService.getNotificationVO(notificition, request));
//    }

    /**
     * 分页获取列表（封装类）
     *
     * @return
     */
//    @PostMapping("/list/page/vo")
//    public BaseResponse<Page<NotificationVO>> listNotificationVOByPage(@RequestBody NotificationQueryRequest
//                                                                               notificitionQueryRequest,
//                                                                       HttpServletRequest request) {
//        long current = notificitionQueryRequest.getCurrent();
//        long size = notificitionQueryRequest.getPageSize();
//        // 限制爬虫
//        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
//        Page<Notification> notificitionPage = notificationService.page(new Page<>(current, size),
//                notificationService.getQueryWrapper(notificitionQueryRequest));
//        return ResultUtils.success(notificationService.getNotificationVOPage(notificitionPage, request));
//    }

//    @GetMapping("/get/vo")
//    public BaseResponse<NotificationVO> getNotificationVO(@RequestParam String domain) {
//        // 1. 校验参数
//        if (domain.isEmpty()) {
//            throw new BusinessException(ErrorCode.PARAMS_ERROR, "域名为空");
//        }
//        // 2. 查询通知
//        Notification notification = notificationService.getOne(new QueryWrapper<Notification>().
//                like("domain", "\"" + domain + "\""));
//        if (notification == null) {
//            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "暂时没有通知");
//        }
//        // 3. 校验通知是否开启状态
//        Integer status = notification.getStatus();
//        // 未开启状态
//        if (status == 0) {
//            return ResultUtils.success(null);
//        }
//        // 4. 校验是否在开始时间到结束时间内
//        Date startTime = notification.getStartTime();
//        Date endTime = notification.getEndTime();
//        if (startTime == null || endTime == null) {
//            throw new BusinessException(ErrorCode.PARAMS_ERROR, "开始时间或结束时间为空");
//        }
//        // 当前时间
//        Date date = DateUtil.date();
//        // 判断当前时间是否在开始时间到结束时间内
//        if (date.before(startTime) || date.after(endTime)) {
//            return ResultUtils.success(null);
//        }
//        log.info("getNotificationVO: {}", notification);
//        NotificationVO notificationVO = notificationService.getNotificationVO(notification);
//        return ResultUtils.success(notificationVO);
//    }


    // endregion

    /**
     * 编辑（用户）
     *
     * @param notificitionEditRequest
     * @param request
     * @return
     */
//    @PostMapping("/edit")
//    public BaseResponse<Boolean> editNotification(@RequestBody NotificationEditRequest
//                                                          notificitionEditRequest, HttpServletRequest request) {
//        if (notificitionEditRequest == null || notificitionEditRequest.getId() <= 0) {
//            throw new BusinessException(ErrorCode.PARAMS_ERROR);
//        }
//        Notification notificition = new Notification();
//        BeanUtils.copyProperties(notificitionEditRequest, notificition);
//        List<String> tags = notificitionEditRequest.getTags();
//        if (tags != null) {
//            notificition.setTags(GSON.toJson(tags));
//        }
//        // 参数校验
//        notificationService.validNotification(notificition, false);
//        User loginUser = userService.getLoginUser(request);
//        long id = notificitionEditRequest.getId();
//        // 判断是否存在
//        Notification oldNotification = notificationService.getById(id);
//        ThrowUtils.throwIf(oldNotification == null, ErrorCode.NOT_FOUND_ERROR);
//        // 仅本人或管理员可编辑
//        if (!oldNotification.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
//            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
//        }
//        boolean result = notificationService.updateById(notificition);
//        return ResultUtils.success(result);
//    }

}

package com.zxr.bsoj.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zxr.bsoj.model.entity.Notification;

import java.util.List;

/**
 * @author zxr0921
 * @description 针对表【notification(公告)】的数据库操作Service
 * @createDate 2025-01-21 20:40:25
 */
public interface NotificationService extends IService<Notification> {
    /**
     * 校验
     * @param notification
     * @param add
     */
    void validNotification(Notification notification,boolean add);
}
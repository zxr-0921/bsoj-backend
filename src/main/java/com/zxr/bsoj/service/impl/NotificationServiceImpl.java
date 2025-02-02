package com.zxr.bsoj.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.zxr.bsoj.common.ErrorCode;
import com.zxr.bsoj.exception.BusinessException;
import com.zxr.bsoj.exception.ThrowUtils;
import com.zxr.bsoj.mapper.NotificationMapper;
import com.zxr.bsoj.model.entity.Notification;
import com.zxr.bsoj.service.NotificationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author zxr0921
 * @description 针对表【notification(公告)】的数据库操作Service实现
 * @createDate 2025-01-21 20:40:25
 */
@Service
public class NotificationServiceImpl extends ServiceImpl<NotificationMapper, Notification>
        implements NotificationService {

    private final static Gson GSON = new Gson();

    @Override
    public void validNotification(Notification notification, boolean add) {
        if (notification == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // TODO: 2025/1/21  待完善
        String title = notification.getTitle();
        String content = notification.getContent();
        Date startTime = notification.getStartTime();
        Date endTime = notification.getEndTime();
        if (ObjectUtil.isNull(startTime) || ObjectUtil.isNull(endTime)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "开始时间或结束时间为空");
        }
        // 创建时，参数不能为空
        if (add) {
            ThrowUtils.throwIf(StringUtils.isAnyBlank(title, content), ErrorCode.PARAMS_ERROR);
        }
        // 有参数则校验
        if (StringUtils.isNotBlank(title) && title.length() > 80) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "标题过长");
        }
        if (StringUtils.isNotBlank(content) && content.length() > 2048) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "内容过长");
        }
    }

}





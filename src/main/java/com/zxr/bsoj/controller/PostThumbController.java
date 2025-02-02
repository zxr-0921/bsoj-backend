package com.zxr.bsoj.controller;

import com.zxr.bsoj.common.BaseResponse;
import com.zxr.bsoj.common.ErrorCode;
import com.zxr.bsoj.common.ResultUtils;
import com.zxr.bsoj.exception.BusinessException;
import com.zxr.bsoj.model.dto.postthumb.PostThumbAddRequest;
import com.zxr.bsoj.model.entity.User;
import com.zxr.bsoj.service.PostThumbService;
import com.zxr.bsoj.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 帖子点赞接口
 */
@RestController
@RequestMapping("/post_thumb")
@Slf4j
public class PostThumbController {

    @Resource
    private PostThumbService postThumbService;

    @Resource
    private UserService userService;

    /**
     * 点赞 / 取消点赞
     *
     * @param postThumbAddRequest
     * @param request
     * @return resultNum 本次点赞变化数
     */
    @PostMapping("/")
    public BaseResponse<Integer> doThumb(@RequestBody PostThumbAddRequest postThumbAddRequest,
                                         HttpServletRequest request) {
        if (postThumbAddRequest == null || postThumbAddRequest.getPostId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 登录才能点赞
        final User loginUser = userService.getLoginUser(request);
        long postId = postThumbAddRequest.getPostId();
        int result = postThumbService.doPostThumb(postId, loginUser);
        return ResultUtils.success(result);
    }
    /**
     * 查询当前用户点赞的帖子
     */
    @PostMapping("/queryMyThumb")
    public BaseResponse<List<Long>> queryMyThumb(HttpServletRequest request) {
        final User loginUser = userService.getLoginUser(request);
        return ResultUtils.success(postThumbService.queryMyThumb(loginUser.getId()));
    }

}

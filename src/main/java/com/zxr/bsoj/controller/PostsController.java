package com.zxr.bsoj.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.zxr.bsoj.annotation.AuthCheck;
import com.zxr.bsoj.common.BaseResponse;
import com.zxr.bsoj.common.ErrorCode;
import com.zxr.bsoj.common.ResultUtils;
import com.zxr.bsoj.constant.AnnouncementConstant;
import com.zxr.bsoj.constant.UserConstant;
import com.zxr.bsoj.exception.BusinessException;
import com.zxr.bsoj.exception.ThrowUtils;
import com.zxr.bsoj.model.dto.posts.AddPostRequest;
import com.zxr.bsoj.model.dto.posts.QueryPostRequest;
import com.zxr.bsoj.model.entity.PostAiAnswer;
import com.zxr.bsoj.model.entity.Posts;
import com.zxr.bsoj.model.entity.User;
import com.zxr.bsoj.model.vo.PostsVO;
import com.zxr.bsoj.service.PostAiAnswerService;
import com.zxr.bsoj.service.PostsService;
import com.zxr.bsoj.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostsController {

    private final static Gson GSON = new Gson();
    @Resource
    private PostsService postsService;

    @Resource
    private UserService userService;


    @Resource
    private PostAiAnswerService postAiAnswerService;


    // region 管理员

    // 分页查询所有帖子
    @PostMapping("/list")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<Posts>> listPosts(@RequestBody QueryPostRequest queryPostRequest) {
        // 获取分页信息
        long pageSize = queryPostRequest.getPageSize();
        long current = queryPostRequest.getCurrent();
        // 限制爬虫
        ThrowUtils.throwIf(pageSize > 20, ErrorCode.PARAMS_ERROR);
        Page<Posts> page = postsService.page(new Page<>(current, pageSize),
                postsService.getQueryWrapper(queryPostRequest));
        return ResultUtils.success(page);
    }

    /**
     * 删除
     *
     * @param postId 帖子id
     * @return
     */
    @PutMapping("/delete/{postId}")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deletePost(@PathVariable Long postId) {
        boolean result = postsService.removeById(postId);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(result);
    }


    // 审核帖子改变状态用户可以看见
    @PutMapping("/check/{postId}")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<String> checkPost(@PathVariable Long postId) {
        Posts post = postsService.getById(postId);
        ThrowUtils.throwIf(post == null, ErrorCode.OPERATION_ERROR);
        post.setStatus("published");
        boolean result = postsService.updateById(post);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success("审核成功");
    }

    /**
     * 根据id获取帖子详情（管理员）
     */
    @GetMapping("/get/{id}")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Posts> getPostById(@PathVariable Long id) {
        Posts post = postsService.getById(id);
        ThrowUtils.throwIf(post == null, ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success(post);
    }

    // endregion


    // region 学生或者老师

    /**
     * 创建普通帖子
     */
    @PostMapping("/create")
    public BaseResponse<Long> creatPost(@RequestBody AddPostRequest addPostRequest, HttpServletRequest request) {
        // 参数校验
        if (addPostRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 用户是否登录
        User loginUser = userService.getLoginUser(request);
        // 如果未登录
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        Posts post = new Posts();
        BeanUtils.copyProperties(addPostRequest, post);
        List<String> tags = addPostRequest.getTags();
        if (tags != null) {
            post.setTags(GSON.toJson(tags));
        }
        // 校验
        postsService.validPost(post, true);
        post.setUserId(loginUser.getId());
        boolean result = postsService.save(post);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        long newPostId = post.getId();
        return ResultUtils.success(newPostId);
    }

    /**
     * 创建题解帖子
     *
     * @param addPostRequest
     * @param request
     * @return
     */
    @PostMapping("/create/solution")
    public BaseResponse<Long> creatSolutionPost(@RequestBody AddPostRequest addPostRequest, HttpServletRequest request) {
        // 参数校验
        if (addPostRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Posts post = new Posts();
        BeanUtils.copyProperties(addPostRequest, post);
        List<String> tags = addPostRequest.getTags();
        if (tags != null) {
            post.setTags(GSON.toJson(tags));
        }
        // 校验
        postsService.validPost(post, true);
        User loginUser = userService.getLoginUser(request);
        post.setUserId(loginUser.getId());
        post.setType("solution_post");
        boolean result = postsService.save(post);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        long newPostId = post.getId();
        return ResultUtils.success(newPostId);
    }

    /**
     * 获取题解列表
     *
     * @param queryPostRequest
     * @param request
     * @return
     */
    @PostMapping("/solution/list")
    public BaseResponse<Page<PostsVO>> listSolutionPosts(@RequestBody QueryPostRequest queryPostRequest, HttpServletRequest request) {
        // 获取分页信息
        long pageSize = queryPostRequest.getPageSize();
        long current = queryPostRequest.getCurrent();
        // 限制爬虫
        ThrowUtils.throwIf(pageSize > 20, ErrorCode.PARAMS_ERROR);
        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", AnnouncementConstant.STATUS_PUBLISHED);
        queryWrapper.eq("type", "solution_post");
        queryWrapper.eq("problemId", queryPostRequest.getProblemId());
        // 判断title是否为空
        String title = queryPostRequest.getTitle();
        queryWrapper.eq(StringUtils.isNotBlank(title), "title", title);
        Page<Posts> page = postsService.page(new Page<>(current, pageSize), queryWrapper);
        return ResultUtils.success(postsService.getPostVOPage(page, request));
    }


    //可以查看审核通过的帖子
    @PostMapping("/user/list/")
    public BaseResponse<Page<PostsVO>> listPostsByUser(@RequestBody QueryPostRequest queryPostRequest, HttpServletRequest request) {
        // 获取分页信息
        long pageSize = queryPostRequest.getPageSize();
        long current = queryPostRequest.getCurrent();
        // 限制爬虫
        ThrowUtils.throwIf(pageSize > 20, ErrorCode.PARAMS_ERROR);
        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", AnnouncementConstant.STATUS_PUBLISHED);
        queryWrapper.eq("type", "common_post");
        Page<Posts> page = postsService.page(new Page<>(current, pageSize), queryWrapper);
        return ResultUtils.success(postsService.getPostVOPage(page, request));
    }

    /**
     * 查看帖子详情（用户）
     */
    @GetMapping("/get/vo")
    public BaseResponse<PostsVO> getPostVOById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Posts post = postsService.getById(id);
        if (post == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return ResultUtils.success(postsService.getPostVO(post, request));
    }

    /**
     * 根据id获取ai回答的内容
     */
    @GetMapping("/get/ai/answer/{id}")
    public BaseResponse<String> getAiAnswer(@PathVariable Long id) {
        Posts byId = postsService.getById(id);
        if (byId == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("postsId", id);
        PostAiAnswer postAiAnswer = postAiAnswerService.getOne(queryWrapper);
        if (postAiAnswer == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return ResultUtils.success(postAiAnswer.getAnswer());
    }
    // endregion


}

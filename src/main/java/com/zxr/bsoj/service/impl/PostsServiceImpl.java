package com.zxr.bsoj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zxr.bsoj.common.ErrorCode;
import com.zxr.bsoj.constant.CommonConstant;
import com.zxr.bsoj.exception.BusinessException;
import com.zxr.bsoj.exception.ThrowUtils;
import com.zxr.bsoj.mapper.PostsMapper;
import com.zxr.bsoj.model.dto.posts.QueryPostRequest;
import com.zxr.bsoj.model.entity.PostFavour;
import com.zxr.bsoj.model.entity.PostThumb;
import com.zxr.bsoj.model.entity.Posts;
import com.zxr.bsoj.model.entity.User;
import com.zxr.bsoj.model.vo.PostVO;
import com.zxr.bsoj.model.vo.PostsVO;
import com.zxr.bsoj.model.vo.UserVO;
import com.zxr.bsoj.service.PostsService;
import com.zxr.bsoj.service.UserService;
import com.zxr.bsoj.utils.SqlUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author zxr0921
 * @description 针对表【posts(帖子表，用于存储用户发布的帖子信息)】的数据库操作Service实现
 * @createDate 2025-02-12 10:19:59
 */
@Service
public class PostsServiceImpl extends ServiceImpl<PostsMapper, Posts>
        implements PostsService {

    @Resource
    private UserService userService;

    //校验常见帖子
    @Override
    public void validPost(Posts post, boolean add) {
        if (post == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String title = post.getTitle();
        String content = post.getContent();
        String tags = post.getTags();
        // 创建时，参数不能为空
        if (add) {
            ThrowUtils.throwIf(StringUtils.isAnyBlank(title, content, tags), ErrorCode.PARAMS_ERROR);
        }
        // 有参数则校验
        if (StringUtils.isNotBlank(title) && title.length() > 80) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "标题过长");
        }
        if (StringUtils.isNotBlank(content) && content.length() > 8192) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "内容过长");
        }
    }

    /**
     * 获取查询条件
     *
     * @param queryPostRequest
     * @return
     */
    @Override
    public QueryWrapper<Posts> getQueryWrapper(QueryPostRequest queryPostRequest) {
        Long id = queryPostRequest.getId();
        String title = queryPostRequest.getTitle();
        String status = queryPostRequest.getStatus();
        Long userId = queryPostRequest.getUserId();
        String content = queryPostRequest.getContent();
        Integer problemId = queryPostRequest.getProblemId();
        String language = queryPostRequest.getLanguage();
        Date startTime = queryPostRequest.getStartTime();
        Date endTime = queryPostRequest.getEndTime();
        String type = queryPostRequest.getType();
        List<String> tags = queryPostRequest.getTags();
        String sortField = queryPostRequest.getSortField();
        String sortOrder = queryPostRequest.getOrder();

        QueryWrapper<Posts> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(id != null, "id", id);
        queryWrapper.eq(userId != null, "userId", userId);
        queryWrapper.eq(problemId != null, "problemId", problemId);
        queryWrapper.eq(StringUtils.isNotBlank(language), "language", language);
        queryWrapper.eq(StringUtils.isNotBlank(status), "status", status);
        queryWrapper.eq(StringUtils.isNotBlank(type), "type", type);
        queryWrapper.like(StringUtils.isNotBlank(title), "title", title);
        queryWrapper.like(StringUtils.isNotBlank(content), "content", content);
        queryWrapper.between(startTime != null && endTime != null, "publishTime", startTime, endTime);

        queryWrapper.eq("isDelete", false);
        if (CollectionUtils.isNotEmpty(tags)) {
            for (String tag : tags) {
                queryWrapper.like("tags", "\"" + tag + "\"");
            }
        }
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    @Override
    public Page<PostsVO> getPostVOPage(Page<Posts> page, HttpServletRequest request) {
        //获取数据
        List<Posts> postList = page.getRecords();
        Page<PostsVO> postVOPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        if (CollectionUtils.isEmpty(postList)) {
            return postVOPage;
        }
        // 1. 收集用户id
        Set<Long> userIdSet = postList.stream().map(Posts::getUserId).collect(Collectors.toSet());
        // 2. 查询用户信息
        Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));

        // 填充信息
        List<PostsVO> postVOList = postList.stream().map(post -> {
            PostsVO postsVO = PostsVO.objToVo(post);
            Long userId = post.getUserId();
            User user = null;
            // 在Map用户信息中找到对应的用户
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }
            postsVO.setUserVO(userService.getUserVO(user));
            return postsVO;
        }).collect(Collectors.toList());
        postVOPage.setRecords(postVOList);
        return postVOPage;
    }

    @Override
    public PostsVO getPostVO(Posts post, HttpServletRequest request) {
        PostsVO postVO = PostsVO.objToVo(post);
        long postId = post.getId();
        // 1. 关联查询用户信息
        Long userId = post.getUserId();
        User user = null;
        if (userId != null && userId > 0) {
            user = userService.getById(userId);
        }
        UserVO userVO = userService.getUserVO(user);
        postVO.setUserVO(userVO);
        return postVO;
    }
}





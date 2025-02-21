package com.zxr.bsoj.model.vo;
import com.google.gson.reflect.TypeToken;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.gson.Gson;
import com.zxr.bsoj.model.entity.Post;
import com.zxr.bsoj.model.entity.Posts;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
@Data
public class PostsVO implements Serializable {
    private final static Gson GSON = new Gson();
    /**
     * 帖子ID（主键），自增
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 发布者ID（外键），不能为空
     */
    private Long userId;

    /**
     * 帖子标题，不能为空
     */
    private String title;

    /**
     * 帖子内容，不能为空
     */
    private String content;

    /**
     * 题目ID（外键，可为空）
     */
    private Integer problemId;

    /**
     * 编程语言（题解专用），可为空
     */
    private String language;

    /**
     * 代码内容（题解专用），可为空
     */
    private String code;

    /**
     * 题解版本（题解专用），可为空
     */
    private String version;


    /**
     * 发布时间，不能为空，默认值为当前时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date publishTime;


    /**
     * 浏览量，可为空，默认值为 0
     */
    private Integer views;

    /**
     * 点赞量，可为空，默认值为 0
     */
    private Integer likes;

    /**
     * 评论数量，可为空，默认值为 0
     */
    private Integer commentsCount;

    /**
     * 帖子类型，不能为空，默认值为 solution_post
     */
    private String type;

    /**
     * 标签（JSON格式），可为空
     */
    private List<String> tags;

    /**
     * 逻辑删除标志(0:未删除,1:已删除)
     */
    /**
     * 创建人信息
     */
    private UserVO userVO;


    /**
     * 对象转包装类
     */
    public static PostsVO objToVo(Posts post) {
        if (post == null) {
            return null;
        }
        PostsVO postsVO = new PostsVO();
        BeanUtils.copyProperties(post, postsVO);
        postsVO.setTags(GSON.fromJson(post.getTags(), new TypeToken<List<String>>() {
        }.getType()));
        return postsVO;
    }

    private static final long serialVersionUID = 1L;
}

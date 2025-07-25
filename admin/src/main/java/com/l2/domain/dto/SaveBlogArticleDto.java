package com.l2.domain.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class SaveBlogArticleDto implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 文章标题
     */
    private String title;

    /**
     * Markdown内容
     */
    private String content;

    /**
     * 作者名称（冗余存储）
     */
    private String userName;

    /**
     * 是否发布（0=私密，1=公开）
     */
    private Integer isPublished;

}

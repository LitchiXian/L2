package com.l2.web;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.l2.domain.BlogArticle;
import com.l2.domain.dto.IdDto;
import com.l2.domain.dto.SaveBlogArticleDto;
import com.l2.exception.ServiceException;
import com.l2.service.BlogArticleService;
import com.l2.util.AjaxResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/blog/article")
@RequiredArgsConstructor
public class BlogArticleController {

    private final BlogArticleService blogArticleService;

    @PostMapping("/save")
    public AjaxResult save(@RequestBody SaveBlogArticleDto dto) {
        blogArticleService.save(dto);
        return AjaxResult.success();
    }

    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody IdDto idDto) {
        blogArticleService.removeById(idDto.getId());
        return AjaxResult.success();
    }

    @PostMapping("/update")
    public AjaxResult update(@RequestBody BlogArticle blogArticle) {
        blogArticleService.updateById(blogArticle);
        return AjaxResult.success();
    }

    @GetMapping("/list")
    public AjaxResult list() {
        System.out.println("是否登录：" + StpUtil.isLogin());
        if (StpUtil.isLogin()) {
            System.out.println("当前登录用户：" + StpUtil.getLoginId());
            System.out.println("当前登录用户：" + StpUtil.getTokenInfo());
        }
        return AjaxResult.success(blogArticleService.list(
                        new LambdaQueryWrapper<BlogArticle>()
                                .eq(BlogArticle::getIsDeleted, 0)
                                .eq(BlogArticle::getIsPublished, 1)
                                .orderByDesc(BlogArticle::getUpdateTime)
                )
        );
    }

    @GetMapping("/get")
    public AjaxResult get(IdDto idDto) {
        BlogArticle article = blogArticleService.getById(idDto.getId());
        return AjaxResult.success(article);
    }

    @GetMapping("/list2")
    public AjaxResult list2() {
//        int i = 1/0;
        throw new ServiceException("10086", "自定义异常");
//        return AjaxResult.success(bBlogArticleService.listBlogArticle());
    }
}

package com.barablah.web.response;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

@Data
public class GetTextbooksResponse {

    /**
     * 选择题
     */
    private List<TextbookDto> choices = Lists.newArrayList();

    /**
     * 填空题
     */
    private List<TextbookDto> fillins = Lists.newArrayList();

    /**
     * 听写题
     */
    private List<TextbookDto> listens = Lists.newArrayList();

    /**
     * 看图造句
     */
    private List<TextbookDto> sentences = Lists.newArrayList();

    /**
     * 图词对应
     */
    private List<TextbookDto> connects = Lists.newArrayList();

    /**
     * 认词拼读
     */
    private List<TextbookDto> words = Lists.newArrayList();

    /**
     * 认图读词
     */
    private List<TextbookDto> pictures = Lists.newArrayList();

    /**
     * 阅读文章
     */
    private List<TextbookDto> article = Lists.newArrayList();

    private long classid;

    private long catid;

}

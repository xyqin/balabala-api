package com.balabala;

import com.google.common.collect.Lists;
import lombok.Getter;

import java.util.List;

/**
 * 分页结果
 * Created by xyqin on 16/5/23.
 */

public class Page<T> {

    /**
     * 页码
     */
    @Getter
    private int number;

    /**
     * 页面大小
     */
    @Getter
    private int size;

    /**
     * 总记录数
     */
    @Getter
    private long totalElements;

    /**
     * 总页码数
     */
    @Getter
    private int totalPages;

    /**
     * 当前页码内容
     */
    @Getter
    private List<T> content = Lists.newArrayList();

    public Page(List<T> content, int number, int size, long totalElements) {
        this.content.addAll(content);
        this.number = number;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = size <= 0 ? 1 : (int) ((totalElements + size - 1) / size);
    }

    public int getNumberOfElements() {
        return this.content.size();
    }

    public boolean hasContent() {
        return !this.content.isEmpty();
    }

}

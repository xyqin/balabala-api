package com.balabala.domain;

import java.util.Date;

/**
 * 实体对象
 * Created by xyqin on 16/6/2.
 */
public interface Entity<I> {

    I getId();

    Date getCreatedAt();

    Date getUpdatedAt();

    Long getCreator();

    Long getLastModifier();

    Boolean getDeleted();

}

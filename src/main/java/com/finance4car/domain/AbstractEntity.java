package com.finance4car.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 抽象实体对象
 * Created by xyqin on 16/6/2.
 */
@Data
public abstract class AbstractEntity<I> implements Entity<I>, Serializable {

    private I id;

    private Date createdAt;

    private Date updatedAt;

    private Long creator;

    private Long lastModifier;

    private Boolean deleted;

}

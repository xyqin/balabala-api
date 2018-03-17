package com.barablah.domain;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;
import java.util.Date;

/**
 *
 * RudderFramework框架自动生成，不允许修改！
 * 表 barablah_teacher_major
 * @generated do_not_delete_during_merge 2018年3月18日 01:59:09
 */
public class BarablahTeacherMajor extends AbstractEntity<Long> {
    /**
     *
     * {"viewconfig":{"optype":"1","formid":"1"},"name":"主键","fieldType":"Id","visible":true,"queryType":0,"displayOrder":0,"length":0,"types":[],"valid":false}
     * @generated 2018年3月18日 01:59:09
     */
    private Long id;

    /**
     *
     * {"viewconfig":{"optype":"3","formid":"1"},"name":"专业名称","checkName":true,"fieldType":"String","visible":true,"queryType":2,"displayOrder":0,"length":0,"types":[],"valid":true}
     * @generated 2018年3月18日 01:59:09
     */
    @NotEmpty(message="专业名称不能为空")
    @Size(max=0,message = "专业名称长度无效")

    private String majorName;

    /**
     *
     * {"viewconfig":{"optype":"3","formid":"1"},"name":"排序号","fieldType":"Number","visible":true,"queryType":0,"displayOrder":0,"length":0,"types":[],"valid":false,"orderkey":true,"remark":"大数优先"}
     * @generated 2018年3月18日 01:59:09
     */
    private Integer position;

    /**
     *
     * {"name":"创建时间","fieldType":"Date","visible":false,"queryType":0,"displayOrder":0,"length":0,"types":[],"valid":false}
     * @generated 2018年3月18日 01:59:09
     */
    private Date createdAt;

    /**
     *
     * {"name":"修改时间","fieldType":"Date","visible":false,"queryType":0,"displayOrder":0,"length":0,"types":[],"valid":false}
     * @generated 2018年3月18日 01:59:09
     */
    private Date updatedAt;

    /**
     *
     * {"name":"创建者","fieldType":"Number","visible":false,"queryType":0,"displayOrder":0,"length":0,"types":[],"valid":false}
     * @generated 2018年3月18日 01:59:09
     */
    private Long creator;

    /**
     *
     * {"name":"修改人","fieldType":"Number","visible":false,"queryType":0,"displayOrder":0,"length":0,"types":[],"valid":false}
     * @generated 2018年3月18日 01:59:09
     */
    private Long lastModifier;

    /**
     *
     * {"name":"删除标志","fieldType":"Bool","visible":false,"queryType":0,"displayOrder":0,"length":0,"types":[],"valid":false}
     * @generated 2018年3月18日 01:59:09
     */
    private Boolean deleted;

    /**
     * 返回 id 主键
     * @generated 2018年3月18日 01:59:09
     */
    public Long getId() {
        
        return id;
    }

    /**
     * 设置主键
     *
     * @param id
     * @generated 2018年3月18日 01:59:09
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 返回 major_name 专业名称
     * @generated 2018年3月18日 01:59:09
     */
    public String getMajorName() {
        
        return majorName;
    }

    /**
     * 设置专业名称
     *
     * @param majorName
     * @generated 2018年3月18日 01:59:09
     */
    public void setMajorName(String majorName) {
        this.majorName = majorName == null ? null : majorName.trim();
    }

    /**
     * 返回 position 排序号
     * @generated 2018年3月18日 01:59:09
     */
    public Integer getPosition() {
        
        return position;
    }

    /**
     * 设置排序号
     *
     * @param position
     * @generated 2018年3月18日 01:59:09
     */
    public void setPosition(Integer position) {
        this.position = position;
    }

    /**
     * 返回 created_at 创建时间
     * @generated 2018年3月18日 01:59:09
     */
    public Date getCreatedAt() {
        
        return createdAt;
    }

    /**
     * 设置创建时间
     *
     * @param createdAt
     * @generated 2018年3月18日 01:59:09
     */
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * 返回 updated_at 修改时间
     * @generated 2018年3月18日 01:59:09
     */
    public Date getUpdatedAt() {
        
        return updatedAt;
    }

    /**
     * 设置修改时间
     *
     * @param updatedAt
     * @generated 2018年3月18日 01:59:09
     */
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * 返回 creator 创建者
     * @generated 2018年3月18日 01:59:09
     */
    public Long getCreator() {
        
        return creator;
    }

    /**
     * 设置创建者
     *
     * @param creator
     * @generated 2018年3月18日 01:59:09
     */
    public void setCreator(Long creator) {
        this.creator = creator;
    }

    /**
     * 返回 last_modifier 修改人
     * @generated 2018年3月18日 01:59:09
     */
    public Long getLastModifier() {
        
        return lastModifier;
    }

    /**
     * 设置修改人
     *
     * @param lastModifier
     * @generated 2018年3月18日 01:59:09
     */
    public void setLastModifier(Long lastModifier) {
        this.lastModifier = lastModifier;
    }

    /**
     * 返回 deleted 删除标志
     * @generated 2018年3月18日 01:59:09
     */
    public Boolean getDeleted() {
        
        return deleted;
    }

    /**
     * 设置删除标志
     *
     * @param deleted
     * @generated 2018年3月18日 01:59:09
     */
    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}
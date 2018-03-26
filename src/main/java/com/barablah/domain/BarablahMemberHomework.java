package com.barablah.domain;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;
import java.util.Date;

/**
 *
 * RudderFramework框架自动生成，不允许修改！
 * 表 barablah_member_homework
 * @generated do_not_delete_during_merge 2018年3月25日 10:55:18
 */
public class BarablahMemberHomework extends AbstractEntity<Long> {
    /**
     *
     * {"viewconfig":{"optype":"1","formid":"1"},"name":"主键","fieldType":"Id","visible":true,"queryType":0,"displayOrder":0,"length":0,"types":[],"valid":false}
     * @generated 2018年3月25日 10:55:18
     */
    private Long id;

    /**
     *
     * {"viewconfig":{"optype":"3","formid":"1"},"name":"会员ID","fieldType":"Object","visible":true,"queryType":0,"displayOrder":0,"length":0,"ref":{"module":"BarablahMember","field":"nickname","type":"SingleList"},"valid":true}
     * @generated 2018年3月25日 10:55:18
     */
    private Long memberId;

    /**
     *
     * {"viewconfig":{"optype":"3","formid":"1"},"name":"教师ID","fieldType":"Object","visible":true,"queryType":0,"displayOrder":0,"length":0,"ref":{"module":"BarablahTeacher","field":"fullName","type":"SingleList"},"valid":true}
     * @generated 2018年3月25日 10:55:18
     */
    private Long teacherId;

    /**
     *
     * {"viewconfig":{"optype":"3","formid":"1"},"name":"作业名称","fieldType":"String","visible":true,"checkName":false,"queryType":2,"displayOrder”:0,”length":16,"types":[],"valid":true}
     * @generated 2018年3月25日 10:55:18
     */
    @NotEmpty(message="作业名称不能为空")
    @Size(max=0,message = "作业名称长度无效")

    private String homeworkName;

    /**
     *
     * {"name":"截止时间","fieldType":"Date","visible":false,"queryType":0,"displayOrder":0,"length":0,"types":[],"valid":false}
     * @generated 2018年3月25日 10:55:18
     */
    private Date closingAt;

    /**
     *
     * {"viewconfig":{"optype":"3","formid":"1"},"name":"状态","fieldType":"Enum","visible":true,"queryType":2,"displayOrder":0,"length":0,"types":[{"label":"未完成","value":"PENDING"},{"label":"已完成","value":"FINISHED"}],"valid":true}
     * @generated 2018年3月25日 10:55:18
     */
    @NotEmpty(message="状态不能为空")
    @Size(max=0,message = "状态长度无效")

    private String status;

    /**
     *
     * {"name":"创建时间","fieldType":"Date","visible":false,"queryType":0,"displayOrder":0,"length":0,"types":[],"valid":false}
     * @generated 2018年3月25日 10:55:18
     */
    private Date createdAt;

    /**
     *
     * {"name":"修改时间","fieldType":"Date","visible":false,"queryType":0,"displayOrder":0,"length":0,"types":[],"valid":false}
     * @generated 2018年3月25日 10:55:18
     */
    private Date updatedAt;

    /**
     *
     * {"name":"创建者","fieldType":"Number","visible":false,"queryType":0,"displayOrder":0,"length":0,"types":[],"valid":false}
     * @generated 2018年3月25日 10:55:18
     */
    private Long creator;

    /**
     *
     * {"name":"修改人","fieldType":"Number","visible":false,"queryType":0,"displayOrder":0,"length":0,"types":[],"valid":false}
     * @generated 2018年3月25日 10:55:18
     */
    private Long lastModifier;

    /**
     *
     * {"name":"删除标志","fieldType":"Bool","visible":false,"queryType":0,"displayOrder":0,"length":0,"types":[],"valid":false}
     * @generated 2018年3月25日 10:55:18
     */
    private Boolean deleted;

    /**
     *
     * {"viewconfig":{"optype":"3","formid":"1"},"name":"开班ID","fieldType":"Object","visible":true,"queryType":2,"displayOrder":0,"length":0,"ref":{"module":"BarablahClass","field":"className","type":"SingleList"},"valid":true}
     * @generated 2018年3月25日 10:55:18
     */
    private Long classId;

    private Integer scoreIcon;

    private Integer finishedNums;

    public String content;

    /**
     * 返回 id 主键
     * @generated 2018年3月25日 10:55:18
     */
    public Long getId() {
        
        return id;
    }

    /**
     * 设置主键
     *
     * @param id
     * @generated 2018年3月25日 10:55:18
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 返回 member_id 会员ID
     * @generated 2018年3月25日 10:55:18
     */
    public Long getMemberId() {
        
        return memberId;
    }

    /**
     * 设置会员ID
     *
     * @param memberId
     * @generated 2018年3月25日 10:55:18
     */
    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    /**
     * 返回 teacher_id 教师ID
     * @generated 2018年3月25日 10:55:18
     */
    public Long getTeacherId() {
        
        return teacherId;
    }

    /**
     * 设置教师ID
     *
     * @param teacherId
     * @generated 2018年3月25日 10:55:18
     */
    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }

    /**
     * 返回 homework_name 作业名称
     * @generated 2018年3月25日 10:55:18
     */
    public String getHomeworkName() {
        
        return homeworkName;
    }

    /**
     * 设置作业名称
     *
     * @param homeworkName
     * @generated 2018年3月25日 10:55:18
     */
    public void setHomeworkName(String homeworkName) {
        this.homeworkName = homeworkName == null ? null : homeworkName.trim();
    }

    /**
     * 返回 closing_at 截止时间
     * @generated 2018年3月25日 10:55:18
     */
    public Date getClosingAt() {
        
        return closingAt;
    }

    /**
     * 设置截止时间
     *
     * @param closingAt
     * @generated 2018年3月25日 10:55:18
     */
    public void setClosingAt(Date closingAt) {
        this.closingAt = closingAt;
    }

    /**
     * 返回 status 状态
     * @generated 2018年3月25日 10:55:18
     */
    public String getStatus() {
        
        return status;
    }

    /**
     * 设置状态
     *
     * @param status
     * @generated 2018年3月25日 10:55:18
     */
    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    /**
     * 返回 created_at 创建时间
     * @generated 2018年3月25日 10:55:18
     */
    public Date getCreatedAt() {
        
        return createdAt;
    }

    /**
     * 设置创建时间
     *
     * @param createdAt
     * @generated 2018年3月25日 10:55:18
     */
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * 返回 updated_at 修改时间
     * @generated 2018年3月25日 10:55:18
     */
    public Date getUpdatedAt() {
        
        return updatedAt;
    }

    /**
     * 设置修改时间
     *
     * @param updatedAt
     * @generated 2018年3月25日 10:55:18
     */
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * 返回 creator 创建者
     * @generated 2018年3月25日 10:55:18
     */
    public Long getCreator() {
        
        return creator;
    }

    /**
     * 设置创建者
     *
     * @param creator
     * @generated 2018年3月25日 10:55:18
     */
    public void setCreator(Long creator) {
        this.creator = creator;
    }

    /**
     * 返回 last_modifier 修改人
     * @generated 2018年3月25日 10:55:18
     */
    public Long getLastModifier() {
        
        return lastModifier;
    }

    /**
     * 设置修改人
     *
     * @param lastModifier
     * @generated 2018年3月25日 10:55:18
     */
    public void setLastModifier(Long lastModifier) {
        this.lastModifier = lastModifier;
    }

    /**
     * 返回 deleted 删除标志
     * @generated 2018年3月25日 10:55:18
     */
    public Boolean getDeleted() {
        
        return deleted;
    }

    /**
     * 设置删除标志
     *
     * @param deleted
     * @generated 2018年3月25日 10:55:18
     */
    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    /**
     * 返回 class_id 开班ID
     * @generated 2018年3月25日 10:55:18
     */
    public Long getClassId() {
        
        return classId;
    }

    public Integer getScoreIcon() {
        return scoreIcon;
    }

    public void setScoreIcon(Integer scoreIcon) {
        this.scoreIcon = scoreIcon;
    }

    /**
     * 设置开班ID
     *
     * @param classId
     * @generated 2018年3月25日 10:55:18
     */
    public void setClassId(Long classId) {
        this.classId = classId;
    }

    public Integer getFinishedNums() {
        return finishedNums;
    }

    public void setFinishedNums(Integer finishedNums) {
        this.finishedNums = finishedNums;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
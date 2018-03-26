package com.barablah.domain;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;
import java.util.Date;

/**
 *
 * RudderFramework框架自动生成，不允许修改！
 * 表 barablah_class
 * @generated do_not_delete_during_merge 2018年3月25日 10:55:18
 */
public class BarablahClass extends AbstractEntity {
    /**
     *
     * {"viewconfig":{"optype":"1","formid":"1"},"name":"ID","fieldType":"Id","visible":true,"queryType":0,"displayOrder":0,"length":0,"types":[],"valid":false}
     * @generated 2018年3月25日 10:55:18
     */
    private Long id;

    /**
     *
     * {"viewconfig":{"optype":"3","formid":"1"},"name":"班级类别","fieldType":"Object","visible":true,"queryType":2,"displayOrder":0,"length":0,"ref":{"module":"BarablahClassCategory","field":"categoryName","type":"SingleList"},"valid":true}
     * @generated 2018年3月25日 10:55:18
     */
    private Long categoryId;

    /**
     *
     * {"viewconfig":{"optype":"3","formid":"1"},"name":"所在校区","fieldType":"Object","visible":true,"queryType":2,"displayOrder":0,"length":0,"ref":{"module":"BarablahCampus","field":"campusName","type":"SingleList"},"valid":true}
     * @generated 2018年3月25日 10:55:18
     */
    private Long campusId;

    /**
     *
     * {"viewconfig":{"optype":"3","formid":"1"},"name":"课程类别","fieldType":"Object","visible":true,"queryType":0,"displayOrder":0,"length":0,"ref":{"module":"BarablahCourseCategory","field":"categoryName","type":"SingleList"},"valid":true}
     * @generated 2018年3月25日 10:55:18
     */
    private Long courseCatId;

    /**
     *
     * {"viewconfig":{"optype":"3","formid":"1"},"name":"所属课程","fieldType":"Object","visible":true,"queryType":2,"displayOrder":0,"length":0,"ref":{"module":"BarablahCourse","field":"courseName","type":"SingleList"},"valid":true}
     * @generated 2018年3月25日 10:55:18
     */
    private Long courseId;

    /**
     *
     * {"viewconfig":{"optype":"3","formid":"1"},"name":"班级名称","fieldType":"String","visible":true,"checkName":false,"queryType":2,"displayOrder”:0,”length":16,"types":[],"valid":true}
     * @generated 2018年3月25日 10:55:18
     */
    @NotEmpty(message="班级名称不能为空")
    @Size(max=0,message = "班级名称长度无效")

    private String className;

    /**
     *
     * {"viewconfig":{"optype":"3","formid":"1"},"name":"线上教师","fieldType":"Object","visible":true,"queryType":2,"displayOrder":0,"length":0,"ref":{"module":"BarablahTeacher","field":"fullName","type":"SingleList"},"valid":true}
     * @generated 2018年3月25日 10:55:18
     */
    private Long teacherId;

    /**
     *
     * {"viewconfig":{"optype":"3","formid":"1"},"name":"线下教师","fieldType":"Object","visible":true,"queryType":0,"displayOrder":0,"length":0,"ref":{"module":"BarablahTeacher","field":"fullName","type":"SingleList"},"valid":false}
     * @generated 2018年3月25日 10:55:18
     */
    private Long englishTeacherId;

    /**
     *
     * {"viewconfig":{"optype":"3","formid":"1"},"name":"班长","fieldType":"String","visible":true,"checkName":false,"queryType":0,"displayOrder”:0,”length":16,"types":[],"valid":false}
     * @generated 2018年3月25日 10:55:18
     */
    private String monitor;

    /**
     *
     * {"viewconfig":{"optype":"3","formid":"1"},"name":"班长电话","fieldType":"String","visible":true,"checkName":false,"queryType":0,"displayOrder”:0,”length":16,"types":[],"valid":false}
     * @generated 2018年3月25日 10:55:18
     */
    private String monitorPhoneNumber;

    /**
     *
     * {"viewconfig":{"optype":"3","formid":"1"},"name":"开班状态","fieldType":"Enum","visible":true,"queryType":2,"displayOrder":0,"length":0,"types":[{"label":"审核中","value":"IN_REVIEW"},{"label":"审核被拒","value":"REJECTED"},{"label":"待开课","value":"WAITTING"},{"label":"已开课","value":"ONGOING"},{"label":"已结束","value":"FINISHED"}],"valid":false}
     * @generated 2018年3月25日 10:55:18
     */
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
     * 返回 id ID
     * @generated 2018年3月25日 10:55:18
     */
    public Long getId() {
        
        return id;
    }

    /**
     * 设置ID
     *
     * @param id
     * @generated 2018年3月25日 10:55:18
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 返回 category_id 班级类别
     * @generated 2018年3月25日 10:55:18
     */
    public Long getCategoryId() {
        
        return categoryId;
    }

    /**
     * 设置班级类别
     *
     * @param categoryId
     * @generated 2018年3月25日 10:55:18
     */
    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * 返回 campus_id 所在校区
     * @generated 2018年3月25日 10:55:18
     */
    public Long getCampusId() {
        
        return campusId;
    }

    /**
     * 设置所在校区
     *
     * @param campusId
     * @generated 2018年3月25日 10:55:18
     */
    public void setCampusId(Long campusId) {
        this.campusId = campusId;
    }

    /**
     * 返回 course_cat_id 课程类别
     * @generated 2018年3月25日 10:55:18
     */
    public Long getCourseCatId() {
        
        return courseCatId;
    }

    /**
     * 设置课程类别
     *
     * @param courseCatId
     * @generated 2018年3月25日 10:55:18
     */
    public void setCourseCatId(Long courseCatId) {
        this.courseCatId = courseCatId;
    }

    /**
     * 返回 course_id 所属课程
     * @generated 2018年3月25日 10:55:18
     */
    public Long getCourseId() {
        
        return courseId;
    }

    /**
     * 设置所属课程
     *
     * @param courseId
     * @generated 2018年3月25日 10:55:18
     */
    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    /**
     * 返回 class_name 班级名称
     * @generated 2018年3月25日 10:55:18
     */
    public String getClassName() {
        
        return className;
    }

    /**
     * 设置班级名称
     *
     * @param className
     * @generated 2018年3月25日 10:55:18
     */
    public void setClassName(String className) {
        this.className = className == null ? null : className.trim();
    }

    /**
     * 返回 teacher_id 线上教师
     * @generated 2018年3月25日 10:55:18
     */
    public Long getTeacherId() {
        
        return teacherId;
    }

    /**
     * 设置线上教师
     *
     * @param teacherId
     * @generated 2018年3月25日 10:55:18
     */
    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }

    /**
     * 返回 english_teacher_id 线下教师
     * @generated 2018年3月25日 10:55:18
     */
    public Long getEnglishTeacherId() {
        
        return englishTeacherId;
    }

    /**
     * 设置线下教师
     *
     * @param englishTeacherId
     * @generated 2018年3月25日 10:55:18
     */
    public void setEnglishTeacherId(Long englishTeacherId) {
        this.englishTeacherId = englishTeacherId;
    }

    /**
     * 返回 monitor 班长
     * @generated 2018年3月25日 10:55:18
     */
    public String getMonitor() {
        
        return monitor;
    }

    /**
     * 设置班长
     *
     * @param monitor
     * @generated 2018年3月25日 10:55:18
     */
    public void setMonitor(String monitor) {
        this.monitor = monitor == null ? null : monitor.trim();
    }

    /**
     * 返回 monitor_phone_number 班长电话
     * @generated 2018年3月25日 10:55:18
     */
    public String getMonitorPhoneNumber() {
        
        return monitorPhoneNumber;
    }

    /**
     * 设置班长电话
     *
     * @param monitorPhoneNumber
     * @generated 2018年3月25日 10:55:18
     */
    public void setMonitorPhoneNumber(String monitorPhoneNumber) {
        this.monitorPhoneNumber = monitorPhoneNumber == null ? null : monitorPhoneNumber.trim();
    }

    /**
     * 返回 status 开班状态
     * @generated 2018年3月25日 10:55:18
     */
    public String getStatus() {
        
        return status;
    }

    /**
     * 设置开班状态
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
}
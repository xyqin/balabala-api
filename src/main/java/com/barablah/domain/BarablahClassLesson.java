package com.barablah.domain;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;
import java.util.Date;

/**
 *
 * RudderFramework框架自动生成，不允许修改！
 * 表 barablah_class_lesson
 * @generated do_not_delete_during_merge 2018年3月25日 10:55:18
 */
public class BarablahClassLesson extends AbstractEntity {
    /**
     *
     * {"viewconfig":{"optype":"1","formid":"1"},"name":"ID","fieldType":"Id","visible":true,"queryType":0,"displayOrder":0,"length":0,"types":[],"valid":false}
     * @generated 2018年3月25日 10:55:18
     */
    private Long id;

    /**
     *
     * {"viewconfig":{"optype":"3","formid":"1"},"name":"班级","fieldType":"Object","visible":true,"queryType":2,"displayOrder":0,"length":0,"ref":{"module":"BarablahClass","field":"className","type":"SingleList"},"valid":true}
     * @generated 2018年3月25日 10:55:18
     */
    private Long classId;

    /**
     *
     * {"viewconfig":{"optype":"3","formid":"1"},"name":"课时名称","fieldType":"String","visible":true,"checkName":false,"queryType":2,"displayOrder”:0,”length":16,"types":[],"valid":true}
     * @generated 2018年3月25日 10:55:18
     */
    @NotEmpty(message="课时名称不能为空")
    @Size(max=0,message = "课时名称长度无效")

    private String lessonName;

    /**
     *
     * {"viewconfig":{"optype":"3","formid":"1"},"name":"配套教材","fieldType":"Object","visible":true,"queryType":0,"displayOrder":0,"length":0,"ref":{"module":"BarablahTextbookCategory","field":"categoryName","type":"SingleTree"},"valid":true}
     * @generated 2018年3月25日 10:55:18
     */
    private Long categoryId;

    /**
     *
     * {"viewconfig":{"optype":"3","formid":"1"},"name":"开课时间","fieldType":"Date","visible":true,"queryType":0,"displayOrder":0,"length":0,"types":[],"valid":true}
     * @generated 2018年3月25日 10:55:18
     */
    private Date startAt;

    /**
     *
     * {"viewconfig":{"optype":"3","formid":"1"},"name":"结束时间","fieldType":"Date","visible":true,"queryType":0,"displayOrder":0,"length":0,"types":[],"valid":true}
     * @generated 2018年3月25日 10:55:18
     */
    private Date endAt;

    /**
     *
     * {"viewconfig":{"optype":"3","formid":"1"},"name":"视频预览","fieldType":"Image","visible":true,"checkName":false,"queryType":0,"displayOrder”:0,”length":16,"types":[],"valid":false}
     * @generated 2018年3月25日 10:55:18
     */
    private String thumbnail;

    /**
     *
     * {"viewconfig":{"optype":"3","formid":"1"},"name":"课时视频","fieldType":"String","visible":false,"checkName":false,"queryType":0,"displayOrder”:0,”length":16,"types":[],"valid":false}
     * @generated 2018年3月25日 10:55:18
     */
    private String video;

    /**
     *
     * {"viewconfig":{"optype":"3","formid":"1"},"name":"网易云房间","fieldType":"String","visible":false,"checkName":false,"queryType":0,"displayOrder”:0,”length":16,"types":[],"valid":false}
     * @generated 2018年3月25日 10:55:18
     */
    private String room;

    /**
     *
     * {"viewconfig":{"optype":"3","formid":"1"},"name":"备课情况","fieldType":"Bool","visible":true,"queryType":0,"displayOrder":0,"length":0,"types":[],"valid":true,"remark":"1已备课，0未备课"}
     * @generated 2018年3月25日 10:55:18
     */
    private Boolean prepared;

    /**
     *
     * {"viewconfig":{"optype":"3","formid":"1"},"name":"课时状态","fieldType":"Enum","visible":true,"queryType":2,"displayOrder":0,"length":0,"types":[{"label":"待开课","value":"WAITING"},{"label":"已开课","value":"GOING"},{"label":"已过期","value":"GONE"},{"label":"已结束","value":"FINISH"}],"valid":false}
     * @generated 2018年3月25日 10:55:18
     */
    private String status;

    /**
     *
     * {"viewconfig":{"optype":"3","formid":"1"},"name":"是否已签到","fieldType":"Bool","visible":false,"queryType":0,"displayOrder":0,"length":0,"types":[],"valid":false,"remark":"1已签到，0签到"}
     * @generated 2018年3月25日 10:55:18
     */
    private Boolean sign;

    /**
     *
     * {"viewconfig":{"optype":"3","formid":"1"},"name":"类型","fieldType":"Enum","visible":true,"queryType":2,"displayOrder":0,"length":0,"types":[{"label":"线上","value":"ONLINE"},{"label":"线下","value":"OFFLINE"}],"valid":true}
     * @generated 2018年3月25日 10:55:18
     */
    @NotEmpty(message="类型不能为空")
    @Size(max=0,message = "类型长度无效")

    private String type;

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
     * 返回 class_id 班级
     * @generated 2018年3月25日 10:55:18
     */
    public Long getClassId() {
        
        return classId;
    }

    /**
     * 设置班级
     *
     * @param classId
     * @generated 2018年3月25日 10:55:18
     */
    public void setClassId(Long classId) {
        this.classId = classId;
    }

    /**
     * 返回 lesson_name 课时名称
     * @generated 2018年3月25日 10:55:18
     */
    public String getLessonName() {
        
        return lessonName;
    }

    /**
     * 设置课时名称
     *
     * @param lessonName
     * @generated 2018年3月25日 10:55:18
     */
    public void setLessonName(String lessonName) {
        this.lessonName = lessonName == null ? null : lessonName.trim();
    }

    /**
     * 返回 category_id 配套教材
     * @generated 2018年3月25日 10:55:18
     */
    public Long getCategoryId() {
        
        return categoryId;
    }

    /**
     * 设置配套教材
     *
     * @param categoryId
     * @generated 2018年3月25日 10:55:18
     */
    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * 返回 start_at 开课时间
     * @generated 2018年3月25日 10:55:18
     */
    public Date getStartAt() {
        
        return startAt;
    }

    /**
     * 设置开课时间
     *
     * @param startAt
     * @generated 2018年3月25日 10:55:18
     */
    public void setStartAt(Date startAt) {
        this.startAt = startAt;
    }

    /**
     * 返回 end_at 结束时间
     * @generated 2018年3月25日 10:55:18
     */
    public Date getEndAt() {
        
        return endAt;
    }

    /**
     * 设置结束时间
     *
     * @param endAt
     * @generated 2018年3月25日 10:55:18
     */
    public void setEndAt(Date endAt) {
        this.endAt = endAt;
    }

    /**
     * 返回 thumbnail 视频预览
     * @generated 2018年3月25日 10:55:18
     */
    public String getThumbnail() {
        
        return thumbnail;
    }

    /**
     * 设置视频预览
     *
     * @param thumbnail
     * @generated 2018年3月25日 10:55:18
     */
    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail == null ? null : thumbnail.trim();
    }

    /**
     * 返回 video 课时视频
     * @generated 2018年3月25日 10:55:18
     */
    public String getVideo() {
        
        return video;
    }

    /**
     * 设置课时视频
     *
     * @param video
     * @generated 2018年3月25日 10:55:18
     */
    public void setVideo(String video) {
        this.video = video == null ? null : video.trim();
    }

    /**
     * 返回 room 网易云房间
     * @generated 2018年3月25日 10:55:18
     */
    public String getRoom() {
        
        return room;
    }

    /**
     * 设置网易云房间
     *
     * @param room
     * @generated 2018年3月25日 10:55:18
     */
    public void setRoom(String room) {
        this.room = room == null ? null : room.trim();
    }

    /**
     * 返回 prepared 备课情况
     * @generated 2018年3月25日 10:55:18
     */
    public Boolean getPrepared() {
        
        return prepared;
    }

    /**
     * 设置备课情况
     *
     * @param prepared
     * @generated 2018年3月25日 10:55:18
     */
    public void setPrepared(Boolean prepared) {
        this.prepared = prepared;
    }

    /**
     * 返回 status 课时状态
     * @generated 2018年3月25日 10:55:18
     */
    public String getStatus() {
        
        return status;
    }

    /**
     * 设置课时状态
     *
     * @param status
     * @generated 2018年3月25日 10:55:18
     */
    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    /**
     * 返回 sign 是否已签到
     * @generated 2018年3月25日 10:55:18
     */
    public Boolean getSign() {
        
        return sign;
    }

    /**
     * 设置是否已签到
     *
     * @param sign
     * @generated 2018年3月25日 10:55:18
     */
    public void setSign(Boolean sign) {
        this.sign = sign;
    }

    /**
     * 返回 type 类型
     * @generated 2018年3月25日 10:55:18
     */
    public String getType() {
        
        return type;
    }

    /**
     * 设置类型
     *
     * @param type
     * @generated 2018年3月25日 10:55:18
     */
    public void setType(String type) {
        this.type = type == null ? null : type.trim();
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
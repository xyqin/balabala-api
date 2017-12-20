package com.balabala.auth;

/**
 * Created by xyqin on 2017/4/2.
 */
public interface Authenticator {

    /**
     * 用户身份认证,已登录则返回true
     *
     * @return
     */
    boolean authenticate();

    Long getCurrentMemberId();

    void newSession(Long memberId);

    void invalidateSession();

    /**
     * 教师身份处理
     */
    boolean authenticateForTeacher();

    Long getCurrentTeacherId();

    void newSessionForTeacher(Long teacherId);

}

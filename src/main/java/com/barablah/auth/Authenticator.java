package com.barablah.auth;

/**
 * Created by xyqin on 2017/4/2.
 */
public interface Authenticator {

    /**
     * 用户身份认证,已登录则返回true
     *
     * @return
     */
    boolean isAuthenticated();

    Long getCurrentMemberId();

    void newSession(Long memberId);

    void invalidateSession();

    /**
     * 教师身份处理
     */
    boolean isTeacherAuthenticated();

    Long getCurrentTeacherId();

    void newSessionForTeacher(Long teacherId);

}

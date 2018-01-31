package com.barablah.auth;

import org.springframework.stereotype.Component;

/**
 * Created by xyqin on 2017/4/2.
 */
@Component
public class RedisAuthenticator implements Authenticator {

    @Override
    public boolean isAuthenticated() {
        WebApiContext context = WebApiContext.current();
        return context.isAuthenticated();
    }

    @Override
    public Long getCurrentMemberId() {
        return WebApiContext.current().getMemberId();
    }

    @Override
    public void newSession(Long memberId) {
        WebApiContext.current().newSession(memberId);
    }

    @Override
    public void invalidateSession() {
        WebApiContext.current().invalidateSession();
    }

    @Override
    public boolean isTeacherAuthenticated() {
        WebApiContext context = WebApiContext.current();
        return context.isTeacherAuthenticated();
    }

    @Override
    public Long getCurrentTeacherId() {
        return WebApiContext.current().getTeacherId();
    }

    @Override
    public void newSessionForTeacher(Long teacherId) {
        WebApiContext.current().newSessionForTeacher(teacherId);
    }

}

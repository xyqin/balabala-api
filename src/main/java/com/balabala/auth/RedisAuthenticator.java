package com.balabala.auth;

import com.balabala.Constants;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.util.Objects;

/**
 * Created by xyqin on 2017/4/2.
 */
@Component
public class RedisAuthenticator implements Authenticator {

    @Override
    public boolean authenticate() {
        WebApiContext context = WebApiContext.current();

        if (Objects.nonNull(context.getMemberId())) {
            return true;
        }

        HttpSession session = context.getRequest().getSession(false);

        if (Objects.nonNull(session)) {
            Object memberId = session.getAttribute(Constants.SESSION_KEY_MEMBER);

            if (Objects.nonNull(memberId)) {
                context.setMemberId(Long.valueOf(memberId.toString()));
                return true;
            }
        }

        return false;
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
    public boolean authenticateForTeacher() {
        WebApiContext context = WebApiContext.current();

        if (Objects.nonNull(context.getTeacherId())) {
            return true;
        }

        HttpSession session = context.getRequest().getSession(false);

        if (Objects.nonNull(session)) {
            Object teacherId = session.getAttribute(Constants.SESSION_KEY_TEACHER);

            if (Objects.nonNull(teacherId)) {
                context.setTeacherId(Long.valueOf(teacherId.toString()));
                return true;
            }
        }

        return false;
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

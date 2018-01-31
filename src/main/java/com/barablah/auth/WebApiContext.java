package com.barablah.auth;

import com.barablah.Constants;
import lombok.Data;
import lombok.Getter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Objects;

/**
 * Created by xyqin on 2017/4/2.
 */
@Data
public class WebApiContext {

    private HttpServletRequest request;

    @Getter
    private Long memberId;

    @Getter
    private Long teacherId;

    @Getter
    private boolean authenticated;

    @Getter
    private boolean teacherAuthenticated;

    private static final ThreadLocal<WebApiContext> current = new ThreadLocal<>();

    private WebApiContext() {
    }

    protected static WebApiContext current() {
        WebApiContext context = current.get();

        if (context == null) {
            context = new WebApiContext();
            current.set(context);
        }

        return context;
    }

    protected void setRequest(HttpServletRequest request) {
        this.request = request;

        HttpSession session = this.request.getSession(false);

        if (Objects.nonNull(session)) {
            Object memberIdStr = session.getAttribute(Constants.SESSION_KEY_MEMBER);

            // 从session提取memberId
            if (Objects.nonNull(memberIdStr)) {
                this.memberId = Long.valueOf(memberIdStr.toString());
                this.authenticated = true;
            }

            // 从session提取teacherId
            Object teacherIdStr = session.getAttribute(Constants.SESSION_KEY_TEACHER);

            if (Objects.nonNull(teacherIdStr)) {
                this.teacherId = Long.valueOf(teacherIdStr.toString());
                this.teacherAuthenticated = true;
            }
        }
    }

    void newSession(Long memberId) {
        if (Objects.nonNull(request)) {
            request.getSession().setAttribute(Constants.SESSION_KEY_MEMBER, memberId);
        }
    }

    void newSessionForTeacher(Long teacherId) {
        if (Objects.nonNull(request)) {
            request.getSession().setAttribute(Constants.SESSION_KEY_TEACHER, teacherId);
        }
    }

    void invalidateSession() {
        if (Objects.nonNull(request)) {
            request.getSession().invalidate();
        }
    }

    public void reset() {
        this.request = null;
        this.memberId = null;
        this.teacherId = null;
        this.authenticated = false;
        this.teacherAuthenticated = false;
    }

}

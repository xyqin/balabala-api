package com.finance4car.auth;

import com.finance4car.Constants;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * Created by xyqin on 2017/4/2.
 */
@Data
public class WebApiContext {

    @Getter
    @Setter(value = AccessLevel.PACKAGE)
    private HttpServletRequest request;

    @Getter
    @Setter(value = AccessLevel.PACKAGE)
    private Long memberId;

    private static final ThreadLocal<WebApiContext> current = new ThreadLocal<>();

    private WebApiContext() {
    }

    public static WebApiContext current() {
        WebApiContext context = current.get();

        if (context == null) {
            context = new WebApiContext();
            current.set(context);
        }

        return context;
    }

    void newSession(Long memberId) {
        if (Objects.nonNull(request)) {
            request.getSession().setAttribute(Constants.SESSION_KEY_MEMBER, memberId);
        }
    }

    void invalidateSession() {
        if (Objects.nonNull(request)) {
            request.getSession().invalidate();
        }
    }

    public String getRemoteIp() {
        if (Objects.isNull(request)) {
            return null;
        }

        String ip = request.getHeader("x-forwarded-for");

        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }

        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }

        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        return ip;
    }

    public void reset() {
        this.request = null;
        this.memberId = null;
    }

}

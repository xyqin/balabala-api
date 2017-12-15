package com.finance4car.auth;

import com.finance4car.Constants;
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
}

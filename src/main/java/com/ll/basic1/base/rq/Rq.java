package com.ll.basic1.base.rq;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.Arrays;
import java.util.Enumeration;

@Component
@RequestScope // 이 객체는 매 요청마다 생성된다.
@AllArgsConstructor
public class Rq {
    private final HttpServletRequest req;
    private final HttpServletResponse resp;
    public boolean removeCookie(String name) {
        if (req.getCookies() != null) {
            Cookie cookie = Arrays.stream(req.getCookies())
                    .filter(c -> c.getName().equals(name))
                    .findFirst()
                    .orElse(null);
            if (cookie != null) {
                cookie.setMaxAge(0);
                resp.addCookie(cookie);
                return true;
            }
        }
        return false;
    }
    public String getCookie(String name, String defaultValue) {
        if (req.getCookies() == null) return defaultValue;
        return Arrays.stream(req.getCookies())
                .filter(cookie -> cookie.getName().equals(name))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(defaultValue);
    }
    public long getCookieAsLong(String name, long defaultValue) {
        String value = getCookie(name, null);
        if (value == null) {
            return defaultValue;
        }
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
    public void setCookie(String name, long value) {
        setCookie(name, value + "");
    }
    public void setCookie(String name, String value) {
        resp.addCookie(new Cookie(name, value));
    }

    public void setSession(String name, long value) {
        HttpSession session = req.getSession();
        session.setAttribute(name, value);
    }

    public long getSessionAsLong(String name, long defaultValue) {
        try {
            long value = (long) req.getSession().getAttribute(name);
            return value;
        } catch (Exception e) {
            return defaultValue;
        }
    }

    private String getSessionAsStr(String name, String defaultValue) {
        try {
            String value = (String) req.getSession().getAttribute(name);
            if (value == null) return defaultValue;
            return value;
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public boolean removeSession(String name) {
        HttpSession session = req.getSession();

        if (session.getAttribute(name) == null) return false;

        session.removeAttribute(name);
        return true;
    }

    // 디버깅용 함수
    public String getSessionDebugContents() {
        HttpSession session = req.getSession();
        StringBuilder sb = new StringBuilder("Session content:\n");

        Enumeration<String> attributeNames = session.getAttributeNames();
        while (attributeNames.hasMoreElements()) {
            String attributeName = attributeNames.nextElement();
            Object attributeValue = session.getAttribute(attributeName);
            sb.append(String.format("%s: %s\n", attributeName, attributeValue));
        }

        return sb.toString();
    }

    public boolean isLogined() {
        long loginedMemberId = getSessionAsLong("loginedMemberId", 0);

        return loginedMemberId > 0;
    }

    public boolean isLogout() {
        return !isLogined();
    }

    public long getLoginedMemberId() {
        return getSessionAsLong("loginedMemberId", 0);
    }
}
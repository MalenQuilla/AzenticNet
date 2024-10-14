package malenquilla.cds.common.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import malenquilla.cds.common.enums.ECookies;

import java.util.Arrays;
import java.util.Objects;

@RequiredArgsConstructor
public class CookiesUtils {
    private final RuntimeEnvUtils runtimeEnvUtils;

    public Cookie createCookie(String name, String attribute, int age) {
        Cookie cookie = new Cookie(name, attribute);
        cookie.setMaxAge(age);

        cookie.setHttpOnly(true);
        cookie.setPath("/");

        boolean isSecure = !this.runtimeEnvUtils.isDevelop();
        cookie.setSecure(isSecure);

        return cookie;
    }

    public void deleteCookie(String name, HttpServletResponse response) {
        Cookie cookie = this.createCookie(name, null, 0);

        response.addCookie(cookie);
    }

    public void addCookie(Cookie cookie, HttpServletResponse response) {
        response.addCookie(cookie);
    }

    public void addCookie(String name, String attribute, int age, HttpServletResponse response) {
        Cookie cookie = this.createCookie(name, attribute, age);

        response.addCookie(cookie);
    }

    public void addAccessCookie(String accessToken, HttpServletResponse response) {
        Cookie cookie = this.createCookie(ECookies.ACCESS_TOKEN, accessToken, -1);

        response.addCookie(cookie);
    }

    public void addRefreshCookie(String refreshToken, HttpServletResponse response) {
        Cookie cookie = this.createCookie(ECookies.REFRESH_TOKEN, refreshToken, -1);

        response.addCookie(cookie);
    }

    public String getAccessToken(HttpServletRequest request) {
        try {
            return Arrays.stream(request.getCookies())
                         .filter(cookie -> Objects.equals(cookie.getName(), ECookies.ACCESS_TOKEN))
                         .findFirst()
                         .map(Cookie::getValue)
                         .orElse(null);
        } catch (RuntimeException exception) {
            return null;
        }
    }

    public String getRefreshToken(HttpServletRequest request) {
        try {
            return Arrays.stream(request.getCookies())
                         .filter(cookie -> Objects.equals(cookie.getName(), ECookies.REFRESH_TOKEN))
                         .findFirst()
                         .map(Cookie::getValue)
                         .orElse(null);
        } catch (RuntimeException exception) {
            return null;
        }
    }

    public void deleteAccessCookie(HttpServletResponse response) {
        this.deleteCookie(ECookies.ACCESS_TOKEN, response);
    }

    public void deleteRefreshCookie(HttpServletResponse response) {
        this.deleteCookie(ECookies.REFRESH_TOKEN, response);
    }
}

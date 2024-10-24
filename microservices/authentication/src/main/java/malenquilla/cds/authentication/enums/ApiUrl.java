package malenquilla.cds.authentication.enums;

public class ApiUrl {
    public static final String[] WHITELIST = {
            "/api/v1/auth/login",
            "/api/v1/auth/register",
            "/api/v1/auth/request-activation/*",
            "/api/v1/auth/refresh"
    };

    public static final String LOG_OUT = "/api/v1/auth/logout";
}

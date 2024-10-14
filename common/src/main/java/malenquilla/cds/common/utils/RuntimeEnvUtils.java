package malenquilla.cds.common.utils;

import org.springframework.beans.factory.annotation.Value;

public class RuntimeEnvUtils {
    public final static String RUNTIME_DEVELOP = "develop";
    public final static String RUNTIME_STAGING = "staging";
    public final static String RUNTIME_LIVE = "live";
    private static String ENVIRONMENT;

    @Value("${cds.environment}")
    public void setEnvironment(String environment) {
        RuntimeEnvUtils.ENVIRONMENT = environment;
    }

    public boolean isDevelop() {
        return ENVIRONMENT.equals(RUNTIME_DEVELOP);
    }

    public boolean isStaging() {
        return ENVIRONMENT.equals(RUNTIME_STAGING);
    }

    public boolean isLive() {
        return ENVIRONMENT.equals(RUNTIME_LIVE);
    }
}

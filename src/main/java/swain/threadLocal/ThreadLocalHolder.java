package swain.threadLocal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

public class ThreadLocalHolder {
    /**
     * 整个会话过程中会使用的标识
     */
    private static final ThreadLocal<HttpServletRequest> REQUEST_THREAD_LOCAL = new ThreadLocal<>();

    private static final ThreadLocal<HttpServletResponse> RESPONSE_THREAD_LOCAL = new ThreadLocal<>();

    private static final ThreadLocal<Long> START_TIME_THREAD_LOCAL = new ThreadLocal<>();

    private static final ThreadLocal<Object> REQUEST_BODY_THREAD_LOCAL = new ThreadLocal<>();

    private static final ThreadLocal<HashMap<String, String>> ATTRIBUTE_THREAD_LOCAL = new ThreadLocal<>();

    private static final ThreadLocal<Boolean> REDIS_FLAG = new ThreadLocal<>();



    public static void setResponse(HttpServletResponse response) {
        RESPONSE_THREAD_LOCAL.set(response);
    }

    public static HttpServletResponse getResponse() {
        return RESPONSE_THREAD_LOCAL.get();
    }

    public static void setReuqest(HttpServletRequest request) {
        REQUEST_THREAD_LOCAL.set(request);
    }

    public static HttpServletRequest getRequest() {
        return REQUEST_THREAD_LOCAL.get();
    }

    public static void setStartTime(Long startTime) {
        START_TIME_THREAD_LOCAL.set(startTime);
    }

    public static Long getStartTime() {
        return START_TIME_THREAD_LOCAL.get();
    }

    public static Object getRequestBodyThreadLocal() {
        return REQUEST_BODY_THREAD_LOCAL.get();
    }

    public static void setRequestBodyThreadLocal(Object requestBody) {
        REQUEST_BODY_THREAD_LOCAL.set(requestBody);
    }

    public static String getAttribute(String key) {
        if (null == ATTRIBUTE_THREAD_LOCAL.get()) {
            initAttribute();
        }
        return ATTRIBUTE_THREAD_LOCAL.get().get(key);
    }

    public static void setAttribute(String key, String value) {
        if (null == ATTRIBUTE_THREAD_LOCAL.get()) {
            initAttribute();
        }
        ATTRIBUTE_THREAD_LOCAL.get().put(key, value);
    }

    public static void initAttribute() {
        HashMap<String, String> map = new HashMap<>();
        ATTRIBUTE_THREAD_LOCAL.set(map);
    }

    public static void setRedisFlag(Boolean redisFlag) {
        REDIS_FLAG.set(redisFlag);
    }

    public static Boolean getRedisFlag() {
        return true;
//        return (null == REDIS_FLAG.get() ? true : REDIS_FLAG.get()) && BasicProbeFactory.isRedisAvailable();
    }
}

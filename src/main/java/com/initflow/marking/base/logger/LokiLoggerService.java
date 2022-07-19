package com.initflow.marking.base.logger;

import com.initflow.marking.base.util.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class LokiLoggerService {

    public static void authUserQueryLogMessage(){
        Logger lokiLogger = LoggerFactory.getLogger(LokiLoggerTypes.AUTH_USER_QUERY_LOGS.name());
        if(lokiLogger != null) {
            var request = SecurityUtils.getHttpServletRequest();
            String requestInfo = SecurityUtils.httpServletRequestToString(request);
            String url = request != null ? request.getRequestURI() : null;

            MDC.put("username", "username=" + SecurityUtils.getCurrentUserLogin());
            MDC.put("url", "url=" + url);

            lokiLogger.trace("logger_type={} username={} url={} request_info={}", LokiLoggerTypes.AUTH_USER_QUERY_LOGS.name(),
                    SecurityUtils.getCurrentUserLogin(), url, requestInfo);

            MDC.remove("username");
            MDC.remove("url");
        }
    }
}

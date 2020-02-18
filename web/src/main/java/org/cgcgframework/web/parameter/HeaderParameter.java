package org.cgcgframework.web.parameter;

import com.google.common.base.CaseFormat;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;

@Setter
@Getter
public class HeaderParameter extends HashMap<String, String> {
    private String cookie;
    private String accept;
    private String userAgent;
    private String cacheControl;
    private String upgradeInsecureRequests;
    private String connection;
    private String host;
    private String acceptLanguage;
    private String acceptEncoding;
    private String accessToken;

    public HeaderParameter(HttpServletRequest request) {
        final Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            final String name = headerNames.nextElement().replaceAll("-", "_");
            final String caseKey = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, name);
            this.put(caseKey, request.getHeader(name));
        }

        this.setCookie(this.get("cookie"));
        this.setAccept(this.get("accept"));
        this.setCacheControl(this.get("cacheControl"));
        this.setUserAgent(this.get("userAgent"));
        this.setUpgradeInsecureRequests(this.get("upgradeInsecureRequests"));
        this.setConnection(this.get("connection"));
        this.setHost(this.get("host"));
        this.setAcceptLanguage(this.get("acceptLanguage"));
        this.setAcceptEncoding(this.get("acceptEncoding"));
        this.setAccessToken(this.get("accessToken"));
    }
}

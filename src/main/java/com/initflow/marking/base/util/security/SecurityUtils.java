package com.initflow.marking.base.util.security;

import com.initflow.marking.base.constants.AuthRoles;
import org.keycloak.KeycloakPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.stream.Collectors;

/**
 * Utility class for Spring Security.
 */
public final class SecurityUtils {

    private SecurityUtils() {
    }

    /**
     * Get the logic of the current user.
     *
     * @return the logic of the current user
     */
    public static String getCurrentUserLogin() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        String userName = null;
        if (authentication != null) {
            if (authentication.getPrincipal() instanceof UserDetails) {
                UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
                userName = springSecurityUser.getUsername();
            } else if (authentication.getPrincipal() instanceof String) {
                userName = (String) authentication.getPrincipal();
            } else if (authentication.getPrincipal() instanceof KeycloakPrincipal){
                userName = ((KeycloakPrincipal) authentication.getPrincipal()).getName();
            }
        }
        return userName;
    }

    /**
     * Check if a user is authenticated.
     *
     * @return true if the user is authenticated, false otherwise
     */
    public static boolean isAuthenticated() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if (authentication != null) {
            return authentication.getAuthorities().stream()
                .noneMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(AuthRoles.ANONYMOUS));
        }
        return false;
    }

    /**
     * If the current user has a specific authority (security role).
     *
     * <p>The name of this method comes from the isUserInRole() method in the Servlet API</p>
     *
     * @param authority the authority to check
     * @return true if the current user has the authority, false otherwise
     */
    public static boolean isCurrentUserInRole(String authority) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if (authentication != null) {
            return authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(authority));
        }
        return false;
    }

    public static HttpServletRequest getHttpServletRequest(){
        var requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return requestAttributes != null ? requestAttributes.getRequest() : null;
    }

    public static String httpServletRequestToString(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        if(request != null) {

            sb.append("Request Method = [").append(request.getMethod()).append("], ");
            sb.append("Request URL Path = [").append(request.getRequestURL()).append("], ");

//            String headers =
//                    Collections.list((Enumeration<String>) request.getHeaderNames()).stream()
//                            .map(headerName -> headerName + " : " + Collections.list(request.getHeaders(headerName)))
//                            .collect(Collectors.joining(", "));
//
//            if (headers.isEmpty()) {
//                sb.append("Request headers: NONE,");
//            } else {
//                sb.append("Request headers: [").append(headers).append("],");
//            }

            String parameters =
                    Collections.list(request.getParameterNames()).stream()
                            .map(p -> p + " : " + Arrays.asList(request.getParameterValues(p)))
                            .collect(Collectors.joining(", "));

            if (parameters.isEmpty()) {
                sb.append("Request parameters: NONE.");
            } else {
                sb.append("Request parameters: [").append(parameters).append("].");
            }
        }

        return sb.toString();
    }
}

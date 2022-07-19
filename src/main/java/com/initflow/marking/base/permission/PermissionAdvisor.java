package com.initflow.marking.base.permission;

import com.initflow.marking.base.exception.model.ForbiddenMetricsBaseRuntimeException;
import com.initflow.marking.base.logger.LokiLoggerService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.core.annotation.Order;
import org.springframework.expression.EvaluationContext;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Aspect
//@Component
//@Order(25)
public class PermissionAdvisor {

    private ExpressionEvaluator<String> evaluator = new ExpressionEvaluator<>();

    @Before(value="@annotation(methodPermissionPath)")
    public void enableAuthFilter(JoinPoint joinPoint, PermissionPath methodPermissionPath) {
        SecurityContext securityContext = SecurityContextHolder.getContext();

        boolean isTruPermission = false;
        if(securityContext.getAuthentication() != null && securityContext.getAuthentication().getAuthorities() != null) {
            LokiLoggerService.authUserQueryLogMessage();

            Collection<? extends GrantedAuthority> grantedAuthorities = securityContext.getAuthentication().getAuthorities();
            Set<String> paths = new HashSet<>();
            for(var auth : grantedAuthorities) {
                if(auth instanceof PermissionGrantedAuthority){
                    var permissionAuth = (PermissionGrantedAuthority) auth;
                    paths.addAll(permissionAuth.getPermissions());
                }
            }

            var contains = new ArrayList<Boolean>();
            for(String methodPath : methodPermissionPath.value()) {

                methodPath = getValue(joinPoint, methodPath);
//                var methodPathClear = methodPath.replaceAll(":","");
                boolean contain = false;
                for(String userPath : paths){
                    userPath = userPath.replace("*","");
                    boolean checkSize = methodPath.length() >= userPath.length();
                    boolean checkContains = methodPath.contains(userPath);
                    if(checkContains && checkSize){
                        contain = true;
                        break;
                    }
                }
                contains.add(contain);
            }

            isTruPermission = checkContains(methodPermissionPath.logical(), contains);
        }

        if(!isTruPermission) throw new ForbiddenMetricsBaseRuntimeException("Forbidden. No right permissions.");
    }

    private boolean checkContains(PermissionLogical permissionLogical, ArrayList<Boolean> contains){
        if(permissionLogical == PermissionLogical.OR) {
            return contains.stream().anyMatch(it -> it);
        } else if(permissionLogical == PermissionLogical.AND) {
            return contains.stream().allMatch(it -> it);
        }
        return false;
    }

    private String getValue(JoinPoint joinPoint, String condition) {

        return condition.startsWith("#") ? getValue(joinPoint.getTarget(), joinPoint.getArgs(),
                joinPoint.getTarget().getClass(),
                ((MethodSignature) joinPoint.getSignature()).getMethod(), condition) : condition;
    }

    private String getValue(Object object, Object[] args, Class clazz, Method method, String condition) {
        EvaluationContext evaluationContext = evaluator.createEvaluationContext(object, clazz, method, args);
        AnnotatedElementKey methodKey = new AnnotatedElementKey(method, clazz);
        return evaluator.condition(condition, methodKey, evaluationContext, String.class);
    }
}

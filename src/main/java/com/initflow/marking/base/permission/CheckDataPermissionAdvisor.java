package com.initflow.marking.base.permission;

import com.initflow.marking.base.exception.model.ForbiddenMetricsBaseRuntimeException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.core.annotation.Order;
import org.springframework.expression.EvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
//@Component
//@Order(50)
public class CheckDataPermissionAdvisor {

    private ExpressionEvaluator<Boolean> evaluator = new ExpressionEvaluator<>();

    @Before(value="@annotation(checkDataPermission)")
    public void enableAuthFilter(JoinPoint joinPoint, CheckDataPermission checkDataPermission) {
        boolean isTruPermission = getValue(joinPoint, checkDataPermission.value());
        if(!isTruPermission) throw new ForbiddenMetricsBaseRuntimeException("Forbidden. No right permissions.");
    }

    private boolean getValue(JoinPoint joinPoint, String condition) {
        if(condition == null || !condition.startsWith("#")) return false;
        return getValue(joinPoint.getTarget(), joinPoint.getArgs(),
                joinPoint.getTarget().getClass(),
                ((MethodSignature) joinPoint.getSignature()).getMethod(), condition);
    }

    private Boolean getValue(Object object, Object[] args, Class clazz, Method method, String condition) {
        EvaluationContext evaluationContext = evaluator.createEvaluationContext(object, clazz, method, args);
        AnnotatedElementKey methodKey = new AnnotatedElementKey(method, clazz);
        return evaluator.condition(condition, methodKey, evaluationContext, Boolean.class);
    }
}
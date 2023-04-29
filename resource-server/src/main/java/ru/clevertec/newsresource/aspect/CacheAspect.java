package ru.clevertec.newsresource.aspect;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Profile;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import ru.clevertec.newsresource.cache.Cache;
import ru.clevertec.newsresource.entity.Identifiable;

import java.util.Optional;
import java.util.stream.IntStream;

@Slf4j
@Aspect
@Component
@Profile("dev")
@RequiredArgsConstructor
public class CacheAspect {
    private final Cache<String, Object> cache;
    private final SpelExpressionParser parser = new SpelExpressionParser();

    @SneakyThrows
    @Around("cacheableGetMethods() && @annotation(cacheable)")
    public Object cacheableGetMethodsAdvice(ProceedingJoinPoint proceedingJoinPoint, Cacheable cacheable) {
        String key = getArgValue(cacheable.key(), proceedingJoinPoint).toString();
        String name = cacheable.value()[0];

        Optional<Object> cachedValue = cache.get(key, name);

        if (cachedValue.isPresent()) {
            return cachedValue;
        }

        Object result = proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
        cache.put(key, name, result);
        return result;
    }

    @SneakyThrows
    @AfterReturning(value = "cachePutSaveMethods() && @annotation(cachePut)", returning = "retVal")
    public void cachePutSaveMethodsAdvice(Object retVal, CachePut cachePut) {
        Identifiable<?> object = (Identifiable<?>) retVal;
        cache.put(object.getId().toString(), cachePut.value()[0], retVal);
    }

    @SneakyThrows
    @After("cachePutUpdateMethods() && @annotation(cachePut)")
    public void cachePutUpdateMethodsAdvice(JoinPoint joinPoint, CachePut cachePut) {
        String key = getArgValue(cachePut.key(), joinPoint).toString();
        String name = cachePut.value()[0];
        Object objectToPut = joinPoint.getArgs()[1];
        cache.put(key, name, objectToPut);
    }

    @Pointcut(value = "execution(* ru.clevertec.newsresource.service.*.*(..)) &&" +
            " @annotation(org.springframework.cache.annotation.Cacheable)")
    private void cacheableGetMethods() {}

    @Pointcut(value = "(execution(* ru.clevertec.newsresource.service.NewsService.saveNews(..)) || " +
            "execution(* ru.clevertec.newsresource.service.CommentService.insertComment(..))) && " +
            "@annotation(org.springframework.cache.annotation.CachePut)")
    private void cachePutSaveMethods() {}

    @Pointcut(value = "(execution(* ru.clevertec.newsresource.service.NewsService.updateNewsPartiallyById(..)) || " +
            "execution(* ru.clevertec.newsresource.service.CommentService.updateCommentPartiallyById(..))) && " +
            "@annotation(org.springframework.cache.annotation.CachePut)")
    private void cachePutUpdateMethods() {}

    private Object getArgValue(String argKey, JoinPoint joinPoint) {
        Expression expression = parser.parseExpression(argKey);
        EvaluationContext context = new StandardEvaluationContext();

        Object[] args = joinPoint.getArgs();
        String[] parameterNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();

        IntStream.range(0, args.length)
                .forEach(i -> context.setVariable(parameterNames[i], args[i]));

        return expression.getValue(context);
    }
}


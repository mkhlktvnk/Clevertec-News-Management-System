package ru.clevertec.newsresource.aspect;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.cache.annotation.CacheEvict;
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

/**
 * A Spring AOP aspect that provides caching functionality for methods annotated with
 * {@link org.springframework.cache.annotation.Cacheable}, {@link org.springframework.cache.annotation.CachePut},
 * or {@link org.springframework.cache.annotation.CacheEvict} annotations. This aspect is enabled only for
 * development and testing profiles, and uses a {@link Cache} instance to store cached values.
 */
@Slf4j
@Aspect
@Component
@Profile({"dev", "test"})
@RequiredArgsConstructor
public class CacheAspect {

    /**
     * The cache instance used to store cached values.
     */
    private final Cache<String, Object> cache;

    /**
     * The SpEL expression parser used to parse cache key and prefix values.
     */
    private final SpelExpressionParser parser = new SpelExpressionParser();

    /**
     * Advice method that intercepts method invocations annotated with {@link org.springframework.cache.annotation.Cacheable}
     * and caches the method result using the cache instance. The cached value is retrieved from the cache
     * if it is present and returned, otherwise the method is invoked and its result is cached and returned.
     *
     * @param proceedingJoinPoint The {@link ProceedingJoinPoint} representing the intercepted method invocation.
     * @param cacheable           The {@link Cacheable} annotation used to annotate the intercepted method.
     * @return The cached value, if present in the cache, or the result of the intercepted method invocation.
     * @throws Throwable If an error occurs during the intercepted method invocation.
     */
    @SneakyThrows
    @Around("cacheableGetMethods() && @annotation(cacheable)")
    public Object cacheableGetMethodsAdvice(ProceedingJoinPoint proceedingJoinPoint, Cacheable cacheable) {
        String key = getArgValue(cacheable.key(), proceedingJoinPoint).toString();
        String name = concatAnnotationValues(cacheable.value());

        Optional<Object> cachedValue = cache.get(key, name);

        if (cachedValue.isPresent()) {
            return cachedValue;
        }

        Object result = proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
        cache.put(key, name, result);

        return result;
    }

    /**
     * Advice method that intercepts method invocations annotated with {@link org.springframework.cache.annotation.CachePut}
     * and caches the method result using the cache instance.
     *
     * @param retVal   The value returned by the intercepted method invocation.
     * @param cachePut The {@link CachePut} annotation used to annotate the intercepted method.
     */
    @SneakyThrows
    @AfterReturning(value = "cachePutSaveMethods() && @annotation(cachePut)", returning = "retVal")
    public void cachePutSaveMethodsAdvice(Object retVal, CachePut cachePut) {
        Identifiable<?> object = (Identifiable<?>) retVal;
        String key = object.getId().toString();
        String prefix = concatAnnotationValues(cachePut.value());
        cache.put(key, prefix, retVal);
    }

    /**
     * Advice method that intercepts method invocations annotated with {@link org.springframework.cache.annotation.CachePut}
     * and updates the cached value using the cache instance.
     *
     * @param joinPoint The {@link JoinPoint} representing the intercepted method invocation.
     * @param cachePut  The {@link CachePut} annotation used to annotate the intercepted method.
     */
    @SneakyThrows
    @After("cachePutUpdateMethods() && @annotation(cachePut)")
    public void cachePutUpdateMethodsAdvice(JoinPoint joinPoint, CachePut cachePut) {
        String key = getArgValue(cachePut.key(), joinPoint).toString();
        String prefix = concatAnnotationValues(cachePut.value());
        Object objectToPut = joinPoint.getArgs()[1];
        cache.put(key, prefix, objectToPut);
    }

    /**
     * Advice executed after methods annotated with {@link org.springframework.cache.annotation.CacheEvict} are called.
     * Evicts the cached object associated with the given key and prefix from the cache.
     *
     * @param joinPoint the join point for the advice
     * @param cacheEvict the {@link org.springframework.cache.annotation.CacheEvict} annotation applied to the method
     */
    @SneakyThrows
    @After("cacheEvictDeleteMethods() && @annotation(cacheEvict)")
    public void cachePutDeleteMethodsAdvice(JoinPoint joinPoint, CacheEvict cacheEvict) {
        String key = getArgValue(cacheEvict.key(), joinPoint).toString();
        String prefix = concatAnnotationValues(cacheEvict.value());
        cache.evict(key, prefix);
    }

    @Pointcut(value = "execution(* ru.clevertec.newsresource.service.*.*(..)) &&" +
            " @annotation(org.springframework.cache.annotation.Cacheable)")
    private void cacheableGetMethods() {}

    @Pointcut(value = "(execution(* ru.clevertec.newsresource.service.NewsService.saveNews(..)) || " +
            "execution(* ru.clevertec.newsresource.service.CommentService.addCommentToNews(..))) && " +
            "@annotation(org.springframework.cache.annotation.CachePut)")
    private void cachePutSaveMethods() {}

    @Pointcut(value = "(execution(* ru.clevertec.newsresource.service.NewsService.updateNewsPartiallyById(..)) || " +
            "execution(* ru.clevertec.newsresource.service.CommentService.updateCommentPartiallyById(..))) && " +
            "@annotation(org.springframework.cache.annotation.CachePut)")
    private void cachePutUpdateMethods() {}

    @Pointcut(value = "(execution(* ru.clevertec.newsresource.service.NewsService.deleteNewsById(..)) || " +
            "execution(* ru.clevertec.newsresource.service.CommentService.deleteCommentById(..))) && " +
            "@annotation(org.springframework.cache.annotation.CacheEvict)")
    private void cacheEvictDeleteMethods() {}

    private String concatAnnotationValues(String[] values) {
        return String.join("", values);
    }

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


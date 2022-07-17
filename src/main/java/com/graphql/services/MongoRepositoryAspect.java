package com.graphql.services;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Aspect
@Component
public class MongoRepositoryAspect {
    private final Map<String, AtomicInteger> invocationsById = new ConcurrentHashMap<>();

    @Around("execution(* com.graphql.repositories.CategoryRepository.findById(String))")
//    @Around("execution(* org.springframework.data.mongodb.repository.MongoRepository+.findById(String))")
//    @Around("execution(* org.springframework.data.repository.reactive.ReactiveCrudRepository+.findById(String))")
    public Object profileRepositories(ProceedingJoinPoint joinPoint) throws Throwable {
        String categoryId = (String) joinPoint.getArgs()[0];
        int invocations = invocationsById.computeIfAbsent(categoryId, value -> new AtomicInteger()).incrementAndGet();
        log.info("Category with id = {} was invoked {} times", categoryId, invocations);
        return joinPoint.proceed(joinPoint.getArgs());
    }

}

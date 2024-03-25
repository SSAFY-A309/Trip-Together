package com.ssafy.triptogether.tripaccount.concurrency;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import com.ssafy.triptogether.global.utils.CustomSpringELParser;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Aspect
@Component
public class DistributedLockAop {
	private static final String REDISSON_LOCK_PREFIX = "LOCK:";
	private final RedissonClient redissonClient;
	private final AopForTransaction aopForTransaction;

	@Around("@annotation(com.ssafy.triptogether.tripaccount.concurrency.DistributedLock)")
	public Object lock(final ProceedingJoinPoint joinPoint) throws Throwable {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();
		DistributedLock distributedLock = method.getAnnotation(DistributedLock.class);

		String key = REDISSON_LOCK_PREFIX + CustomSpringELParser.getDynamicValue(signature.getParameterNames(), joinPoint.getArgs(), distributedLock.key());
		RLock rLock = redissonClient.getLock(key);

		try {
			boolean available = rLock.tryLock(distributedLock.waitTime(), distributedLock.leaseTime(), distributedLock.timeUnit());
			if (!available) {
				return false;
			}
			return aopForTransaction.proceed(joinPoint);
		} catch (InterruptedException exception) {
			throw new InterruptedException(); // TODO: 커스텀 예외 처리
		} finally {
			try {
				rLock.unlock();
			} catch (IllegalMonitorStateException exception) {
				throw new IllegalMonitorStateException(); // TODO: 커스텀 예외 처리
			}
		}
	}
}

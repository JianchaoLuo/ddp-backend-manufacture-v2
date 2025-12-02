package cn.hex.ddp.manufacture.application.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 这是日志切面，展示了Spring的AOP(面向切面)，
 * 我们可以把打印日志的部分抽出来，形成切面
 *
 * @author Huhaisen
 * @date 2024/05/07
 */
//@Slf4j
//@Aspect
//@Component
public class ApplicationLoggingAspect {

    @Autowired
    private ObjectMapper objectMapper;

//    @Pointcut("execution(* cn.hex.ddp.manufacture.application.*.service.impl.*.*(..))")
    public void pointcut() {
    }

//    @Around("pointcut()")
    public Object pringLog(ProceedingJoinPoint joinPoint) throws Throwable {
//        try{
//            log.info("进入方法 类名 {} 方法名 {} 入参 {}",
//                    joinPoint.getSignature().getDeclaringTypeName(),
//                    joinPoint.getSignature().getName(),
//                    objectMapper.writeValueAsString(joinPoint.getArgs()));
//        }catch (Exception e){
//            log.info("进入方法 类名 {} 方法名 {} 入参 {}",
//                    joinPoint.getSignature().getDeclaringTypeName(),
//                    joinPoint.getSignature().getName(),
//                    "入参无法序列化");
//        }

        Object result = joinPoint.proceed();
//        try {
//            log.info("离开方法 类名 {} 方法 {} 出参 {}",
//                    joinPoint.getSignature().getDeclaringTypeName(),
//                    joinPoint.getSignature().getName(),
//                    objectMapper.writeValueAsString(result));
//        } catch (Exception e) {
//            log.info("离开方法 类名 {} 方法 {} 出参 {}",
//                    joinPoint.getSignature().getDeclaringTypeName(),
//                    joinPoint.getSignature().getName(),
//                    "出参无法序列化");
//        }

        return result;
    }

}

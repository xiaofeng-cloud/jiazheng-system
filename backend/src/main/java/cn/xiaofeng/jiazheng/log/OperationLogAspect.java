package cn.xiaofeng.jiazheng.log;

import cn.xiaofeng.jiazheng.auth.LoginUser;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/** 自动记录控制器操作，避免在每个接口中重复编写审计代码。 */
@Aspect
@Component
public class OperationLogAspect {
    private final OperationLogMapper mapper;
    public OperationLogAspect(OperationLogMapper mapper) { this.mapper = mapper; }

    @Around("@within(org.springframework.web.bind.annotation.RestController)")
    public Object record(ProceedingJoinPoint point) throws Throwable {
        int result = 1;
        try { return point.proceed(); } catch (Throwable error) { result = 0; throw error; }
        finally {
            try {
                var authentication = SecurityContextHolder.getContext().getAuthentication();
                Long operatorId = authentication != null && authentication.getPrincipal() instanceof LoginUser user ? user.userId() : null;
                HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
                mapper.insert(new OperationLogMapper.OperationLogRow(mapper.nextId(), operatorId, point.getSignature().getDeclaringTypeName(), point.getSignature().getName(), request.getMethod(), request.getRequestURI(), result, null));
            } catch (Exception ignored) {
                // 审计记录失败不能覆盖原业务响应；生产环境可接入独立日志告警。
            }
        }
    }
}

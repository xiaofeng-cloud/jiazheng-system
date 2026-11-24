package cn.xiaofeng.jiazheng.common;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<Void> invalidArgument(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream().findFirst().map(error -> error.getDefaultMessage()).orElse("请求参数不正确");
        return ApiResponse.failure(HttpStatus.BAD_REQUEST.value(), message);
    }
    @ExceptionHandler(ConstraintViolationException.class)
    public ApiResponse<Void> invalidConstraint(ConstraintViolationException ex) { return ApiResponse.failure(400, ex.getMessage()); }
    @ExceptionHandler(IllegalArgumentException.class)
    public ApiResponse<Void> illegalArgument(IllegalArgumentException ex) { return ApiResponse.failure(400, ex.getMessage()); }
    @ExceptionHandler(IllegalStateException.class)
    public ApiResponse<Void> illegalState(IllegalStateException ex) { return ApiResponse.failure(409, ex.getMessage()); }
    @ExceptionHandler(Exception.class)
    public ApiResponse<Void> unexpected(Exception ex) { return ApiResponse.failure(500, "服务器处理请求失败"); }
}

package cn.xiaofeng.jiazheng.common;

/** 统一 REST 响应结构，便于前端统一处理成功和失败消息。 */
public record ApiResponse<T>(int code, String message, T data) {
    public static <T> ApiResponse<T> success(T data) { return new ApiResponse<>(200, "操作成功", data); }
    public static <T> ApiResponse<T> success(String message, T data) { return new ApiResponse<>(200, message, data); }
    public static <T> ApiResponse<T> failure(int code, String message) { return new ApiResponse<>(code, message, null); }
}

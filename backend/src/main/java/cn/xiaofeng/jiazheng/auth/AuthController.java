package cn.xiaofeng.jiazheng.auth;

import cn.xiaofeng.jiazheng.security.JwtTokenService;
import cn.xiaofeng.jiazheng.common.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenService tokenService;
    private final RegisterService registerService;

    public AuthController(AuthenticationManager authenticationManager, JwtTokenService tokenService, RegisterService registerService) {
        this.authenticationManager = authenticationManager; this.tokenService = tokenService; this.registerService = registerService;
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        var authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.username(), request.password()));
        String roleCode = authentication.getAuthorities().stream().findFirst().map(a -> a.getAuthority().replace("ROLE_", "")).orElse("CUSTOMER");
        long userId = authentication.getPrincipal() instanceof LoginUser loginUser ? loginUser.userId() : 0L;
        return ApiResponse.success(new LoginResponse(tokenService.createToken(userId, request.username(), roleCode), request.username(), roleCode));
    }

    @GetMapping("/me")
    public ApiResponse<Object> me(org.springframework.security.core.Authentication authentication) { return ApiResponse.success(authentication.getPrincipal()); }

    @PostMapping("/logout")
    public ApiResponse<Void> logout() { return ApiResponse.success("退出成功", null); }

    @PostMapping("/register")
    public ApiResponse<Long> register(@Valid @RequestBody RegisterRequest request) { return ApiResponse.success("注册成功", registerService.register(request)); }
}

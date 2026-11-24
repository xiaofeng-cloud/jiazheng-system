package cn.xiaofeng.jiazheng.auth;

import cn.xiaofeng.jiazheng.security.JwtTokenService;
import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenService tokenService;

    public AuthController(AuthenticationManager authenticationManager, JwtTokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        var authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.username(), request.password()));
        String roleCode = authentication.getAuthorities().stream().findFirst().map(a -> a.getAuthority().replace("ROLE_", "")).orElse("CUSTOMER");
        long userId = switch (request.username()) { case "admin" -> 1L; case "customer01" -> 1001L; case "provider01" -> 2001L; default -> 0L; };
        return new LoginResponse(tokenService.createToken(userId, request.username(), roleCode), request.username(), roleCode);
    }

    @GetMapping("/me")
    public Object me(org.springframework.security.core.Authentication authentication) { return authentication.getPrincipal(); }
}

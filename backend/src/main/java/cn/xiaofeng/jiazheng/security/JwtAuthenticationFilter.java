package cn.xiaofeng.jiazheng.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import cn.xiaofeng.jiazheng.auth.LoginUser;

import java.io.IOException;
import java.util.List;

/** 从 Authorization 请求头恢复当前用户身份。 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenService tokenService;

    public JwtAuthenticationFilter(JwtTokenService tokenService) { this.tokenService = tokenService; }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            try {
                JwtTokenService.TokenClaims claims = tokenService.parseToken(header.substring(7));
                var auth = new UsernamePasswordAuthenticationToken(new LoginUser(claims.userId(), claims.username(), "", claims.roleCode()), null,
                        List.of(new SimpleGrantedAuthority("ROLE_" + claims.roleCode())));
                SecurityContextHolder.getContext().setAuthentication(auth);
            } catch (RuntimeException ignored) {
                // 无效令牌交由 Spring Security 返回 401，不泄露解析细节。
            }
        }
        chain.doFilter(request, response);
    }
}

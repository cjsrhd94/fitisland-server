package happyperson.fitisland.global.security.jwt;

import happyperson.fitisland.global.security.oauth2.CustomOAuth2User;
import happyperson.fitisland.domain.user.entity.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    public JWTFilter(JWTUtil jwtUtil) {

        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //cookie들을 불러온 뒤 Authorization Key에 담긴 쿠키를 찾음
        String authorizationHeader = null;
        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            for (Cookie cookie : cookies) {

                if (cookie.getName().equals("Authorization")) {

                    authorizationHeader = cookie.getValue();
                }
            }
        }

        //Authorization 헤더 검증
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {

            filterChain.doFilter(request, response);

            //조건이 해당되면 메소드 종료 (필수)
            return;
        }

        //토큰
        String token = authorizationHeader.split(" ")[1];

        //토큰 소멸 시간 검증
        if (jwtUtil.isExpired(token)) {

            filterChain.doFilter(request, response);

            //조건이 해당되면 메소드 종료 (필수)
            return;
        }

        //토큰에서 email과 role 획득
        String email = jwtUtil.getEmail(token);
        String role = jwtUtil.getRole(token);

        User user = User.builder()
            .email(email)
            .role(role)
            .build();

        //UserDetails에 회원 정보 객체 담기
        CustomOAuth2User customOAuth2User = new CustomOAuth2User(user);

        //스프링 시큐리티 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.getAuthorities());
        //세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}

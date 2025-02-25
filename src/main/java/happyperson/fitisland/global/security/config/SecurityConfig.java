package happyperson.fitisland.global.security.config;

import happyperson.fitisland.domain.user.repository.UserRepository;
import happyperson.fitisland.global.security.jwt.CustomAuthenticationEntryPoint;
import happyperson.fitisland.global.security.jwt.JwtAuthenticationFilter;
import happyperson.fitisland.global.security.jwt.JwtAuthorizationFilter;
import happyperson.fitisland.global.security.jwt.JWTUtil;
import happyperson.fitisland.global.security.oauth2.CustomOAuth2UserService;
import happyperson.fitisland.global.security.oauth2.CustomSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomSuccessHandler customSuccessHandler;
    private final UserRepository userRepository;
    private final JWTUtil jwtUtil;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // cors 설정
        http
                .cors(corsCustomizer -> corsCustomizer.configurationSource(request -> {

                    // cors setting
                    CorsConfiguration configuration = new CorsConfiguration();

                    configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "https://fitisland-client.vercel.app"));
                    configuration.setAllowedMethods(Collections.singletonList("*"));
                    configuration.setAllowCredentials(true);
                    configuration.setAllowedHeaders(Collections.singletonList("*"));
                    configuration.setMaxAge(3600L);
                    configuration.setExposedHeaders(Collections.singletonList("Set-Cookie"));
                    configuration.setExposedHeaders(Collections.singletonList("Authorization"));
                    return configuration;
                }));
        http
                .csrf(AbstractHttpConfigurer::disable);
        http
                .formLogin(AbstractHttpConfigurer::disable);
        http
                .httpBasic(AbstractHttpConfigurer::disable);
        http
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // JWT 필터 설정
        http
                .addFilter(new JwtAuthenticationFilter(authenticationManager(authenticationConfiguration), jwtUtil))
                .addFilter(new JwtAuthorizationFilter(authenticationManager(authenticationConfiguration), userRepository, jwtUtil));
        http
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(authenticationEntryPoint)
                );
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login", "/join", "/error", "/health", "/api/v1/exercise/**").permitAll()
                        .anyRequest().authenticated());
        http
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(
                                userInfoEndpoint -> userInfoEndpoint.userService(customOAuth2UserService))
                        .successHandler(customSuccessHandler));
        http
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));

        return http.build();
    }
}

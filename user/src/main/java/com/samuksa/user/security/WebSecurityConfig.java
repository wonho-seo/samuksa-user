package com.samuksa.user.security;

import com.samuksa.user.security.basic.filter.JsonUsernamePasswordAuthenticationFilter;
import com.samuksa.user.security.exception.SecurityExceptionFilter;
import com.samuksa.user.security.jwt.filter.JwtAuthenticationFilter;
import com.samuksa.user.security.jwt.exception.JwtAuthenticationEntryPoint;
import com.samuksa.user.security.oauth2.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class WebSecurityConfig {

    private final JsonUsernamePasswordAuthenticationFilter jsonUsernamePasswordAuthenticationFilter;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final SecurityExceptionFilter securityExceptionFilter;
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers("/v2/api-docs", "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/**",
                "/swagger-ui.html",
                "/webjars/**", "/csrf", "/");
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .authorizeRequests()
                .antMatchers("/", "/**").permitAll()
                .antMatchers( "/login","/signup/*").permitAll()
                .antMatchers("/user/*","/board/create").hasRole("USER")
                .and()
                    .csrf().disable()
//                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
                    .formLogin().disable()
//                .successForwardUrl("/login")
//                .loginPage("/login")
//                .loginProcessingUrl("/logina")
//                        .usernameParameter("userId")
//                        .passwordParameter("password")
//                .defaultSuccessUrl("/login/test")
//                        .failureUrl("/login/test")
//                .and()
                    .exceptionHandling()
                    .authenticationEntryPoint(new JwtAuthenticationEntryPoint())

                .and()
                    .cors()
                .and()
                .oauth2Login()
                .redirectionEndpoint()
                .and()
                .defaultSuccessUrl("/login/oauth2")
//                .failureUrl("/login/test")
                .userInfoEndpoint()
                .userService(customOAuth2UserService)

        ;
        http
                .addFilterBefore(securityExceptionFilter, UsernamePasswordAuthenticationFilter.class);
//                .addFilterBefore(jsonUsernamePasswordAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.addAllowedOriginPattern("*");
        configuration.setAllowCredentials(true);
        configuration.addExposedHeader("Refresh-Token");
        configuration.addExposedHeader("Access-Token");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


}

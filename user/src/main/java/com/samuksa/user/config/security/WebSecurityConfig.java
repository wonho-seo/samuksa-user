package com.samuksa.user.config.security;

import com.samuksa.user.config.security.architecture.filter.JsonUsernamePasswordAuthenticationFilter;
import com.samuksa.user.config.security.architecture.filter.JwtAuthenticationFilter;
import com.samuksa.user.config.security.jwt.JwtAuthenticationEntryPoint;
import com.samuksa.user.config.security.jwt.JwtTokenProvider;
import com.samuksa.user.config.security.oauth2.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class WebSecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final JsonUsernamePasswordAuthenticationFilter jsonUsernamePasswordAuthenticationFilter;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final CustomOAuth2UserService customOAuth2UserService;

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
                .antMatchers( "/login").permitAll()
                .antMatchers(HttpMethod.POST, "/logina").permitAll()

                .antMatchers("/user/*").hasRole("USER")
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
                .defaultSuccessUrl("/login/oauth2")
//                .failureUrl("/login/test")
                .userInfoEndpoint()
                .userService(customOAuth2UserService)
        ;
        http
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider,bCryptPasswordEncoder), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jsonUsernamePasswordAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        ;
        return http.build();
    }
//    @Bean
//    public UserDetailsService userDetailsService(){
//        return this.userService;
//    }

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

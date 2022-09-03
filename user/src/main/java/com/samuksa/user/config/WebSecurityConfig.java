package com.samuksa.user.config;

import com.samuksa.user.config.jwt.JwtAuthenticationEntryPoint;
import com.samuksa.user.config.jwt.JwtAuthenticationFilter;
import com.samuksa.user.config.jwt.JwtTokenProvider;
import com.samuksa.user.mapper.UserMapper;
import com.samuksa.user.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.sql.DataSource;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class WebSecurityConfig {

    private final DataSource dataSource;
    private final UserMapper userMapper;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers("/v2/api-docs", "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/**",
                "/swagger-ui.html",
                "/webjars/**", "/csrf", "/");
    }

    @Bean
    public AuthenticationManager authenticationManager(
           AuthenticationConfiguration authenticationConfiguration
    ) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        return new MyAuthenticationProvider(passwordEncoder(),this.userService);
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .authorizeRequests()
                    .antMatchers("/*/signup", "/user/login", "/user/login/*","/user/signup/message", "/*/refresh-token").permitAll()
                    .antMatchers("/user/*").hasRole("USER")
                    .anyRequest().permitAll()
                .and()
                    .csrf().disable()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                    .formLogin()
                        .usernameParameter("userId")
                        .passwordParameter("passwd")
                        .defaultSuccessUrl("/user/user_access")
                        .failureUrl("/user/access_denied")
                .and()
                    .exceptionHandling()
                    .authenticationEntryPoint(new JwtAuthenticationEntryPoint())
                .and()
                    .cors();
        http
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider,this.passwordEncoder()), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    @Bean
    public UserDetailsService userDetailsService(){
        return this.userService;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.addAllowedOriginPattern("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}

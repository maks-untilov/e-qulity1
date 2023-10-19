package app.equalityboot.config;

import app.equalityboot.service.UserService;
import app.equalityboot.service.impl.UserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.util.Set;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    private UserService userService;

    public WebSecurityConfig(UserServiceImpl userService) {
        this.userService = userService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/", "/login", "/register", "/logout", "/questionnaire", "/confirm/**", "/inject/**").permitAll()
                        .anyRequest().authenticated())
                .formLogin((form) -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/profile", true) // Общий путь по умолчанию
                        .successHandler(successHandler()) // Перенаправление в зависимости от роли
                        .permitAll())
                .logout((logout) -> logout.logoutUrl("/logout").permitAll())
                .authenticationProvider(daoAuthenticationProvider());
        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler successHandler() {
        return (request, response, authentication) -> {
            Set<String> authorities = AuthorityUtils.authorityListToSet(authentication.getAuthorities());

            if (authorities.contains("ADMIN") || authorities.contains("BOSS")) {
                response.sendRedirect("/index");
            } else if (authorities.contains("MANAGER")) {
                response.sendRedirect("/panel/coordinator");
            } else if (authorities.contains("USER")) {
                response.sendRedirect("/panel/worker");
            } else {
                // Если роль неизвестна, перенаправляем на главную страницу
                response.sendRedirect("/");
            }
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }
}


package phuongnq.asm3.security;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import phuongnq.asm3.dao.UserRepository;
import phuongnq.asm3.exception.EntityNotFoundException;
import phuongnq.asm3.jwt.JwtTokenFilter;


@Configuration
@EnableWebSecurity
public class Asm3SecurityConfig {

    private UserRepository userRepo;

    private JwtTokenFilter jwtTokenFilter;

    @Autowired
    public Asm3SecurityConfig(UserRepository theUserRepository, JwtTokenFilter theJwtTokenFilter) {
        userRepo = theUserRepository;
        jwtTokenFilter = theJwtTokenFilter;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userRepo.findByUsername(username)
                .orElseThrow(
                        () -> new EntityNotFoundException("Thong tin dang nhap khong dung hoac tai khoan da bi khoa, " +
                                "vui long thu lai hoac lien he voi admin. Thanks!!!"));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authConfig) throws Exception {

        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authorizeHttpRequests(configurer ->
                configurer
                        .requestMatchers("/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/users-api/users").permitAll()
                        .requestMatchers("/users-api/**").hasRole("PATIENT")
                        .requestMatchers("/doctor-api/**").hasRole("DOCTOR")
                        .requestMatchers("/admin-api/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
        );

        http.exceptionHandling()
                .authenticationEntryPoint(
                        (request, response, ex) -> {
                            response.sendError(
                                    HttpServletResponse.SC_UNAUTHORIZED,
                                    ex.getMessage()
                            );
                        }
                );

        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}

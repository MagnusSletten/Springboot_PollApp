package com.example.backend.Security;

import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.example.backend.Model.User.User;
import com.example.backend.Repositories.UserRepository;

import jakarta.servlet.http.HttpServletResponse;



@Configuration
@EnableWebSecurity
public class SecurityConfig {

@Bean
SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    var repo = CookieCsrfTokenRepository.withHttpOnlyFalse();
    repo.setHeaderName("X-XSRF-TOKEN");
    repo.setCookieCustomizer(c -> c.path("/polling").sameSite("Lax"));

    var requestHandler = new CsrfTokenRequestAttributeHandler();
    requestHandler.setCsrfRequestAttributeName("_csrf");

    return http
        .cors(Customizer.withDefaults())
        .csrf(csrf -> csrf
            .csrfTokenRepository(repo)
            .csrfTokenRequestHandler(requestHandler)
            .ignoringRequestMatchers(
                req -> req.getRequestURI().startsWith("/polling/backend/users/auth/"),
                req -> req.getRequestURI().startsWith("/polling/backend/auth/csrf"),
                req -> req.getRequestURI().equals("/polling/backend/users") && "POST".equals(req.getMethod())
            )
        )
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(HttpMethod.GET, "/users")
            .hasAuthority("Admin")
            .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
            .requestMatchers("/users/auth/**").permitAll()
            .requestMatchers("/auth/csrf").permitAll()
            .requestMatchers(HttpMethod.POST, "/users").permitAll()
             .requestMatchers(HttpMethod.GET,  "/polls/**").permitAll()
            .requestMatchers(HttpMethod.POST, "/polls").authenticated()
            .requestMatchers(HttpMethod.DELETE, "/polls/**").authenticated()
             .anyRequest().authenticated() 
        )
        .exceptionHandling(e -> e
    .authenticationEntryPoint(
        new org.springframework.security.web.authentication.HttpStatusEntryPoint(
            org.springframework.http.HttpStatus.UNAUTHORIZED))
    .accessDeniedHandler((req, res, ex) -> res.setStatus(403))
)
        .formLogin(f -> f
            .loginProcessingUrl("/users/auth/login")
            .successHandler((req, res, auth) -> {
                CsrfToken token = (CsrfToken) req.getAttribute(CsrfToken.class.getName());
                if (token != null) res.setHeader(token.getHeaderName(), token.getToken());
                res.setStatus(HttpServletResponse.SC_OK);
            })
            .failureHandler((req, res, ex) -> res.setStatus(401))
            .permitAll()
        )
        .build();
}

@Bean
CorsConfigurationSource corsConfigurationSource() {
  var cfg = new CorsConfiguration();
  cfg.setAllowedOrigins(List.of("http://localhost:5173", "https://magnus-demo-project.com"));
  cfg.setAllowedMethods(List.of("GET","POST","PUT","PATCH","DELETE","OPTIONS"));
  cfg.setAllowedHeaders(List.of("Content-Type","X-XSRF-TOKEN","Authorization","X-Requested-With"));
  cfg.setAllowCredentials(true);
  var source = new UrlBasedCorsConfigurationSource();
  source.registerCorsConfiguration("/**", cfg);
  return source;
}
// User validation
@Bean
public UserDetailsService userDetailsService(UserRepository userRepository) {
    return new UserDetailsService() {
        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            Optional<User> maybeUser = userRepository.findByUsername(username);
            if (maybeUser.isEmpty()) {
                throw new UsernameNotFoundException("User does not exist");
            }
            return maybeUser.get();
        }
    };
}
    
@Bean
AuthenticationManager authenticationManager(AuthenticationConfiguration cfg) throws Exception {
    return cfg.getAuthenticationManager(); 
}
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}   


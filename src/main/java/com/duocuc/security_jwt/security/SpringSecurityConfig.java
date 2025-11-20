package com.duocuc.security_jwt.security;

import com.duocuc.security_jwt.security.filter.JwtAuthenticationFilter;
import com.duocuc.security_jwt.security.filter.JwtValidationFilter;
import org.apache.catalina.filters.CorsFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
// EnableMethodSecurity solamente se tiene que agregar si vamos a delimitar los roles de acceso desde el controlador
// @EnableMethodSecurity(prePostEnabled = true)
public class SpringSecurityConfig {

    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;

    // Esto nos permite devolver un script de codificador de contraseñas
    // Como necesito que sea un componente lo que estoy realizando debemos anotar con
    // con la anotación @Bean antes del método.
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager() throws Exception {
        return this.authenticationConfiguration.getAuthenticationManager();
    }

    // Esta componente es el que nos permitirá gestionar los accesos de cada uno de los enpoint que poseemos
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests((authz) -> {
                // De esta forma dejamos publica la ruta de usuario pero todo lo demás necesita autentificación
                    authz
                            // Le damos acceso público para que pueda consultar el listado de usuario
                            .requestMatchers(HttpMethod.GET, "/api/v1/users").permitAll()
                            // Le dejamos acceso público para poder registrarse
                            .requestMatchers(HttpMethod.POST, "/api/v1/users/register").permitAll()
                            // De esta forma podemos ir delimitando quien puede acceder a cada endpoint
                            // Otro método que podriamos hacer esto es a traves del controlador o service
                            .requestMatchers(HttpMethod.POST, "/api/v1/users").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.GET, "/api/v1/products", "/api/v1/products/{id}").hasAnyRole("ADMIN", "USER")
                            .requestMatchers(HttpMethod.POST, "/api/v1/products").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.PUT, "/api/v1/products/{id}").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.DELETE, "/api/v1/products/{id}").hasRole("ADMIN")
                            .anyRequest().authenticated();
                })
                // la configuracion del csrf la desactivamos dado que no estamos trabajando con un modelo MVC
                .addFilter(new JwtAuthenticationFilter(authenticationManager()))
                // Método que me permite realizar la validación del token
                .addFilter(new JwtValidationFilter(authenticationManager()))
                .csrf(config ->
                        config.disable()
                )
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // Y la sesion de generación la dejamos en STATELESS dado que esa autentificación se realizara mediante
                // un token JWT, no queda autentificado en una session HTTP si no mediante un token
                .sessionManagement(management ->
                        management.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    FilterRegistrationBean<CorsFilter> corsFilter() {
        FilterRegistrationBean<CorsFilter> corsBean = new FilterRegistrationBean<>(
                new CorsFilter()
        );
        corsBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return corsBean;
    }
}

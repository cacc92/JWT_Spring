package com.duocuc.security_jwt.security;

import com.duocuc.security_jwt.security.filter.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
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
                // De esta forma dejamo publica la ruta de usuario pero todo lo demás necesita autentificación
                    authz
                            // Le damos acceso público para que pueda consultar el listado de usuario
                            .requestMatchers(HttpMethod.GET, "/api/v1/users").permitAll()
                            // Le dejamos acceso público para poder registrarse
                            .requestMatchers(HttpMethod.POST, "/api/v1/users/register").permitAll()
                            .anyRequest().authenticated();
                })
                // la configuracion del csrf la desactivamos dado que no estamos trabajando con un modelo MVC
                .addFilter(new JwtAuthenticationFilter(authenticationManager()))
                .csrf(config ->
                        config.disable()
                )
                // Y la sesion de generación la dejamos en STATELESS dado que esa autentificación se realizara mediante
                // un token JWT, no queda autentificado en una session HTTP si no mediante un token
                .sessionManagement(management ->
                        management.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .build();
    }
}

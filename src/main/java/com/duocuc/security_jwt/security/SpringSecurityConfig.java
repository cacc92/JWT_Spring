package com.duocuc.security_jwt.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SpringSecurityConfig {

    // Esto nos permite devolver un script de codificador de contraseñas
    // Como necesito que sea un componente lo que estoy realizando debemos anotar con
    // con la anotación @Bean antes del método.
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

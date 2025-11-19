package com.duocuc.security_jwt.security.filter;

import com.duocuc.security_jwt.security.SimpleGrantedAuthorityJsonCreator;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static com.duocuc.security_jwt.security.TokenJwtConfig.*;

public class JwtValidationFilter extends BasicAuthenticationFilter {

    // Se debe implementar el constructor donde se realiza el llamado a la clase padre
    public JwtValidationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader(HEADER_STRING);

        // este es el caso que estamos accediendo a un recurso público como el login y el register
        if  (header == null || !header.startsWith(JWT_TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }
        // Le sacamos el bearer
        String token = header.replace(JWT_TOKEN_PREFIX, "");

        try {
            // CArgamos el claims donde tengo guardada la información del token
            Claims claims = Jwts.parser().verifyWith(SECRET_KEY).build().parseSignedClaims(token).getPayload();
            // Desde el claim sacamos el username
            String username = claims.getSubject();

            // Obtengo los perfiles a un objeto pero posteriormente lo tenemos que procesar
            Object authoritiesClaim = claims.get("authorities");

            // Formateo el valor authoritiesClaim a GrantedAuthority
            Collection<? extends GrantedAuthority> authorities = Arrays.asList(
                    new ObjectMapper()
                            .addMixIn(SimpleGrantedAuthority.class,SimpleGrantedAuthorityJsonCreator.class)
                            .readValue(authoritiesClaim.toString().getBytes(), SimpleGrantedAuthority[].class)
            );

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            chain.doFilter(request, response);

        } catch (JwtException e) {

            // En caso que el token no es valido
            Map<String, String> body = new HashMap<>();
            body.put("message", "The Token is not valid");
            body.put("error", e.getMessage());

            response.getWriter().write(new ObjectMapper().writeValueAsString(body));
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(CONTENT_TYPE);


        }





    }
}

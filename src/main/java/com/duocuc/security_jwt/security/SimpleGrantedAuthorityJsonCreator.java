package com.duocuc.security_jwt.security;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

// Clase Mixed que permitirá realizar el mapeo entre la Clase Role y la Clase Authorities
// Esto es un tipo de diccionario de traducción
public abstract class SimpleGrantedAuthorityJsonCreator {

    // Mapea que propiedad va transformar del objeto
    @JsonCreator
    public SimpleGrantedAuthorityJsonCreator(@JsonProperty("authority") String role) {

    }
}

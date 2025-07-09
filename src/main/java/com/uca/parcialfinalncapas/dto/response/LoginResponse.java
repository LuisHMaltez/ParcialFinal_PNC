package com.uca.parcialfinalncapas.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private String tipo = "Bearer";
    private Long id;
    private String nombre;
    private String correo;
    private String nombreRol;
} 
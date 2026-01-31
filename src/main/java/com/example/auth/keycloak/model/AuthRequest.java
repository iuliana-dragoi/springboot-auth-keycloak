package com.example.auth.keycloak.model;

public record AuthRequest(
        String username,
        String password
) {
}

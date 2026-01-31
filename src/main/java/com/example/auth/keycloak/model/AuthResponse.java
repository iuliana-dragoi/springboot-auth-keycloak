package com.example.auth.keycloak.model;

import java.util.List;

public record AuthResponse(
        String username,
        List<String> roles,
        String accessToken
) {
}

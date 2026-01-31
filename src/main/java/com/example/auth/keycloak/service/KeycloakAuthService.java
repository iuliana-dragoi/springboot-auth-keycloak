package com.example.auth.keycloak.service;

import com.example.auth.keycloak.client.KeycloakClient;
import com.example.auth.keycloak.dto.KeycloakTokenResponse;
import com.example.auth.keycloak.model.AuthRequest;
import com.example.auth.keycloak.model.AuthResponse;
import com.nimbusds.jwt.JWTParser;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

@Service
public class KeycloakAuthService {

    private final KeycloakClient keycloakClient;

    public KeycloakAuthService(KeycloakClient keycloakClient) {
        this.keycloakClient = keycloakClient;
    }

    public AuthResponse authenticate(AuthRequest request) {
        try {
            KeycloakTokenResponse tokenResponse = keycloakClient.login(request.username(), request.password());
            String accessToken = tokenResponse.getAccessToken();

            SignedJWT jwt = (SignedJWT) JWTParser.parse(accessToken);
            Map<String, Object> realmAccess = (Map<String, Object>) jwt.getJWTClaimsSet().getClaim("realm_access");
            List<String> roles = realmAccess != null
                    ? (List<String>) realmAccess.get("roles")
                    : List.of();

            return new AuthResponse(request.username(), roles, accessToken);

        } catch (ParseException e) {
            throw new RuntimeException("Failed to parse JWT token", e);
        } catch (Exception e) {
            throw new RuntimeException("Authentication failed: " + e.getMessage(), e);
        }

    }
}

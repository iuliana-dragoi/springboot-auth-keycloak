package com.example.auth.keycloak.client;

import com.example.auth.keycloak.dto.KeycloakTokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class KeycloakClient {

    private final WebClient webClient = WebClient.create();

    @Value("${keycloak.auth-server-url}")
    private String authServerUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.resource}")
    private String clientId;

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    public KeycloakTokenResponse login(String username, String password) {
        String url = authServerUrl + "/realms/" + realm + "/protocol/openid-connect/token";
        String body = "grant_type=password&client_id=" + clientId +
                "&client_secret=" + clientSecret +
                "&username=" + username +
                "&password=" + password;

        return webClient.post()
                .uri(url)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(KeycloakTokenResponse.class)
                .block();

    }
}

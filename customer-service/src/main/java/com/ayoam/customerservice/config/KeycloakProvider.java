package com.ayoam.customerservice.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@Getter
public class KeycloakProvider {
    private WebClient.Builder webClientBuiler;

    @Autowired
    public KeycloakProvider(WebClient.Builder webClientBuiler) {
        this.webClientBuiler = webClientBuiler;
    }


    @Value("${keycloak.auth-server-url}")
    public String serverURL;
    @Value("${keycloak.realm}")
    public String realm;
    @Value("${keycloak.resource}")
    public String clientID;
    @Value("${keycloak.credentials.secret}")
    public String clientSecret;

    private static Keycloak keycloak = null;

    public KeycloakProvider() {
    }

    public Keycloak getInstance() {
        if (keycloak == null) {

            return KeycloakBuilder.builder()
                    .realm(realm)
                    .serverUrl(serverURL)
                    .clientId(clientID)
                    .clientSecret(clientSecret)
                    .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                    .build();
        }
        return keycloak;
    }


    public KeycloakBuilder newKeycloakBuilderWithPasswordCredentials(String username, String password) {
        return KeycloakBuilder.builder() //
                .realm(realm) //
                .serverUrl(serverURL)//
                .clientId(clientID) //
                .clientSecret(clientSecret) //
                .username(username) //
                .password(password);
    }

//    public JsonNode refreshToken(String refreshToken) throws UnirestException {
//        String url = serverURL + "/realms/" + realm + "/protocol/openid-connect/token";
//        System.out.println(url);
//        return Unirest.post(url)
//                .header("Content-Type", "application/x-www-form-urlencoded")
//                .field("client_id", clientID)
//                .field("client_secret", clientSecret)
//                .field("refresh_token", refreshToken)
//                .field("grant_type", "refresh_token")
//                .asJson().getBody();
//    }

//    public ResponseEntity<?> refreshToken(String refreshToken) throws JsonProcessingException {
//        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
//        formData.add("client_id", clientID);
//        formData.add("client_secret", clientSecret);
//        formData.add("refresh_token", refreshToken);
//        formData.add("grant_type", "refresh_token");
//
//        String url = serverURL + "/realms/" + realm + "/protocol/openid-connect/token";
//
//        try{
//            Map<String,Object> response =  webClientBuiler.build().post()
//                    .uri(url)
//                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
//                    .body(BodyInserters.fromFormData(formData))
//                    .retrieve()
//                    .toEntity(JsonNode.class)
//                    .map(entity->{
//                        Map<String,Object> res = new HashMap<>();
//                        res.put("body",entity.getBody());
//                        res.put("status",entity.getStatusCode());
//                        return res;
//                    })
//                    .block();
//            return new ResponseEntity<>(response.get("body"), (HttpStatus) response.get("status"));
//        }
//        catch (WebClientResponseException ex){
//            ObjectMapper mapper = new ObjectMapper();
//            JsonNode errorResponse = mapper.readTree(ex.getResponseBodyAsString());
//            return new ResponseEntity<>(errorResponse,ex.getStatusCode());
//        }
//
//    }

    public Map<String,Object> refreshToken(String refreshToken) throws JsonProcessingException {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", clientID);
        formData.add("client_secret", clientSecret);
        formData.add("refresh_token", refreshToken);
        formData.add("grant_type", "refresh_token");

        String url = serverURL + "/realms/" + realm + "/protocol/openid-connect/token";

        try{
            Map<String,Object> response =  webClientBuiler.build().post()
                    .uri(url)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(BodyInserters.fromFormData(formData))
                    .retrieve()
                    .toEntity(JsonNode.class)
                    .map(entity->{
                        Map<String,Object> res = new HashMap<>();
                        res.put("body",entity.getBody());
                        res.put("status",entity.getStatusCode());
                        return res;
                    })
                    .block();
            return response;
        }
        catch (WebClientResponseException ex){
            JsonNode errorResponse = (new ObjectMapper()).readTree(ex.getResponseBodyAsString());
            Map<String,Object> res = new HashMap<>();
            res.put("body",errorResponse);
            res.put("status",ex.getStatusCode());
            return res;
        }

    }
}

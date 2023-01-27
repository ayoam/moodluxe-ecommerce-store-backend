package com.ayoam.customerservice.service;

import com.ayoam.customerservice.config.KeycloakProvider;
import com.ayoam.customerservice.dto.CustomerDto;
import com.ayoam.customerservice.dto.LoginRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;
import java.util.Map;


@Service
public class KeycloakService {
    @Value("${keycloak.realm}")
    public String realm;

    @Value("${keycloak.resource}")
    public String clientID;

    private final KeycloakProvider kcProvider;


    public KeycloakService(KeycloakProvider keycloakProvider) {
        this.kcProvider = keycloakProvider;
    }

    public Response createKeycloakUser(CustomerDto user) {
        UsersResource usersResource = kcProvider.getInstance().realm(realm).users();
        CredentialRepresentation credentialRepresentation = createPasswordCredentials(user.getPassword());
        UserRepresentation kcUser = new UserRepresentation();
        kcUser.setUsername(user.getEmail());
        kcUser.setCredentials(Collections.singletonList(credentialRepresentation));
        kcUser.setEmail(user.getEmail());
        kcUser.setFirstName(user.getFirstName());
        kcUser.setLastName(user.getLastName());
        kcUser.setEnabled(true);
        kcUser.setEmailVerified(false);

        Response response = usersResource.create(kcUser);
        if (response.getStatus() != 201) {
            throw new RuntimeException("error creating user!");
        }

        String userId = usersResource.search(user.getEmail()).get(0).getId();
//        System.out.println(userId);
        usersResource.get(userId).sendVerifyEmail();
//        usersResource.get(userId).executeActionsEmail(List.of("UPDATE_PASSWORD"));
        return response;
    }

    public void deleteKeycloakUser(String email) {
        UsersResource usersResource = kcProvider.getInstance().realm(realm).users();
        List<UserRepresentation> users = usersResource.search(email);

        if (users.size() > 0) {
            String userId = users.get(0).getId();
            usersResource.delete(userId);
        } else {
            throw new RuntimeException("User not found!");
        }
    }

    private static CredentialRepresentation createPasswordCredentials(String password) {
        CredentialRepresentation passwordCredentials = new CredentialRepresentation();
        passwordCredentials.setTemporary(false);
        passwordCredentials.setType(CredentialRepresentation.PASSWORD);
        passwordCredentials.setValue(password);
        return passwordCredentials;
    }

    public AccessTokenResponse loginKeycloakUser(LoginRequest loginRequest){
        Keycloak keycloak = kcProvider.newKeycloakBuilderWithPasswordCredentials(loginRequest.getEmail(), loginRequest.getPassword()).build();
        AccessTokenResponse accessTokenResponse = null;
        try {
            accessTokenResponse = keycloak.tokenManager().getAccessToken();
            return accessTokenResponse;
        } catch (Exception ex) {
            return null;
        }

    }

    public void updateUserPassword(String keycloakId,String newPassword){
        UserResource user=  kcProvider.getInstance().realm(realm).users().get(keycloakId);
        CredentialRepresentation credentialRepresentation = createPasswordCredentials(newPassword);
        user.resetPassword(credentialRepresentation);
    }

    public void sendForgotPasswordEmail(String email){
        UsersResource usersResource = kcProvider.getInstance().realm(realm).users();
        if(usersResource.search(email).stream().count()>0){
            String userId = usersResource.search(email).get(0).getId();
            usersResource.get(userId).executeActionsEmail(List.of("UPDATE_PASSWORD"));
//            usersResource.get(userId).sendVerifyEmail();
        }
    }

    public Map<String,Object> refreshToken(String refreshToken) throws JsonProcessingException {
        return kcProvider.refreshToken(refreshToken);
    }
}
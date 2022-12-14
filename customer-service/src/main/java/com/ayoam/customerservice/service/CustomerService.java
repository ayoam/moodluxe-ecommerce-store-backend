package com.ayoam.customerservice.service;

import com.ayoam.customerservice.converter.AdresseConverter;
import com.ayoam.customerservice.converter.CustomerConverter;
import com.ayoam.customerservice.dto.*;
import com.ayoam.customerservice.model.Country;
import com.ayoam.customerservice.model.Customer;
import com.ayoam.customerservice.model.CustomerAdresse;
import com.ayoam.customerservice.model.UserInfo;
import com.ayoam.customerservice.repository.CountryRepository;
import com.ayoam.customerservice.repository.CustomerAdresseRepository;
import com.ayoam.customerservice.repository.CustomerRepository;
import com.ayoam.customerservice.utils.JwtDataUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.extern.java.Log;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class CustomerService {
    private CustomerRepository customerRepository;
    private CustomerConverter customerConverter;
    private AdresseConverter adresseConverter;

    private WebClient.Builder webClientBuiler;
    private CustomerAdresseRepository customerAdresseRepository;
    private CountryRepository countryRepository;

    private KeycloakService keycloakService;

    private JwtDataUtil jwtDataUtil;

    @Value("${cart-service-url}")
    private String cart_service_url;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, CustomerConverter customerConverter, AdresseConverter adresseConverter, WebClient.Builder webClientBuiler, CustomerAdresseRepository customerAdresseRepository, CountryRepository countryRepository, KeycloakService keycloakAdminClientService,JwtDataUtil jwtDataUtil) {
        this.customerRepository = customerRepository;
        this.customerConverter = customerConverter;
        this.adresseConverter = adresseConverter;
        this.webClientBuiler = webClientBuiler;
        this.customerAdresseRepository = customerAdresseRepository;
        this.countryRepository = countryRepository;
        this.keycloakService = keycloakAdminClientService;
        this.jwtDataUtil=jwtDataUtil;
    }

    public getAllCustomersResponse getAllCustomers() {
        return new getAllCustomersResponse(customerRepository.findAll());
    }

    public Customer createCustomer(CustomerDto customerDto) {
        Customer customer = customerConverter.customerDtoToCustomer(customerDto);

        //check email adresse
        checkEmailAdresse(new CheckEmailRequest(customer.getEmail()));

        //create user in keycloak
        Response response = keycloakService.createKeycloakUser(customerDto);

        //get keycloak id from response
        String keycloakId = extractKeycloakIdFromHeaders(response);
        customer.setKeycloakId(keycloakId);

        CustomerAdresseDto adresseDto = customerDto.getAdresseDto();
        CustomerAdresse adresse = adresseConverter.adresseDtoToAdresse(adresseDto);

        //check if country exists
        Country country = countryRepository.findById(adresseDto.getCountryId()).orElse(null);
        if(country==null){
            throw new RuntimeException("country not found!");
        }

        adresse.setCountry(country);
        customerAdresseRepository.save(adresse);
        customer.setAdresse(adresse);

        customer = customerRepository.save(customer);

        //create customer cart
        CreateCartRequest req = new CreateCartRequest(customer.getIdc());
        CartCreatedResponse result = webClientBuiler.build().post()
                .uri(cart_service_url)
                .body(Mono.just(req),new ParameterizedTypeReference<CreateCartRequest>() {})
                .retrieve()
                .bodyToMono(CartCreatedResponse.class)
                .block();

        customer.setCartId(result.getCartId());

        return customerRepository.save(customer);
    }

    public void checkEmailAdresse(CheckEmailRequest emailRequest){
        Customer customer = customerRepository.findByEmailIgnoreCase(emailRequest.getEmail()).orElse(null);
        if(customer!=null){
            throw new RuntimeException("email already used!");
        }
    }


    private String extractKeycloakIdFromHeaders(Response response){
        String location = response.getHeaders().get("location").get(0)+"";
        List<String> resArr = Arrays.stream(location.split("/",0)).toList();
        System.out.println(resArr.get(resArr.size()-1));
        return resArr.get(resArr.size()-1);
    }



    public ResponseEntity<?> loginCustomer(LoginRequest loginRequest) throws JsonProcessingException {
        AccessTokenResponse response = keycloakService.loginKeycloakUser(loginRequest);
        if(response==null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }else{
            ObjectMapper mapper = new ObjectMapper();
            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setAccessToken(response.getToken());
            loginResponse.setRefreshToken(response.getRefreshToken());
            loginResponse.setUserInfo(buildUserInfoObject(mapper.convertValue(response, ObjectNode.class)));
            return new ResponseEntity<>(loginResponse, HttpStatus.OK);
        }
    }

    public void deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id).orElse(null);
        if(customer==null){
            throw new RuntimeException("customer not found!");
        }
        customerRepository.delete(customer);
    }

    public Customer updateCustomerDetails(CustomerDetailsDto customerDetailsDto, Long id) {

        Customer c = customerRepository.findById(id).orElse(null);
        if(c==null){
            throw new RuntimeException("customer not found!");
        }
        c.setFirstName(customerDetailsDto.getFirstName());
        c.setLastName(customerDetailsDto.getLastName());
        c.setPhoneNumber(customerDetailsDto.getPhoneNumber());
        c.setBirthDate(customerDetailsDto.getBirthDate());

        return customerRepository.save(c);
    }

    public Customer updateCustomerAdresse(CustomerAdresseDto adresseDto, Long id) {
        Customer c = customerRepository.findById(id).orElse(null);
        CustomerAdresse oldAdresse = c.getAdresse();
        CustomerAdresse adresse = adresseConverter.adresseDtoToAdresse(adresseDto);
        if(c==null){
            throw new RuntimeException("customer not found!");
        }
        //check if country exists
        Country country = countryRepository.findById(adresseDto.getCountryId()).orElse(null);
        if(country==null){
            throw new RuntimeException("country not found!");
        }

        adresse.setCountry(country);
        customerAdresseRepository.save(adresse);
        c.setAdresse(adresse);
        customerAdresseRepository.delete(oldAdresse);

        return customerRepository.save(c);
    }

    public Customer updateCustomerPassword(UpdatePasswordDto passwordDto, HttpServletRequest request) {
        String userID = jwtDataUtil.extractUserID(request);
        List<String>  userRoles  =jwtDataUtil.extractUserRoles(request);
        System.out.println(userID);
        System.out.println(userRoles);

        //login with old password
        Customer customer =customerRepository.findByKeycloakId(userID).orElse(null);
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(customer.getEmail());
        loginRequest.setPassword(passwordDto.getOldPassword());

        if(keycloakService.loginKeycloakUser(loginRequest)==null){
            throw new RuntimeException("old password not valid!");
        }

        //update password in keycloak
        keycloakService.updateUserPassword(userID,passwordDto.getNewPassword());

//        Customer c = customerRepository.findById(id).orElse(null);
//        if(c==null){
//            throw new RuntimeException("customer not found!");
//        }
//        if(!Objects.equals(passwordDto.getOldPassword(), c.getPassword())){
//            throw new RuntimeException("incorrect old password!");
//        }
//        c.setPassword(passwordDto.getNewPassword());
//        return customerRepository.save(c);
        return null;
    }

    public void forgotPasswordHandler(ForgotPasswordRequest forgotPasswordRequest) {
        keycloakService.sendForgotPasswordEmail(forgotPasswordRequest.getEmail());
    }

    public ResponseEntity<?> checkEmailExistance(String email){
        customerRepository.findByEmailIgnoreCase(email).orElseThrow(()->new NotFoundException("email not found"));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<?> refreshToken(RefreshTokenRequest refreshTokenRequest) throws JsonProcessingException {
        Map<String,Object> resObj = keycloakService.refreshToken(refreshTokenRequest.getRefreshToken());
        if((HttpStatus)resObj.get("status")==HttpStatus.valueOf(200)){
            ObjectNode body = (ObjectNode) resObj.get("body");
            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setAccessToken(body.get("access_token").asText());
            loginResponse.setRefreshToken(body.get("refresh_token").asText());
            loginResponse.setUserInfo(buildUserInfoObject(body));

            return new ResponseEntity<>(loginResponse,(HttpStatus)resObj.get("status"));
        }
        else{
            return new ResponseEntity<>(resObj.get("body"),(HttpStatus)resObj.get("status"));
        }
    }


    private UserInfo buildUserInfoObject(ObjectNode body) throws JsonProcessingException {
        //decode jwt and extract email
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String[] chunks = body.get("access_token").toString().split("\\.");
        String payload = new String(decoder.decode(chunks[1]));
        String email = (new ObjectMapper()).readTree(payload).get("email").asText();
        String firstName = (new ObjectMapper()).readTree(payload).get("given_name").asText();
        String lastName = (new ObjectMapper()).readTree(payload).get("family_name").asText();
        String emailVerified = (new ObjectMapper()).readTree(payload).get("email_verified").asText();
        String keycloakId = (new ObjectMapper()).readTree(payload).get("sid").asText();

        //get roles from jwt token
        List<String> roles = new ArrayList<>();
        JsonNode arrNode= (new ObjectMapper()).readTree(payload).get("realm_access").get("roles");

        arrNode.forEach(item->{
            roles.add(item.asText());
        });

        Customer customer = customerRepository.findByEmailIgnoreCase(email).orElse(null);
        UserInfo userInfo = new UserInfo(firstName,lastName,email,keycloakId,roles);

        if(customer!=null){
            userInfo.setCustomerId(customer.getIdc());
            userInfo.setCartId(customer.getCartId());
        }

        return userInfo;
    }

    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id).orElseThrow();
    }

    public Long getCustomersTotal() {
        return customerRepository.customersTotal();
    }
}

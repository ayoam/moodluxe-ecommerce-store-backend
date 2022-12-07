package com.ayoam.customerservice.service;

import com.ayoam.customerservice.converter.AdresseConverter;
import com.ayoam.customerservice.converter.CustomerConverter;
import com.ayoam.customerservice.dto.*;
import com.ayoam.customerservice.model.Country;
import com.ayoam.customerservice.model.Customer;
import com.ayoam.customerservice.model.CustomerAdresse;
import com.ayoam.customerservice.repository.CountryRepository;
import com.ayoam.customerservice.repository.CustomerAdresseRepository;
import com.ayoam.customerservice.repository.CustomerRepository;
import com.ayoam.customerservice.utils.JwtDataUtil;
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
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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



    public ResponseEntity<?> loginCustomer(LoginRequest loginRequest){
        AccessTokenResponse response = keycloakService.loginKeycloakUser(loginRequest);
        if(response==null){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }else{
            return new ResponseEntity<>(keycloakService.loginKeycloakUser(loginRequest), HttpStatus.OK);
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
}

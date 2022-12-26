package com.ayoam.emailservice.controller;



import com.ayoam.emailservice.dto.AllContactsResponse;
import com.ayoam.emailservice.model.Contact;
import com.ayoam.emailservice.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/contacts")
public class ContactController {

    public ContactService contactService;
    @Autowired
    public ContactController (ContactService contactService){this.contactService = contactService;}

    @PostMapping
    public ResponseEntity<?> addContact(@RequestBody Contact contact){
        return new ResponseEntity<Contact>(contactService.addContact(contact), HttpStatus.CREATED);
    }
    @GetMapping
    public ResponseEntity<?> getContacts(){
        return new ResponseEntity<AllContactsResponse>(contactService.getContacts(), HttpStatus.OK);
    }
}

package com.ayoam.emailservice.service;

import com.ayoam.emailservice.dto.AllContactsResponse;
import com.ayoam.emailservice.model.Contact;
import com.ayoam.emailservice.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContactService {
    public ContactRepository contactRepository;
    @Autowired
    public ContactService(ContactRepository contactRepository){this.contactRepository = contactRepository; }

    public Contact addContact(Contact contact) {
        if(contactRepository.findContactByEmail(contact.getEmail()).orElse(null)!=null){
            throw new RuntimeException("contact already exist");
        }
        return contactRepository.save(contact);
    }

    public AllContactsResponse getContacts() {
        AllContactsResponse res = new AllContactsResponse();
        res.setContactList(contactRepository.findAll());
        return res;
    }
}

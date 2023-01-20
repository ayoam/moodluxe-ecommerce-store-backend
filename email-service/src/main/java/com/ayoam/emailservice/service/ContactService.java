package com.ayoam.emailservice.service;

import com.ayoam.emailservice.dto.AllContactsResponse;
import com.ayoam.emailservice.model.Contact;
import com.ayoam.emailservice.repository.ContactRepository;
import jakarta.ws.rs.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ContactService {
    public ContactRepository contactRepository;
    @Autowired
    public ContactService(ContactRepository contactRepository){this.contactRepository = contactRepository; }

    public Contact addContact(Contact contact) {
//        if(contactRepository.findContactByEmail(contact.getEmail()).orElse(null)!=null){
//            throw new RuntimeException("contact already exist");
//        }
        contact.setDate(new Date());
        return contactRepository.save(contact);
    }

    public AllContactsResponse getContacts() {
        AllContactsResponse res = new AllContactsResponse();
        res.setContactList(contactRepository.findAll());
        return res;
    }

    public Long getUnreadMessageCount() {

        return contactRepository.countByReadFalse();

    }

    public void markAsRead(Long id) {
        Contact contact = contactRepository.findById(id).orElseThrow(() -> new NotFoundException("Contact not found"));
        contact.setRead(true);
        contactRepository.save(contact);
    }
    public void deleteContact(Long id) {
        contactRepository.deleteById(id);
    }
}

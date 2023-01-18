package com.ayoam.emailservice.repository;

import com.ayoam.emailservice.model.Contact;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
    public Optional<Contact> findContactByEmail(String email);

    Long countByReadFalse();

    List<Contact> findByRead(boolean b);
}

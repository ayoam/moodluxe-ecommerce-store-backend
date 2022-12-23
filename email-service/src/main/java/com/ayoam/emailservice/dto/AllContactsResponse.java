package com.ayoam.emailservice.dto;

import com.ayoam.emailservice.model.Contact;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class AllContactsResponse {
    @JsonProperty("data")
    public List<Contact> contactList;

}

package com.scm.Service.Impl;

import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.scm.Entity.Contact;
import com.scm.Entity.User;
import com.scm.Helper.ResourceNotFoundException;
import com.scm.Repositary.ContactRepositary;
import com.scm.Service.ContactService;

@Service
public class ContactServiceImpl implements ContactService{

    @Autowired
    private ContactRepositary contactRepositary;

    @Autowired
    private ModelMapper model;

    @Override
    public Contact save(Contact contact) {
        contact.setId(UUID.randomUUID().toString());
        return contactRepositary.save(contact);
    }

    @Override
    public Contact update(Contact contact) {
        var contactOld = contactRepositary.findById(contact.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Contact not found"));
        contactOld.setName(contact.getName());
        contactOld.setEmail(contact.getEmail());
        contactOld.setPhoneNumber(contact.getPhoneNumber());
        contactOld.setAddress(contact.getAddress());
        contactOld.setDescription(contact.getDescription());
        contactOld.setPicture(contact.getPicture());
        contactOld.setFavorite(contact.isFavorite());
        contactOld.setWebsiteLink(contact.getWebsiteLink());
        contactOld.setLinkedInLink(contact.getLinkedInLink());
        contactOld.setCloudinaryImagePublicId(contact.getCloudinaryImagePublicId());

        return contactRepositary.save(contactOld);
    }

    @Override
    public List<Contact> getAll() {
        return contactRepositary.findAll();
    }

    @Override
    public Contact getById(String id) {
        return contactRepositary.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contact not found with given id " + id));
    }

    @Override
    public void delete(String id) {
        var contact = contactRepositary.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Contact not found with given id " + id));
        contactRepositary.delete(contact);
    }
    
    @Override
    public List<Contact> getByUserId(String userId) {
        return contactRepositary.findByUserId(userId);
    }

    @Override
    public Page<Contact> searchByName(String nameKeyword, int size, int page, String sortBy, String order, User user) {
        Sort sort = order.equals("desc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        var pageable = PageRequest.of(page, size, sort);
        return contactRepositary.findByUserAndNameContaining(user,nameKeyword, pageable);
    }

    @Override
    public Page<Contact> searchByEmail(String emailKeyword, int size, int page, String sortBy, String order,
            User user) {
        Sort sort = order.equals("desc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        var pageable = PageRequest.of(page, size, sort);
        return contactRepositary.findByUserAndEmailContaining(user,emailKeyword, pageable);
    }

    @Override
    public Page<Contact> searchByPhoneNumber(String phoneNumberKeyword, int size, int page, String sortBy, String order,
            User user) {
        Sort sort = order.equals("desc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        var pageable = PageRequest.of(page, size, sort);
        return contactRepositary.findByUserAndPhoneNumberContaining(user,phoneNumberKeyword, pageable);
    }


    @Override
    public Page<Contact> getByUser(User user, int page, int size, String sortBy, String direction) {

        Sort sort = direction.equals("desc") ? Sort.by(sortBy).descending(): Sort.by(sortBy).ascending();

        var pagaeble = PageRequest.of(page, size, sort);

        return contactRepositary.findByUser(user, pagaeble);
    }
    
}

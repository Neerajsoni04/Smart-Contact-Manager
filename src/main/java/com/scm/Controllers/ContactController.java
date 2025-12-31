package com.scm.Controllers;

import java.util.UUID;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.scm.Entity.Contact;
import com.scm.Entity.User;
import com.scm.Forms.ContactForm;
import com.scm.Forms.ContactSearchForm;
import com.scm.Helper.AppConstants;
import com.scm.Helper.Helper;
import com.scm.Helper.Message;
import com.scm.Helper.MessageType;
import com.scm.Service.ContactService;
import com.scm.Service.ImageService;
import com.scm.Service.UserServices;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;


@Controller
@RequestMapping("/user/contacts")
public class ContactController {

    private Logger logger = org.slf4j.LoggerFactory.getLogger(ContactController.class);
    
    @Autowired
    private UserServices userService;

    @Autowired
    private ContactService contactService;

    @Autowired
    private ImageService imageService;

    
    @RequestMapping("/add")
    public String addContactView(Model model){
        ContactForm contactForm = new ContactForm();
        contactForm.setFavorite(true);
        model.addAttribute(contactForm);
        return "/user/add_contact";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String saveContact(@Valid @ModelAttribute ContactForm contactForm, BindingResult result,
            Authentication authentication, HttpSession session) {

        // process the form data

        // 1 validate form

        if (result.hasErrors()) {

            result.getAllErrors().forEach(error -> logger.info(error.toString()));

            session.setAttribute("message", Message.builder()
                    .content("Please correct the following errors")
                    .type(MessageType.red)
                    .build());
            return "user/add_contact";
        }

        String username = Helper.getEmailOfLoggedInUser(authentication);
        // form ---> contact

        User user = userService.getUserByEmail(username);
        // 2 process the contact picture

        // image process

        // uplod karne ka code
        Contact contact = new Contact();
        contact.setName(contactForm.getName());
        contact.setFavorite(contactForm.isFavorite());
        contact.setEmail(contactForm.getEmail());
        contact.setPhoneNumber(contactForm.getPhoneNumber());
        contact.setAddress(contactForm.getAddress());
        contact.setDescription(contactForm.getDescription());
        contact.setUser(user);
        contact.setLinkedInLink(contactForm.getLinkedInLink());
        contact.setWebsiteLink(contactForm.getWebsiteLink());

        if (contactForm.getContactImage() != null && !contactForm.getContactImage().isEmpty()) {
            
            String public_Id = UUID.randomUUID().toString();
            String fileUrl = imageService.uploadImage(contactForm.getContactImage(),public_Id); 

            contact.setPicture(fileUrl);
            contact.setCloudinaryImagePublicId(public_Id);
        }
        
        System.out.println(contactForm);
        contactService.save(contact);

        // 3 set the contact picture url

        // 4 `set message to be displayed on the view

        session.setAttribute("message",
                Message.builder()
                        .content("You have successfully added a new contact")
                        .type(MessageType.green)
                        .build());

        return "redirect:/user/contacts/add";

    }
    
    @RequestMapping
    public String viewContacts(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = AppConstants.PAGE_SIZE + "") int size,
            @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(value = "direction", defaultValue = "asc") String direction, Model model,
            Authentication authentication) {

        // load all the user contacts
        String username = Helper.getEmailOfLoggedInUser(authentication);

        User user = userService.getUserByEmail(username);

        Page<Contact> pageContact = contactService.getByUser(user, page, size, sortBy, direction);

        model.addAttribute("pageContact", pageContact);
        model.addAttribute("pageSize", AppConstants.PAGE_SIZE);

        model.addAttribute("contactSearchForm", new ContactSearchForm());

        return "user/contacts";
    }

    @RequestMapping("/search")
    public String searchHandler(
        @ModelAttribute ContactSearchForm contactSearchForm,
        @RequestParam(value = "size", defaultValue = AppConstants.PAGE_SIZE + "") int size,
        @RequestParam(value = "page", defaultValue = "0") int page,
        @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
        @RequestParam(value = "direction", defaultValue = "asc") String direction,
        Model model,
        Authentication authentication) {

        var user = userService.getUserByEmail(Helper.getEmailOfLoggedInUser(authentication));

        Page<Contact> pageContact = null;
        if (contactSearchForm.getField().equalsIgnoreCase("name")) {
            pageContact = contactService.searchByName(contactSearchForm.getValue(), size, page, sortBy, direction,user);
        }else if (contactSearchForm.getField().equalsIgnoreCase("email")) {
            pageContact = contactService.searchByEmail(contactSearchForm.getValue(),size,page,sortBy,direction,user);
        }else {
            pageContact = contactService.searchByPhoneNumber(contactSearchForm.getValue(), size, page, sortBy, direction, user);
        }

        
        model.addAttribute("contactSearchForm", contactSearchForm);

        model.addAttribute("pageContact", pageContact);

        model.addAttribute("pageSize", AppConstants.PAGE_SIZE);


        return "user/search";
    }
    
    @RequestMapping("/delete/{id}")
    public String requestMethodName(@PathVariable String id,HttpSession session) {
        contactService.delete(id);

        session.setAttribute("message",
            Message
            .builder()
            .content("Contact Deleted Successfully")
            .type(MessageType.green)
            .build());
        return "redirect:/user/contacts";
    }
    
    @RequestMapping("/view/{id}")
    public String updateContactFormView(@PathVariable String id, Model model){

        var contact = contactService.getById(id);

        ContactForm contactForm = new ContactForm();
        contactForm.setName(contact.getName());
        contactForm.setEmail(contact.getEmail());
        contactForm.setAddress(contact.getAddress());
        contactForm.setDescription(contact.getDescription());
        contactForm.setFavorite(contact.isFavorite());
        contactForm.setPhoneNumber(contact.getPhoneNumber());
        contactForm.setPicture(contact.getPicture());
        contactForm.setWebsiteLink(contact.getWebsiteLink());
        contactForm.setLinkedInLink(contact.getLinkedInLink());

        model.addAttribute("contactForm", contactForm);
        model.addAttribute("contactId", id);

        return "user/update_contact_view";
    }
    
    @RequestMapping(value = "/view/{contactId}", method=RequestMethod.POST)
    public String updateContact(@PathVariable String contactId ,
        @Valid @ModelAttribute ContactForm contactForm,
        BindingResult bindingResult, HttpSession session) {
        
        if (bindingResult.hasErrors()) {
            return "user/update_contact_view";
        }

        var contact = contactService.getById(contactId);
        contact.setId(contactId);
        contact.setName(contactForm.getName());
        contact.setEmail(contactForm.getEmail());
        contact.setPhoneNumber(contactForm.getPhoneNumber());
        contact.setAddress(contactForm.getAddress());
        contact.setFavorite(contactForm.isFavorite());
        contact.setDescription(contactForm.getDescription());
        contact.setLinkedInLink(contactForm.getLinkedInLink());
        contact.setWebsiteLink(contactForm.getWebsiteLink());

        // Process Image 

        if (contactForm.getContactImage() != null && !contactForm.getContactImage().isEmpty()) {
            
            String fileName = UUID.randomUUID().toString();
            String imageUrl = imageService.uploadImage(contactForm.getContactImage(), fileName);

            contact.setCloudinaryImagePublicId(fileName);
            contact.setPicture(imageUrl);

            contactForm.setPicture(imageUrl);   
        }

        var updateCon = contactService.update(contact);
        logger.info("updated contact {}", updateCon);

        session.setAttribute("message", Message.builder()
            .content("Contact Updated !").type(MessageType.green).build());
            return "redirect:/user/contacts";
        }
    
    // @RequestMapping("/all")
    // @ResponseBody
    // public List<Contact> viewAllContacts(Model model ,Authentication authentication){
    //     String username = Helper.getEmailOfLoggedInUser(authentication);
    //     User user = userService.getUserByEmail(username);
    //     List<Contact> contacts = contactService.getByUserId(user.getUserId());
    //     model.addAttribute("contacts", contacts);
    //     return contactService.getByUserId(user.getUserId());
    // }

}

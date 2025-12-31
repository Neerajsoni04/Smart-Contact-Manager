package com.scm.Service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    String uploadImage(MultipartFile picture,String filename);
    
    String getURLbypublicId(String publicId);
}

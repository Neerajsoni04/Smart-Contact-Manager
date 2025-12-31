package com.scm.Service.Impl;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import com.scm.Helper.AppConstants;
import com.scm.Service.ImageService;

@Service
public class ImageServiceImpl implements ImageService{

    @Autowired
    private Cloudinary cloudinary;

    @Override
    public String uploadImage(MultipartFile contactImage,String publicId) {
        try {
            byte[] data = new byte[contactImage.getInputStream().available()];
            contactImage.getInputStream().read(data);
            cloudinary.uploader().upload(data, ObjectUtils.asMap(
                "public_id", publicId));
                
            return this.getURLbypublicId(publicId);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String getURLbypublicId(String publicId) {
        return cloudinary
                .url()  // Start generating the image URL
                .transformation( // The final URL returned will include these transformations
                        new Transformation<>()
                                .width(AppConstants.CONTACT_IMAGE_WIDTH)
                                .height(AppConstants.CONTACT_IMAGE_HEIGHT)
                                .crop(AppConstants.CONTACT_IMAGE_CROP))
                .generate(publicId); // Generate the URL based on the publicId
    }
    
}

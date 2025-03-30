package com.example.picturegaller.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.picturegaller.exceptions.EntityNotfoundException;
import com.example.picturegaller.exceptions.FileOperationException;
import com.example.picturegaller.models.Picture;
import com.example.picturegaller.repositories.PictureRepository;
import com.example.picturegaller.utils.S3Utils;

@Service
public class PicturerService {
    

    @Autowired 
    S3Utils s3Operations;

    @Autowired
    PictureRepository pictureRepo;

    public Picture saveImage(MultipartFile file, String description, String name) {
        String objectKey = s3Operations.generateKey(name);
        String imageUrl = s3Operations.uploadToS3(file, objectKey);
        if(imageUrl == null)
            throw  new FileOperationException("couldn't upload images to bucket");
        Picture picture = new Picture(objectKey, description, name, imageUrl);
        pictureRepo.save(picture);
        return  picture;
    }

    public Page<Picture> getImage(Pageable page) {
        return pictureRepo.findAll(page);
    }


    public void deleteImage (Long id) {

        Optional<Picture> pic = pictureRepo.findById(id);

        if(pic.isEmpty()) {
            throw new EntityNotfoundException("Cant find image");
        }
        Boolean deleted = s3Operations.deleteImageFromS3(pic.get().getImageKey());
        if(!deleted) {
            throw new FileOperationException("couldn't delete image");
        }
        pictureRepo.delete(pic.get());
    } 
}

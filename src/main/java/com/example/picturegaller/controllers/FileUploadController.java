package com.example.picturegaller.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.picturegaller.models.Picture;
import com.example.picturegaller.services.PicturerService;

@RestController
@RequestMapping("/api/files")
public class FileUploadController {
    
    @Autowired
    private  PicturerService picturerService;

    @PostMapping("/upload")
    public Picture uploadFile(
            @RequestParam("name") String name,
            @RequestParam("description") String description, 
            @RequestParam("file") MultipartFile file) {
            // Process the file
                String urlCompatibleName = name.replaceAll(" ", "-");
                Picture picture = picturerService.saveImage(file,description, urlCompatibleName);
                System.out.println(picture);
                return  picture;
            }

    @GetMapping("/images")
    public Page<Picture> getImage(@RequestParam(value = "page", defaultValue="0") int page, @RequestParam(value = "size", defaultValue="5") int size) {
        Pageable entry = PageRequest.of(page, size);
        var pageContent = picturerService.getImage(entry);
        return pageContent;
    }

   @DeleteMapping("/images")
    public ResponseEntity<String> deleteMeassge(@RequestParam(value = "id", required = true) Long id) {
        picturerService.deleteImage(id);
        return  ResponseEntity.ok("Deleted");
    }

}
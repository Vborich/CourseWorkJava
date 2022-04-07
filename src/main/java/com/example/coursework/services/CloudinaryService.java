package com.example.coursework.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import org.apache.commons.codec.binary.Base64;

import java.util.Map;

@Service
public class CloudinaryService {

    public String uploadImage(String image) throws IOException {
        Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "ds2evqh9b",
                "api_key", "143232678337968",
                "api_secret", "c6lCsTCgvqnTwgOrRmmIyacZzRk",
                "secure", true));

        image = image.split("base64,")[1];
        File file = new File("image.png");
        byte[] decodedBytes = Base64.decodeBase64(image);
        FileUtils.writeByteArrayToFile(file, decodedBytes);

        Map uploadResult = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
        if (uploadResult.isEmpty())
            return "";
        return uploadResult.get("url").toString();
    }
}

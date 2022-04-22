package com.example.coursework.services;

public class Cloudinary
{
    private static CloudinaryService cloudinaryService;

    private Cloudinary() {}

    public static CloudinaryService getCloudinaryService()
    {
        if (cloudinaryService == null)
            cloudinaryService = new CloudinaryService();
        return cloudinaryService;
    }

}

package edu.au.cc.gallery;

import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;
import spark.Request;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ImageService {
    private static final String IMAGE_S3_BUCKET_NAME = "image-gallery-bucket-lf";
    private Gson gson = new Gson();
    private S3 s3 = new S3();
    private PostgresImageUploadsDAO postgresImageUploadsDAO;

    public ImageService() {
        try {
            postgresImageUploadsDAO = new PostgresImageUploadsDAO();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void upload(File file, String userName) {
        s3.connect();

        s3.putObject(IMAGE_S3_BUCKET_NAME, file);

        recordUploadedImage(userName, file.getName());
    }

    private void recordUploadedImage(String userName, String imageID) {
        ImageUpload imageUpload = new ImageUpload(userName, imageID);

        try {
            postgresImageUploadsDAO.addImage(imageUpload);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String uploadImage(Request request) throws IOException, ServletException {
        String userName = request.params(":username");
        String location = "image";          // the directory location where files will be stored
        long maxFileSize = 100000000;       // the maximum size allowed for uploaded files
        long maxRequestSize = 100000000;    // the maximum size allowed for multipart/form-data requests
        int fileSizeThreshold = 1024;       // the size threshold after which files will be written to disk

        MultipartConfigElement multipartConfigElement = new MultipartConfigElement(
                location, maxFileSize, maxRequestSize, fileSizeThreshold);
        request.raw().setAttribute("org.eclipse.jetty.multipartConfig",
                multipartConfigElement);

        String fName = request.raw().getPart("file").getSubmittedFileName();
        System.out.println("File: " + fName);

        Part uploadedFile = request.raw().getPart("file");
        File file = new File(fName); // TODO UUID
        FileUtils.copyInputStreamToFile(uploadedFile.getInputStream(), file);

        upload(file, userName);

        return "OK";
    }

    public String getImagesForUser(String userName) {
        s3.connect();

        List<String> objectURLsForBucket = s3.getObjectURLsForBucketAndUser(IMAGE_S3_BUCKET_NAME, userName);

        return gson.toJson(objectURLsForBucket);
    }

    public String deleteImage(String imageID) {
        s3.connect();

        s3.deleteImage(IMAGE_S3_BUCKET_NAME, imageID);

        return "OK";
    }
}


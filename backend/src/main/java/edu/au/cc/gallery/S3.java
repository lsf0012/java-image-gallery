package edu.au.cc.gallery;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.core.sync.RequestBody;

import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

public class S3 {
    private static final Region region = Region.US_EAST_2;
    private S3Client client;
    private PostgresImageUploadsDAO postgresImageUploadsDAO;

    public S3() {
        try {
            postgresImageUploadsDAO = new PostgresImageUploadsDAO();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void connect() {
        client = S3Client.builder().region(region).build();
    }

    public void createBucket(String bucketName) {
        CreateBucketRequest createBucketRequest = CreateBucketRequest
                .builder()
                .bucket(bucketName)
                .createBucketConfiguration(CreateBucketConfiguration.builder()
                        .locationConstraint(region.id())
                        .build())
                .build();
        client.createBucket(createBucketRequest);
    }

    public void putObject(String bucketName, String key, String value) {
        PutObjectRequest por = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();
        client.putObject(por, RequestBody.fromString(value));
    }

    public void putObject(String bucketName, File file) {
        PutObjectRequest por = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(file.getName())
                .build();
        client.putObject(por, file.toPath());
    }

    public List<String> getObjectURLsForBucketAndUser(String bucketName, String userName) {
        Set<String> imagesForUser = new HashSet<>(postgresImageUploadsDAO.getImagesForUser(userName));

        ListObjectsRequest lor = ListObjectsRequest
                .builder()
                .bucket(bucketName)
                .build();
        ListObjectsResponse listObjectsResponse = client.listObjects(lor);

        return listObjectsResponse
                .contents()
                .stream()
                .filter(s3Object -> imagesForUser.contains(s3Object.key()))
                .map(s3Object -> {
                    GetUrlRequest gur = GetUrlRequest
                            .builder()
                            .bucket(bucketName)
                            .key(s3Object.key())
                            .build();
                    URL url = client.utilities().getUrl(gur);
                    return url.toExternalForm();
                })
                .collect(toList());
    }

    public void deleteImage(String bucketName, String imageID) {
        DeleteObjectRequest dor = DeleteObjectRequest
                .builder()
                .bucket(bucketName)
                .key(imageID)
                .build();

        client.deleteObject(dor);

        // TODO delete from image uploads table
    }

    public static void demo() {
        String bucketName = "edu.aa.cc.image-gallery";
        S3 s3 = new S3();
        s3.connect();
        //s3.createBucket(bucketName);
        s3.putObject(bucketName, "banana", "yello");
    }
}


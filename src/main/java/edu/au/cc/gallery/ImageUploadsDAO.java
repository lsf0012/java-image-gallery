package edu.au.cc.gallery;

import java.sql.SQLException;
import java.util.List;

public interface ImageUploadsDAO {
    public void addImage(ImageUpload imageUpload) throws SQLException;
    public List<String> getImagesForUser(String userName);
}


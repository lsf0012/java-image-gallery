package edu.au.cc.gallery;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PostgresImageUploadsDAO implements ImageUploadsDAO {
    private DB connection;

    public PostgresImageUploadsDAO() throws SQLException {
        connection = new DB();
        connection.connect();
    }

    public void addImage(ImageUpload imageUpload) throws SQLException {
        connection.execute("insert into image_uploads(username,imageid) values (?,?)",
                new String[]{imageUpload.getUserName(), imageUpload.getImageID()});
    }

    public List<String> getImagesForUser(String userName) {
        List<String> result = new ArrayList<>();
        try {
            ResultSet rs = connection.executeQuery("select imageid from image_uploads where username = ?",
                    new String[]{userName});
            while (rs.next()) {
                result.add(rs.getString(1));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}


package edu.au.cc.gallery;

import java.util.List;

//Data Access Object
public interface UserDAO {
    
    //return list of users (maybe empty)1
    List<User> getUsers() throws Exception;

    //returns user with username or null if no user
    User getUserByUsername(String username) throws Exception;

    //Add user to database
    void addUser(User u) throws Exception;

    //Delete user from database
    void deleteUser(User u) throws Exception;
    
}

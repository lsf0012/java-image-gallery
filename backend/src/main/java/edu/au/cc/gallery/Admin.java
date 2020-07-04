package edu.au.cc.gallery;

import static spark.Spark.*;

import spark.Request;
import spark.Response;

import java.util.Map;
import java.util.HashMap;

import spark.ModelAndView;

import spark.template.handlebars.HandlebarsTemplateEngine;
import com.google.gson.Gson;

//import edu.au.cc.gallery.data.UserDAO;
//import edu.au.cc.gallery.data.Postgres;

public class Admin {
    private Gson gson = new Gson();

    private static UserDAO getUserDAO() throws Exception {
        return Postgres.getUserDAO();
    }

    private String deleteUser(Request req, Response resp) {
        Map<String, Object> model = new HashMap<>();
        model.put("title", "Delete User");
        model.put("message", "Are you sure that you want to delete this user?");
        model.put("onYes", "/admin/deleteUserExec/" + req.params(":username"));
        model.put("onNo", "/admin/users");
        return new HandlebarsTemplateEngine()
                .render(new ModelAndView(model, "confirm.hbs"));
    }

    private String deleteUserExec(Request req, Response resp) {
        try {
            UserDAO userDAO = getUserDAO();

            String userName = req.params(":username");
            User user = userDAO.getUserByUsername(userName);

            userDAO.deleteUser(user);
            resp.redirect("/admin/users");
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
        return null;
    }

    private String listUsers() {
        try {
            UserDAO dao = Postgres.getUserDAO();
            return gson.toJson(dao.getUsers());
        } catch (Exception e) {
            return null;
        }
    }

    private String sessionDemo(Request req, Response resp) {
        if (req.session().isNew()) {
            req.session().attribute("owner", "fred");
            req.session().attribute("foo", "bar");
        } else {
        }
        return "stored";
    }

    private String debugSession(Request req, Response resp) {
        StringBuffer sb = new StringBuffer();
        for (String key : req.session().attributes()) {
            sb.append(key + "->" + req.session().attribute(key) + "<br />");
        }
        return sb.toString();
    }


    public void addRoutes() {
        get("/admin/users", (req, res) -> listUsers());
        get("/admin/deleteUser/:username", (req, res) -> deleteUser(req, res));
        get("/admin/deleteUserExec/:username", (req, res) -> deleteUserExec(req, res));
        get("/sessionDemo", (req, res) -> sessionDemo(req, res));
        get("/debugSession", (req, res) -> debugSession(req, res));
    }
}


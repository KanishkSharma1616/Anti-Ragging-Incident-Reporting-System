package com.antiragging.service;

import com.antiragging.dao.UserDAO;
import com.antiragging.model.User;
import com.antiragging.util.SessionManager;

import java.sql.SQLException;

/**
 * Service Layer for User Authentication.
 * Author: Kanishk Sharma (PIET25CR023)
 */
public class LoginService {

    private final UserDAO userDAO = new UserDAO();

    public User login(String username, String password) throws SQLException {
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            return null;
        }

        User user = userDAO.authenticate(username.trim(), password.trim());
        if (user != null) {
            SessionManager.setCurrentUser(user);
        }
        return user;
    }

    public boolean changePassword(String currentPassword, String newPassword) throws SQLException {
        if (!SessionManager.isLoggedIn()) return false;
        int userId = SessionManager.getCurrentUserId();
        return userDAO.changePassword(userId, currentPassword, newPassword);
    }

    public void logout() {
        SessionManager.clearSession();
    }
}

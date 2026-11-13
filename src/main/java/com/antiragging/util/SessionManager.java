package com.antiragging.util;

import com.antiragging.model.User;

/**
 * Global Session Manager tracking the currently logged-in user.
 * Author: Kanishk Sharma (PIET25CR023)
 */
public class SessionManager {

    private static User currentUser = null;

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static int getCurrentUserId() {
        return currentUser != null ? currentUser.getUserId() : -1;
    }

    public static String getCurrentUsername() {
        return currentUser != null ? currentUser.getUsername() : "";
    }

    public static String getCurrentRole() {
        return currentUser != null ? currentUser.getRole() : "";
    }

    public static String getCurrentStudentId() {
        return currentUser != null ? currentUser.getStudentId() : "";
    }

    public static boolean isAdmin() {
        return currentUser != null && currentUser.isAdmin();
    }

    public static boolean isLoggedIn() {
        return currentUser != null;
    }

    public static void clearSession() {
        currentUser = null;
    }
}

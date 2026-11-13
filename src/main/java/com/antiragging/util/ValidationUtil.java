package com.antiragging.util;

import java.util.regex.Pattern;

/**
 * Input Data Validation Utility.
 * Author: Kanishk Sharma (PIET25CR023)
 */
public class ValidationUtil {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[0-9]{10}$");
    private static final Pattern DATE_PATTERN = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}$");

    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    public static boolean isValidEmail(String email) {
        if (isEmpty(email)) return false;
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    public static boolean isValidPhone(String phone) {
        if (isEmpty(phone)) return false;
        return PHONE_PATTERN.matcher(phone.trim()).matches();
    }

    public static boolean isValidDate(String dateStr) {
        if (isEmpty(dateStr)) return false;
        return DATE_PATTERN.matcher(dateStr.trim()).matches();
    }

    public static boolean isValidStudentId(String studentId) {
        return !isEmpty(studentId) && studentId.trim().length() >= 3;
    }
}

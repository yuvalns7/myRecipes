package com.example.myrecipes.utils;

public class StringUtils {
    public static boolean isBlank(String str) {
        if (str != null && !str.isEmpty() && !str.trim().isEmpty()) {
            return true;
        }
        return false;
    }
}

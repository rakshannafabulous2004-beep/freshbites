package com.freshbite.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AdminConfig {

    @Value("${freshbite.admin.email}")
    private String adminEmail;

    @Value("${freshbite.admin.password}")
    private String adminPassword;

    public String getAdminEmail() {
        return adminEmail;
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    public boolean isAdminEmail(String email) {
        return adminEmail.equalsIgnoreCase(email);
    }
}

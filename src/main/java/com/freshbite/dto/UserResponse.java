package com.freshbite.dto;

public class UserResponse {

    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private boolean admin;

    public UserResponse() {
    }

    public UserResponse(Long id, String fullName, String email, String phone, boolean admin) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.admin = admin;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
}

package com.example.taserfan.API;

public class AuthenticatonData {
    private String email;
    private String passwd;

    public AuthenticatonData(String email, String password) {
        this.email = email;
        this.passwd = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return passwd;
    }

    public void setPassword(String password) {
        this.passwd = password;
    }
}

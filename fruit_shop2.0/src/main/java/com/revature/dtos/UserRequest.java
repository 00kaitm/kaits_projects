package com.revature.dtos;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class UserRequest {

    @NotBlank
    private String username;

    @NotBlank
    @Size(min = 8, message = "must be at least 8 characters")
    private String password;

    public UserRequest() {
    }

    public UserRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
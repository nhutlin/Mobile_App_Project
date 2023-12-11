package com.example.uit_project.model;

import com.google.gson.annotations.SerializedName;

public class User {
    public String realm;
    @SerializedName("id")
    public String userID;
    public String firstName;
    public String lastName;
    public String email;
    public boolean enabled;
    public long createdOn;
    public boolean serviceAccount;
    @SerializedName("username")
    public String userName;

}

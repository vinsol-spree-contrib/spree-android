package com.vinsol.spree.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by vaibhav on 11/27/15.
 */
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("full_name")
    @Expose
    private String name;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("spree_api_key")
    @Expose
    private String apiKey;

    @SerializedName("confirmed")
    @Expose
    private boolean confirmed;

    @SerializedName("password")
    @Expose
    private String password;

    @SerializedName("provider")
    @Expose
    private String provider;

    @SerializedName("phone")
    @Expose
    private String phone;

    @SerializedName("uid")
    @Expose
    private String uid;

    private String profilePicUrl;

    @SerializedName("authentications_attributes")
    @Expose
    private ArrayList<SocialProfile> requestProfiles;

    @SerializedName("authentications")
    @Expose
    private ArrayList<SocialProfile> profiles;

    public User() {
        initObject();
    }

    public User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    private void initObject() {
        if (requestProfiles==null) requestProfiles = new ArrayList<>();
    }

    public User(String email, String name, SocialProfile profile) {
        initObject();
        this.email = email;
        requestProfiles.add(profile);
        this.name = name;
        this.uid = profile.getUid();
        this.provider = profile.getProvider();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }

    public ArrayList<SocialProfile> getProfiles() {
        return profiles;
    }

    public void setProfiles(ArrayList<SocialProfile> profiles) {
        this.profiles = profiles;
    }

    public boolean hasProfiles() {
        if (profiles!=null && !profiles.isEmpty()) return true;
        return false;
    }

    public ArrayList<SocialProfile> getRequestProfiles() {
        return requestProfiles;
    }

    public void setRequestProfiles(ArrayList<SocialProfile> requestProfiles) {
        this.requestProfiles = requestProfiles;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}

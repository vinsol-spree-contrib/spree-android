package com.vinsol.spree.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Review implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("name")
    @Expose
    private String userName;

    @SerializedName("rating")
    @Expose
    private String rating;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("review")
    @Expose
    private String review;

    @SerializedName("user_id")
    @Expose
    private String userId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName == null ? "" : userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public float getRating() {
        if(rating == null) {
            return 0;
        }

        try {
            return Float.parseFloat(rating);
        } catch (NumberFormatException nfe) {
            return 0;
        }
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getTitle() {
        return title == null ? "" : title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReview() {
        return review == null ? "" : review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getUserId() {
        return userId == null ? "" : userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}

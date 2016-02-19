package com.vinsol.spree.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vinsol.spree.models.Review;


import java.io.Serializable;

/**
 * Created by vaibhav on 12/24/15.
 */
public class ReviewWrapper implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("review")
    @Expose
    private Review review;

    public ReviewWrapper(Review review) {
        this.review = review;
    }

    public Review getReview() {
        return review;
    }

    public void setReview(Review review) {
        this.review = review;
    }
}

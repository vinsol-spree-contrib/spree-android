package com.vinsol.spree.models;

import com.google.gson.annotations.SerializedName;

public class ReviewsMeta {
    private String avg_rating;

    @SerializedName("ratings_distribution")
    private RatingsDistribution ratingsDistribution;

    @SerializedName("reviews_with_content_count")
    private String reviewsWithContentCount;

    @SerializedName("reviews_count")
    private String reviewsCount;

    public float getAvg_rating() {
        if (avg_rating == null) {
            return 0;
        }

        try {
            return Float.parseFloat(avg_rating);
        } catch (NumberFormatException nfe) {
            return 0;
        }
    }

    public void setAvg_rating(String avg_rating) {
        this.avg_rating = avg_rating;
    }

    public RatingsDistribution getRatingsDistribution() {
        return ratingsDistribution;
    }

    public void setRatingsDistribution(RatingsDistribution ratingsDistribution) {
        this.ratingsDistribution = ratingsDistribution;
    }

    public String getReviewsWithContentCount() {
        return reviewsWithContentCount;
    }

    public void setReviewsWithContentCount(String reviewsWithContentCount) {
        this.reviewsWithContentCount = reviewsWithContentCount;
    }

    public String getReviewsCount() {
        return reviewsCount;
    }

    public int getReviews_countInt() {
        if(reviewsCount == null) {
            return 0;
        }

        try {
            return Integer.parseInt(reviewsCount);
        } catch (NumberFormatException nfe) {
            return 0;
        }
    }

    public void setReviewsCount(String reviewsCount) {
        this.reviewsCount = reviewsCount;
    }
}
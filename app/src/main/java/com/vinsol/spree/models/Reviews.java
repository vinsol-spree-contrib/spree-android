package com.vinsol.spree.models;

import java.util.ArrayList;

public class Reviews {
    private ArrayList<Review> reviews;

    private ReviewsMeta meta;

    public ArrayList<Review> getReviews() {
        ArrayList<Review> reviewsWithActualReview = new ArrayList<>();
        for (Review review : reviews) {
            if (review.getUserName().equals("") || (review.getTitle().equals("") && review.getReview().equals(""))) {
                // don't add this
            } else {
                reviewsWithActualReview.add(review);
            }
        }
        return reviewsWithActualReview;
    }

    public void setReviews(ArrayList<Review> reviews) {
        this.reviews = reviews;
    }

    public ReviewsMeta getMeta() {
        return meta;
    }

    public void setMeta(ReviewsMeta meta) {
        this.meta = meta;
    }
}
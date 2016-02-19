package com.vinsol.spree.viewhelpers;

import android.view.View;
import android.widget.TextView;

import com.vinsol.spree.R;
import com.vinsol.spree.models.Review;

public class ReviewListItemViewHolder {
    private TextView userName, title, reviewText;
    private View starRatings;

    public ReviewListItemViewHolder(View view) {
        userName    = (TextView)  view.findViewById(R.id.reviews_list_item_user_name);
        title       = (TextView)  view.findViewById(R.id.review_list_item_review_title);
        reviewText  = (TextView)  view.findViewById(R.id.review_list_item_review);
        starRatings =             view.findViewById(R.id.review_list_item_rating_stars);

    }

    public void setup(final Review review) {
        userName.setText("by " + review.getUserName());
        title.setText(review.getTitle());
        reviewText.setText(review.getReview());

        new RatingStarsHelper(reviewText.getContext(), starRatings).setRating(review.getRating());
    }
}

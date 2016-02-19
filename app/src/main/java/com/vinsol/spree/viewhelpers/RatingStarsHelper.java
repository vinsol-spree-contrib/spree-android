package com.vinsol.spree.viewhelpers;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;

import com.vinsol.spree.R;

public class RatingStarsHelper {

    private ImageView ratingStar1;
    private ImageView ratingStar2;
    private ImageView ratingStar3;
    private ImageView ratingStar4;
    private ImageView ratingStar5;

    private Drawable blankStar;
    private Drawable halfFilledStar;
    private Drawable fullFilledStar;

    public RatingStarsHelper(Context context, View parentView) {

        blankStar      = ContextCompat.getDrawable(context, R.drawable.rating_icon);
        halfFilledStar = ContextCompat.getDrawable(context, R.drawable.half_filled_star);
        fullFilledStar = ContextCompat.getDrawable(context, R.drawable.full_filled_star);

        ratingStar1 = (ImageView) parentView.findViewById(R.id.rating_img1);
        ratingStar2 = (ImageView) parentView.findViewById(R.id.rating_img2);
        ratingStar3 = (ImageView) parentView.findViewById(R.id.rating_img3);
        ratingStar4 = (ImageView) parentView.findViewById(R.id.rating_img4);
        ratingStar5 = (ImageView) parentView.findViewById(R.id.rating_img5);
    }

    public void setRating(float averageRating) {
        // first set all ratings to blank
        ratingStar1.setImageDrawable(blankStar);
        ratingStar2.setImageDrawable(blankStar);
        ratingStar3.setImageDrawable(blankStar);
        ratingStar4.setImageDrawable(blankStar);
        ratingStar5.setImageDrawable(blankStar);

        if(averageRating >= .5) {
            ratingStar1.setImageDrawable(halfFilledStar);
        }

        if(averageRating >= 1) {
            ratingStar1.setImageDrawable(fullFilledStar);
        }

        if(averageRating >= 1.5) {
            ratingStar2.setImageDrawable(halfFilledStar);
        }

        if(averageRating >= 2) {
            ratingStar2.setImageDrawable(fullFilledStar);
        }

        if(averageRating >= 2.5) {
            ratingStar3.setImageDrawable(halfFilledStar);
        }

        if(averageRating >= 3) {
            ratingStar3.setImageDrawable(fullFilledStar);
        }

        if(averageRating >= 3.5) {
            ratingStar4.setImageDrawable(halfFilledStar);
        }

        if(averageRating >= 4) {
            ratingStar4.setImageDrawable(fullFilledStar);
        }

        if(averageRating >= 4.5) {
            ratingStar5.setImageDrawable(halfFilledStar);
        }

        if(averageRating >= 5) {
            ratingStar5.setImageDrawable(fullFilledStar);
        }
    }
}

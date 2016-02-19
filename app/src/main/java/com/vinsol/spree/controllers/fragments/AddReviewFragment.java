package com.vinsol.spree.controllers.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.balysv.materialmenu.MaterialMenuDrawable;
import com.balysv.materialmenu.MaterialMenuView;
import com.vinsol.spree.R;
import com.vinsol.spree.api.ApiClient;
import com.vinsol.spree.api.models.ReviewWrapper;
import com.vinsol.spree.controllers.Home;
import com.vinsol.spree.models.Review;
import com.vinsol.spree.models.User;
import com.vinsol.spree.utils.BusProvider;
import com.vinsol.spree.utils.Log;
import com.vinsol.spree.utils.SharedPreferencesHelper;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class AddReviewFragment extends BaseFragment {
    private static final String TAG = "AddReview";
    private static final String PRODUCT_ID = "product_id";
    private static final String PRODUCT_NAME = "product_name";
    private static final String PRODUCT_PRICE = "product_price";


    private int productId;
    private String productName;
    private String productPrice;

    private Home home;
    private MaterialMenuView back;
    private RelativeLayout pbContainer;

    private TextView productNameTextView;
    private TextView productPriceTextView;
    private EditText titleEditText;
    private EditText messageEditText;
    private RatingBar ratingBar;
    private ImageButton saveReviewButton;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ReviewFragment.
     */
    public static AddReviewFragment newInstance(int productId, String productName, String productPrice) {
        AddReviewFragment fragment = new AddReviewFragment();
        Bundle args = new Bundle();
        args.putInt(PRODUCT_ID, productId);
        args.putString(PRODUCT_NAME, productName);
        args.putString(PRODUCT_PRICE, productPrice);
        fragment.setArguments(args);
        return fragment;
    }

    public AddReviewFragment() {
        // Required empty public constructor
    }

    @Override
    public String getTagText() {
        return TAG;
    }

    @Override
    public boolean onBackPressed() {
        Log.d("AddReviewFragment : onBackPressed");
        home.popBackStack();
        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments()!=null) {
            productId = getArguments().getInt(PRODUCT_ID);
            productName = getArguments().getString(PRODUCT_NAME);
            productPrice = getArguments().getString(PRODUCT_PRICE);
        }
        home = (Home) getActivity();
        BusProvider.getInstance().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_review, container, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BusProvider.getInstance().unregister(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI(view);
        setListeners();
    }

    private void initUI(View view) {
       // common
        pbContainer             = (RelativeLayout)      view.findViewById(R.id.progress_bar_container);
        back                    = (MaterialMenuView)    view.findViewById(R.id.add_review_tab_bar_back_img);
        productNameTextView     = (TextView)            view.findViewById(R.id.add_review_item_name);
        productPriceTextView    = (TextView)            view.findViewById(R.id.add_review_item_price);
        titleEditText           = (EditText)            view.findViewById(R.id.fragment_add_review_title_edit_text);
        messageEditText         = (EditText)            view.findViewById(R.id.add_review_fragment_message_edit_text);
        ratingBar               = (RatingBar)            view.findViewById(R.id.add_review_rating_bar);
        saveReviewButton        = (ImageButton)          view.findViewById(R.id.add_review_fragment_save_review_button);

        back.setState(MaterialMenuDrawable.IconState.ARROW);

        // product name
        productNameTextView.setText(productName);

        // product price
        productPriceTextView.setText(productPrice);

    }

    private void setListeners() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        pbContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Do nothing
            }
        });

        saveReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveReview();

            }
        });
    }

    private void handleError(String msg) {
        Toast.makeText(home, msg, Toast.LENGTH_LONG).show();
    }

    private void showLoader() {
        pbContainer.setVisibility(View.VISIBLE);
    }

    private void hideLoader() {
        pbContainer.setVisibility(View.GONE);
    }

    private void saveReview() {
        int rating = (int) ratingBar.getRating();
        if(rating == 0) {
            Toast.makeText(getContext(), "Please provide your rating", Toast.LENGTH_LONG).show();

            return;
        }

        User user = SharedPreferencesHelper.getCache().getUser();
        if(user == null) {
            Toast.makeText(getContext(), "You need to be logged in to view the reviews", Toast.LENGTH_SHORT).show();
            return;
        }
        Review reviewToBeAdded = new Review();
        reviewToBeAdded.setRating(String.valueOf(rating));
        if (!messageEditText.getText().toString().trim().isEmpty()) {
            if (titleEditText.getText().toString().trim().isEmpty()) {
                Toast.makeText(getContext(), "Please provide a title", Toast.LENGTH_LONG).show();
                return;
            }
            else {
                reviewToBeAdded.setReview(messageEditText.getText().toString());
                reviewToBeAdded.setTitle(titleEditText.getText().toString());
                reviewToBeAdded.setUserName(user.getName());
            }
        }
        ReviewWrapper reviewWrapper = new ReviewWrapper(reviewToBeAdded);
        showLoader();
        Call<Review> call = ApiClient.getInstance().getApiService().addReviewForProduct(productId, user.getApiKey(), reviewWrapper);
        call.enqueue(new Callback<Review>() {
            @Override
            public void onResponse(Response<Review> response, Retrofit retrofit) {
                hideLoader();
                if (response.isSuccess()) {
                    Toast.makeText(home, "Thanks You!", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }
                else {
                    // TODO : handle 422 cases if any
                }
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoader();
                Log.d("AddReviewFragment > addReviewForProduct > onFailure");
            }
        });
    }
}

package com.vinsol.spree.controllers.fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.balysv.materialmenu.MaterialMenuDrawable;
import com.balysv.materialmenu.MaterialMenuView;
import com.vinsol.spree.R;
import com.vinsol.spree.api.ApiClient;
import com.vinsol.spree.controllers.Home;
import com.vinsol.spree.models.Review;
import com.vinsol.spree.models.Reviews;
import com.vinsol.spree.models.ReviewsMeta;
import com.vinsol.spree.utils.BusProvider;
import com.vinsol.spree.utils.Log;
import com.vinsol.spree.viewhelpers.RatingStarsHelper;
import com.vinsol.spree.viewhelpers.ReviewsCustomAdapter;

import java.util.ArrayList;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class ReviewsFragment extends BaseFragment {
    private static final String TAG = "Reviews";
    private static final String PRODUCT_ID = "product_id";
    private static final String PRODUCT_NAME = "product_name";
    private static final String PRODUCT_PRICE = "product_price";


    private int productId;
    private String productName;
    private String productPrice;

    private Home home;
    private MaterialMenuView back;
    private LinearLayout noReviewsFoundContainer;
    private FloatingActionButton addReviewBtn;
    private ListView reviewsListView;
    private Reviews reviews;
    private RelativeLayout pbContainer;
    private ReviewsCustomAdapter adapter;

    private LayoutInflater inflater;
    private View reviewListHeaderView;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ReviewFragment.
     */
    public static ReviewsFragment newInstance(int productId, String productName, String productPrice) {
        ReviewsFragment fragment = new ReviewsFragment();
        Bundle args = new Bundle();
        args.putInt(PRODUCT_ID, productId);
        args.putString(PRODUCT_NAME, productName);
        args.putString(PRODUCT_PRICE, productPrice);
        fragment.setArguments(args);
        return fragment;
    }

    public ReviewsFragment() {
        // Required empty public constructor
    }

    @Override
    public String getTagText() {
        return TAG;
    }

    @Override
    public boolean onBackPressed() {
        Log.d("ReviewsFragment : onBackPressed");
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
        this.inflater = inflater;

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reviews, container, false);
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

        reviewListHeaderView = inflater.inflate(R.layout.reviews_list_header, reviewsListView, false);
        reviewsListView.addHeaderView(reviewListHeaderView);
        adapter = new ReviewsCustomAdapter(home, R.layout.review_list_item, new ArrayList<Review>());
        reviewsListView.setAdapter(adapter);

        if (reviews == null) {
            loadReviews();
        } else {
            setupReviews();
        }

    }

    private void initUI(View view) {
       // common
        pbContainer             = (RelativeLayout)      view.findViewById(R.id.progress_bar_container);
        back                    = (MaterialMenuView)    view.findViewById(R.id.fragment_reviews_tab_bar_back_img);
        noReviewsFoundContainer = (LinearLayout)        view.findViewById(R.id.fragment_reviews_blank_container);
        addReviewBtn            = (FloatingActionButton)view.findViewById(R.id.fragment_reviews_fab);
        reviewsListView         = (ListView)            view.findViewById(R.id.fragment_reviews_list_view);
        back.setState(MaterialMenuDrawable.IconState.ARROW);
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

        addReviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                home.showAddReviewFragment(productId, productName, productPrice);
            }
        });
    }

    private void loadReviews() {
        showLoader();
        Call call = ApiClient.getInstance().getApiService().getProductReviews(productId);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Response response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    hideLoader();
                    reviews = (Reviews) response.body();
                    setupReviews();
                } else {
                    hideLoader();
                    handleError(response.message());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoader();
                handleError(t.getLocalizedMessage());
            }
        });
    }

    private void setupReviews() {
        if (reviews.getReviews().isEmpty()) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) addReviewBtn.getLayoutParams();
            params.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            params.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            params.setMargins(0, (int) home.getResources().getDimension(R.dimen.margin_10dp), 0, 0);
            params.addRule(RelativeLayout.BELOW, R.id.fragment_reviews_blank_container);
            params.addRule(RelativeLayout.CENTER_HORIZONTAL);
            addReviewBtn.setLayoutParams(params);
            addReviewBtn.setVisibility(View.VISIBLE);
            reviewsListView.setVisibility(View.GONE);
            noReviewsFoundContainer.setVisibility(View.VISIBLE);
        } else {
            reviewsListView.setVisibility(View.VISIBLE);

            setUpReviewListHeader(reviewListHeaderView, reviews.getMeta());
            adapter.clear();
            adapter.addAll(reviews.getReviews());
            adapter.notifyDataSetChanged();

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) addReviewBtn.getLayoutParams();
            params.removeRule(RelativeLayout.BELOW);
            params.removeRule(RelativeLayout.CENTER_HORIZONTAL);
            params.setMargins(0, 0, (int) home.getResources().getDimension(R.dimen.margin_15dp), (int) home.getResources().getDimension(R.dimen.margin_15dp));
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            addReviewBtn.setLayoutParams(params);
            addReviewBtn.setVisibility(View.VISIBLE);
        }
    }

    private void setUpReviewListHeader(View listHeader, ReviewsMeta meta) {
        // product name
        TextView productNameTextView = (TextView) listHeader.findViewById(R.id.reviews_list_header_item_name);
        productNameTextView.setText(productName);

        // product price
        TextView productPriceTextView = (TextView) listHeader.findViewById(R.id.reviews_list_header_item_price);
        productPriceTextView.setText(productPrice);

        // rating
        new RatingStarsHelper(getActivity(), listHeader.findViewById(R.id.review_list_header_rating_stars)).setRating(meta.getAvg_rating());

        // total rating count
        TextView totalRatingCount = (TextView) listHeader.findViewById(R.id.review_list_header_rating_count);
        totalRatingCount.setText("(" + meta.getReviewsCount() + ")");

        // setUp rating bars: 5 stars
        View ratingBar5 = listHeader.findViewById(R.id.review_list_header_rating_bar_for_5_rating);
        setUpRatingBar(ratingBar5, "5", meta.getRatingsDistribution().getFive(), meta.getReviews_countInt());

        // setUp rating bars: 4 stars
        View ratingBar4 = listHeader.findViewById(R.id.review_list_header_rating_bar_for_4_rating);
        setUpRatingBar(ratingBar4, "4", meta.getRatingsDistribution().getFour(), meta.getReviews_countInt());

        // setUp rating bars: 3 stars
        View ratingBar3 = listHeader.findViewById(R.id.review_list_header_rating_bar_for_3_rating);
        setUpRatingBar(ratingBar3, "3", meta.getRatingsDistribution().getThree(), meta.getReviews_countInt());

        // setUp rating bars: 2 stars
        View ratingBar2 = listHeader.findViewById(R.id.review_list_header_rating_bar_for_2_rating);
        setUpRatingBar(ratingBar2, "2", meta.getRatingsDistribution().getTwo(), meta.getReviews_countInt());

        // setUp rating bars: 1 stars
        View ratingBar1 = listHeader.findViewById(R.id.review_list_header_rating_bar_for_1_rating);
        setUpRatingBar(ratingBar1, "1", meta.getRatingsDistribution().getOne(), meta.getReviews_countInt());

    }

    private void setUpRatingBar(View parentView, String ratingBarFor, int count, int totalCount) {
        TextView ratingBarForNumber = (TextView) parentView.findViewById(R.id.rating_bar_rating_number);
        ratingBarForNumber.setText(ratingBarFor);

        View percentage = parentView.findViewById(R.id.rating_bar_percentage);
        ViewGroup.LayoutParams lp = percentage.getLayoutParams();
        lp.width = (int)(getResources().getDimension(R.dimen.rating_bar_width) * count / totalCount);
        percentage.setLayoutParams(lp);

        TextView ratingCount = (TextView) parentView.findViewById(R.id.rating_bar_rating_count);
        String userText = count <= 1 ? " user" : " users";
        ratingCount.setText(count + userText);
    }

    private void handleError(String msg) {
        Toast.makeText(home, msg, Toast.LENGTH_LONG).show();
    }

    private void showLoader() {
        pbContainer.setVisibility(View.VISIBLE);
        addReviewBtn.setVisibility(View.GONE);
    }

    private void hideLoader() {
        pbContainer.setVisibility(View.GONE);
    }
}

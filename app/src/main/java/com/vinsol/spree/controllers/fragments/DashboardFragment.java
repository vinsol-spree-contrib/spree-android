package com.vinsol.spree.controllers.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.squareup.picasso.Picasso;
import com.vinsol.spree.R;
import com.vinsol.spree.controllers.Home;
import com.vinsol.spree.models.Banner;
import com.vinsol.spree.models.BannerTypes;
import com.vinsol.spree.models.Product;
import com.vinsol.spree.utils.Common;
import com.vinsol.spree.utils.DisplayArea;
import com.vinsol.spree.utils.SharedPreferencesHelper;
import com.vinsol.spree.utils.StaticSingletonCustomFonts;
import com.vinsol.spree.viewhelpers.RecentlyViewedAdapter;
import com.vinsol.spree.views.CustomFontTextView;

import java.util.ArrayList;

/**
 * Created by vaibhav on 11/2/15.
 */
public class DashboardFragment extends Fragment {
    private SliderLayout currentOffersViewPager;
    private LinearLayout dashboardScrollViewChildContainer, currentOffersViewPagerMarkerContainer;
    private ArrayList<BannerTypes> dashboardData;
    private Home home;
    private ArrayList<ImageView> currentOffersViewPagerMarkers;
    private ArrayList<RelativeLayout> bannersLayoutArrayList;
    private LayoutInflater inflater;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment DashboardFragment.
     */
    public static DashboardFragment newInstance() {
        DashboardFragment fragment = new DashboardFragment();
        return fragment;
    }

    public DashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        home = (Home) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = inflater;
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Initialize view present in scroll view
        initUI(view);

        // Get data from cache
        dashboardData = new ArrayList<>();
        if (SharedPreferencesHelper.getCache()!=null && !SharedPreferencesHelper.getCache().getDashboardData().isEmpty()) {
            dashboardData.addAll(SharedPreferencesHelper.getCache().getDashboardData());
        }
        // Setup Current Offers view pager
        setupCurrentOffersViewPager(dashboardData.get(0).getBanners());
        // Adding view programmatically in scroll view
        addCurrentOffersViewPagerMarkerToUI(dashboardData.get(0).getBanners());
        dashboardData.remove(0);
        for (BannerTypes bannerTypes : dashboardData) {
            addSectionHeaderToUI(bannerTypes.getPresentation());
            addBannersToUI(bannerTypes.getBanners());
        }

        //  Add Recently viewed Products
        if (SharedPreferencesHelper.getCache()!=null && SharedPreferencesHelper.getCache().getRecentlyViewedProducts()!=null
                && !SharedPreferencesHelper.getCache().getRecentlyViewedProducts().isEmpty()) {
            addSectionHeaderToUI(getString(R.string.recently_viewed));
            addRecentlyViewedItemsToUI(SharedPreferencesHelper.getCache().getRecentlyViewedProducts());
        }
        setListeners();
        //This setInitialValues should be called last in this method
        setInitialValues();

    }

    private void initUI(View view) {
        dashboardScrollViewChildContainer   = (LinearLayout) view.findViewById(R.id.fragment_dashboard_scroll_view_child_container);
        currentOffersViewPager              = (SliderLayout) view.findViewById(R.id.fragment_dashboard_current_offers_view_pager);
    }

    private void setListeners() {
        currentOffersViewPager.addOnPageChangeListener(new ViewPagerEx.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < currentOffersViewPagerMarkerContainer.getChildCount(); i++) {
                    ImageView image = (ImageView) currentOffersViewPagerMarkerContainer.getChildAt(i);
                    if (i == position) {
                        image.setImageResource(R.drawable.current_offer_pager_icon_selected);
                    } else {
                        image.setImageResource(R.drawable.current_offer_pager_icon);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setupCurrentOffersViewPager(ArrayList<Banner> currentOfferBanners) {
        ViewGroup.LayoutParams vp = currentOffersViewPager.getLayoutParams();
        vp.height = (int) (DisplayArea.getDisplayWidth()/1.72);
        currentOffersViewPager.setLayoutParams(vp);
        for (Banner currentOffer : currentOfferBanners) {
            DefaultSliderView sliderView = new DefaultSliderView(home);
            sliderView.image(currentOffer.getImage());
            sliderView.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                @Override
                public void onSliderClick(BaseSliderView slider) {
                    home.showProductsFragment(ProductsFragment.PRODUCTS_MODE_BANNER, null);
                }
            });
            currentOffersViewPager.addSlider(sliderView);
        }

        currentOffersViewPager.setPresetTransformer(SliderLayout.Transformer.ZoomOutSlide);
        currentOffersViewPager.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Invisible);
        currentOffersViewPager.stopAutoCycle();
    }

    private void addCurrentOffersViewPagerMarkerToUI(ArrayList<Banner> currentOfferBanners) {
        currentOffersViewPagerMarkers = new ArrayList<>();
        currentOffersViewPagerMarkerContainer = (LinearLayout) getView().findViewById(R.id.fragment_dashboard_current_offers_view_pager_marker);
        int margin_5dp = Common.convertDpToPixel(home, 5);
        for (int i = 0; i < currentOfferBanners.size(); i++) {
            ImageView imageView = new ImageView(home);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setMargins(margin_5dp, margin_5dp, 0, margin_5dp);
            imageView.setImageResource(R.drawable.current_offer_pager_icon);
            if (i==0) imageView.setImageResource(R.drawable.current_offer_pager_icon_selected);
            currentOffersViewPagerMarkers.add(imageView);
            currentOffersViewPagerMarkerContainer.addView(imageView, lp);
        }
    }

    private void addSectionHeaderToUI(String header) {
        int margin_15dp = Common.convertDpToPixel(home, 15);
        CustomFontTextView textView = new CustomFontTextView(home);
        textView.setText(header);
        textView.setTypeface(StaticSingletonCustomFonts.getTypeface("roboto_light.ttf"));
        textView.setTextColor(ContextCompat.getColor(home, R.color.textColor));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, home.getResources().getDimension(R.dimen.text_size_19sp));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(margin_15dp, margin_15dp, 0, (int) home.getResources().getDimension(R.dimen.margin_10dp));
        dashboardScrollViewChildContainer.addView(textView, lp);
    }

    private void addBannersToUI(ArrayList<Banner> banners) {
        LinearLayout rowLayout = null;
        bannersLayoutArrayList = new ArrayList<>();
        for (int i=0; i < banners.size(); i++) {
            Banner banner = banners.get(i);
            RelativeLayout currentBannerLayout;
            if((i+1) % 2 == 1) { // left banner
                rowLayout = getRowLinearLayout();
                currentBannerLayout = addBannerView(banner, rowLayout, true);
                dashboardScrollViewChildContainer.addView(rowLayout);
            } else {
                currentBannerLayout = addBannerView(banner, rowLayout, false);
            }
            currentBannerLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    home.showProductsFragment(ProductsFragment.PRODUCTS_MODE_BANNER, null);
                }
            });
            bannersLayoutArrayList.add(currentBannerLayout);
        }
    }

    private void setInitialValues() {

    }

    private LinearLayout getRowLinearLayout() {
        LinearLayout layout = new LinearLayout(getActivity());

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layout.setLayoutParams(layoutParams);

        return layout;
    }

    private RelativeLayout addBannerView(final Banner banner, LinearLayout layout, boolean isLeft) {
        RelativeLayout bannerView;
        int margin = Common.convertDpToPixel(home, 2);
        int bannerWidth = (DisplayArea.getDisplayWidth() - Common.convertDpToPixel(home, 6))/2;

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(bannerWidth, LinearLayout.LayoutParams.MATCH_PARENT);

        layoutParams.topMargin = margin;
        if(isLeft) {
            bannerView = (RelativeLayout) inflater.inflate(R.layout.dashboard_brand_offers_grid_item, null);
            layoutParams.leftMargin = margin;
            layoutParams.rightMargin = margin;
        } else {
            bannerView = (RelativeLayout) inflater.inflate(R.layout.dashboard_brand_offers_grid_item, null);
            layoutParams.rightMargin = margin;
        }


        ImageView bannerImage = (ImageView) bannerView.findViewById(R.id.dashboard_brand_offers_grid_item_img);
        double ratio = 1.19;
        int imageWidth, imageHeight;
        imageWidth  = (DisplayArea.getDisplayWidth() - Common.convertDpToPixel(home, 6))/2;
        imageHeight = (int) (imageWidth * ratio);
        ViewGroup.LayoutParams imageLayoutParams = bannerImage.getLayoutParams();
        imageLayoutParams.width  = imageWidth;
        imageLayoutParams.height = imageHeight;
        bannerImage.setLayoutParams(imageLayoutParams);

        Picasso.with(home)
                .load(banner.getImage()).into(bannerImage);

        bannerView.setLayoutParams(layoutParams);
        layout.addView(bannerView);

        return bannerView;
    }

    private void addRecentlyViewedItemsToUI(ArrayList<Product> recentProducts) {
        RecentlyViewedAdapter recentlyViewedAdapter = new RecentlyViewedAdapter(recentProducts);
        RecyclerView recentlyView = new RecyclerView(home);
        RecyclerView.LayoutParams rp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        rp.height = Common.convertDpToPixel(home, 180);
        rp.bottomMargin = (int) home.getResources().getDimension(R.dimen.margin_10dp);
        recentlyView.setLayoutParams(rp);
        recentlyView.setHasFixedSize(true);
        recentlyView.setLayoutManager(new LinearLayoutManager(home, LinearLayoutManager.HORIZONTAL, false));
        recentlyView.setAdapter(recentlyViewedAdapter);
        dashboardScrollViewChildContainer.addView(recentlyView);
    }
}

package com.vinsol.spree.controllers.fragments;


import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.balysv.materialmenu.MaterialMenuDrawable;
import com.balysv.materialmenu.MaterialMenuView;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.vinsol.spree.R;
import com.vinsol.spree.api.ApiClient;
import com.vinsol.spree.api.models.LineItemWrapper;
import com.vinsol.spree.api.models.OrderWrapper;
import com.vinsol.spree.api.models.ProductWrapper;
import com.vinsol.spree.cache.Cache;
import com.vinsol.spree.controllers.Home;
import com.vinsol.spree.events.RefreshCartEvent;
import com.vinsol.spree.models.AvailableOption;
import com.vinsol.spree.models.Image;
import com.vinsol.spree.models.LineItem;
import com.vinsol.spree.models.Order;
import com.vinsol.spree.models.Product;
import com.vinsol.spree.models.ProductProperty;
import com.vinsol.spree.models.User;
import com.vinsol.spree.models.Variant;
import com.vinsol.spree.utils.BusProvider;
import com.vinsol.spree.utils.Common;
import com.vinsol.spree.utils.DisplayArea;
import com.vinsol.spree.utils.Log;
import com.vinsol.spree.utils.SharedPreferencesHelper;
import com.vinsol.spree.viewhelpers.ProductDetailColorOptionHelper;
import com.vinsol.spree.viewhelpers.ProductDetailOptionHelper;
import com.vinsol.spree.viewhelpers.RatingStarsHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductDetailFragment extends BaseFragment {
    private static final String TAG = "PRODUCT_DETAIL";

    private static final String PRODUCT_ID = "productId";
    private int productId;
    private Home home;
    private Product product;

    private SliderLayout productImagesViewPager;
    private LinearLayout productImagesViewPagerMarkerContainer;
    private ArrayList<ImageView> productImagesViewPagerMarkers;
    private FloatingActionButton addToCart;
    private MaterialMenuView back;
    private boolean dataReceived = false;
    private RelativeLayout pbContainer;

    private ViewGroup parent;

    private View dataContainer;
    private TextView productName;
    private TextView price;
    private View reviewContainer;

    private View ratingStars;
    private TextView ratingCount;
    private TextView reviewCount;

    private LinearLayout productOptionsLinearLayout;
    private ArrayList<ProductDetailOptionHelper> productDetailOptionHelpers = new ArrayList<>();
    private TextView productDescription;
    private LinearLayout productSpecificationLinearLayout;

    private LayoutInflater layoutInflater;

    private boolean isFabGoToCart = false;

    public ProductDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param productId Parameter 1.
     * @return A new instance of fragment ProductDetailFragment.
     */

    public static ProductDetailFragment newInstance(int productId) {
        ProductDetailFragment fragment = new ProductDetailFragment();
        Bundle args = new Bundle();
        args.putInt(PRODUCT_ID, productId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public String getTagText() {
        return TAG;
    }

    @Override
    public boolean onBackPressed() {
        home.popBackStack();
        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            productId = getArguments().getInt(PRODUCT_ID);
        }
        home = (Home) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layoutInflater = inflater;
        parent = container;
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_product_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI(view);
        setListeners();
        if (!dataReceived) loadProduct();
        else setupView();
    }

    private void initUI(View view) {
        dataContainer                           =                           view.findViewById(R.id.fragment_product_detail_scroll_view_child_container);
        productImagesViewPager                  = (SliderLayout)            view.findViewById(R.id.fragment_product_detail_images_view_pager);
        productImagesViewPagerMarkerContainer   = (LinearLayout)            view.findViewById(R.id.fragment_product_detail_product_images_view_pager_marker);
        pbContainer                             = (RelativeLayout)          view.findViewById(R.id.progress_bar_container);
        addToCart                               = (FloatingActionButton)    view.findViewById(R.id.add_to_cart);
        back                                    = (MaterialMenuView)        view.findViewById(R.id.fragment_product_detail_tab_bar_back_img);
        back.setState(MaterialMenuDrawable.IconState.ARROW);

        productName             = (TextView)  view.findViewById(R.id.fragment_product_detail_name_text_view);
        price                   = (TextView)  view.findViewById(R.id.fragment_product_detail_item_price);
        ratingStars             =             view.findViewById(R.id.fragment_product_detail_rating_stars);
        reviewContainer         =             view.findViewById(R.id.fragment_products_detail_view_review_container);
        ratingCount             = (TextView)  view.findViewById(R.id.fragment_product_detail_rating_count);
        reviewCount             = (TextView)  view.findViewById(R.id.fragment_product_detail_review_count);

        productOptionsLinearLayout = (LinearLayout) view.findViewById(R.id.fragment_product_detail_layout_for_options);
        productDescription      = (TextView) view.findViewById(R.id.fragment_product_detail_description_value);
        productSpecificationLinearLayout = (LinearLayout) view.findViewById(R.id.fragment_product_detail_layout_for_specification);


    }

    private void setListeners() {
        productImagesViewPager.addOnPageChangeListener(new ViewPagerEx.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < productImagesViewPagerMarkerContainer.getChildCount(); i++) {
                    ImageView image = (ImageView) productImagesViewPagerMarkerContainer.getChildAt(i);
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

        reviewContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (product.getRatingsCount()==0) home.showAddReviewFragment(productId, product.getName(), product.getDisplayPrice());
                else home.showReviewFragment(productId, product.getName(), product.getDisplayPrice());
            }
        });

        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isFabGoToCart) {
                    home.showCartFragment(true);
                } else {
                    addProductToCart();
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void loadProduct() {
        showLoader();
        Call<ProductWrapper> call = ApiClient.getInstance().getApiService().getProductById(productId);
        call.enqueue(new Callback<ProductWrapper>() {
            @Override
            public void onResponse(Response<ProductWrapper> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    hideLoader();
                    Log.d("Product detail success");
                    dataReceived = true;
                    ProductWrapper productWrapper = response.body();
                    product = productWrapper.getProduct();
                    setupView();
                    Cache cache = Cache.get();
                    cache.getRecentlyViewedProducts().add(0, product);
                    SharedPreferencesHelper.saveCache(cache);
                } else {
                    showErrorMessage("Some issue in loading product.");
                    Log.d("Product detail error");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoader();
                Log.d("Product detail failed");
            }
        });
    }

    private void setupView() {
        dataContainer.setVisibility(View.VISIBLE);

        // images
        if(product != null && product.getImages() != null) {
            showProductImages(product.getImages());
        }

        // name
        productName.setText(product.getName());

        // price
        price.setText(product.getDisplayPrice());

        // rating
        new RatingStarsHelper(getContext(), ratingStars).setRating(product.getAverageRating());

        // rating count
        ratingCount.setText("(" + product.getRatingsCount() + ")");

        // review count
        reviewCount.setText(product.getReviewsCount() + " REVIEWS");

        // show options
        showOptions();

        // description
        productDescription.setText(product.getDescription());

        // fab
        setFab();

        // show specifications
        showSpecifications();

    }

    private void setFab() {
        if(isFabGoToCart) {
            addToCart.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.cart_selected));
        } else {
            addToCart.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.add_to_cart_icon));
        }
    }

    private void updateUIWithVariant() {
        HashMap<String, String> selectedOptions = new HashMap<>();

        for (ProductDetailOptionHelper productDetailOptionHelper : productDetailOptionHelpers) {
            selectedOptions.put(productDetailOptionHelper.getType(), productDetailOptionHelper.getSelectedValue());
        }

        Variant variant = product.getVariantsWithSelectedOptions(selectedOptions);

        if(variant == null) {
            return;
        }

        // images
        if(variant.getImages() != null) {
            showProductImages(variant.getImages());
        }

        // name
        productName.setText(variant.getName());

        // price
        price.setText(variant.getDisplayPrice());

        // description
        productDescription.setText(variant.getDescription());
    }

    private void showOptions() {
        final ArrayList<AvailableOption> availableOptions = product.getAvailableOptions();

        for(int i=0; i < availableOptions.size(); i++) {
            AvailableOption availableOption = availableOptions.get(i);
            final ProductDetailOptionHelper productDetailOptionHelper;
            if(availableOption.getType().equals("Color")) {
                productDetailOptionHelper = new ProductDetailColorOptionHelper(getContext(), layoutInflater, productOptionsLinearLayout);
            } else {
                productDetailOptionHelper = new ProductDetailOptionHelper(getContext(), layoutInflater, productOptionsLinearLayout);
            }

            productDetailOptionHelper.showOptions(availableOption);
            productDetailOptionHelper.setPosition(i);
            productDetailOptionHelper.setOnOptionItemSelectedListener(new ProductDetailOptionHelper.OnOptionItemSelectedListener() {

                @Override
                public void onOptionItemSelected(String selectedItem) {
                    int positionOfOption = productDetailOptionHelper.getPosition();
                    availableOptions.get(positionOfOption).setCurrentlySelectedValue(selectedItem);

                    // enable next option
                    if (positionOfOption < availableOptions.size() - 1) {
                        productDetailOptionHelpers.get(positionOfOption + 1).setThisOptionEnabled(true);
                        AvailableOption availableOption = product.getAvailableOptionsAccordingToSelectedOption(availableOptions.get(positionOfOption), selectedItem, availableOptions.get(positionOfOption + 1));
                        productDetailOptionHelpers.get(positionOfOption + 1).updateOptions(availableOption);
                    } else {
                        // if this is the last option update product data with variant data
                        updateUIWithVariant();
                    }
                }
            });

            if (i == 0) {
                productDetailOptionHelper.setThisOptionEnabled(true);
            } else {
                productDetailOptionHelper.setThisOptionEnabled(false);
                productDetailOptionHelper.setMessageForDisabledState("Please select " + availableOptions.get(i -1).getType() + " first");
            }
            productDetailOptionHelpers.add(productDetailOptionHelper);
        }
    }

    private void showSpecifications() {
        HashMap<String, ArrayList<ProductProperty>> groupedProductProperties = product.getGroupedProductProperties();
        Iterator it = groupedProductProperties.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry groupedProductProperty = (Map.Entry)it.next();

            addPropertyHeader((String) groupedProductProperty.getKey());
            ArrayList<ProductProperty> productProperties = (ArrayList<ProductProperty>) groupedProductProperty.getValue();
            for(int i=0; i < productProperties.size(); i++) {
                addProductProperty(productProperties.get(i), i % 2);
            }
        }
    }

    private void addPropertyHeader(String headerText) {
        Log.d("add property header");

        LinearLayout specificationHeader = (LinearLayout) layoutInflater.inflate(R.layout.products_detail_specification_header, productSpecificationLinearLayout, false);
        TextView specificationHeaderText = (TextView) specificationHeader.findViewById(R.id.products_detail_specification_header_text);
        specificationHeaderText.setText(headerText);

        productSpecificationLinearLayout.addView(specificationHeader);
    }

    // backgroundType 0 white
    //backgroundType 1 grey
    private void addProductProperty(ProductProperty productProperty, int backgroundType) {
        Log.d("add property value " + productProperty.getPresentation() + "  " + productProperty.getValue());

        LinearLayout specificationItem = (LinearLayout) layoutInflater.inflate(R.layout.products_detail_specification_item, productSpecificationLinearLayout, false);

        Drawable backgroundDrawable;
        if(backgroundType == 0) {
            backgroundDrawable = getResources().getDrawable(R.drawable.product_detail_spec_list_item_white_bg);
        } else {
            backgroundDrawable = getResources().getDrawable(R.drawable.product_detail_spec_list_item_grey_bg);
        }
        specificationItem.setBackground(backgroundDrawable);

        TextView specificationKeyText = (TextView) specificationItem.findViewById(R.id.products_detail_specification_key_text);
        specificationKeyText.setText(productProperty.getPresentation());

        TextView specificationValueText = (TextView) specificationItem.findViewById(R.id.products_detail_specification_value_text);
        specificationValueText.setText(productProperty.getValue());

        productSpecificationLinearLayout.addView(specificationItem);
    }

    private void showProductImages(ArrayList<Image> productImages) {
        ArrayList<String> imagesURL = new ArrayList<>();
        for(Image productImage: productImages) {
            imagesURL.add(productImage.getLargeUrl());
            Log.d("ImageURL = " + productImage.getLargeUrl());
        }
        setupCurrentImagesViewPager(imagesURL);
        addProductImagesViewPagerMarkerToUI(imagesURL);
    }

    private void setupCurrentImagesViewPager(ArrayList<String> productImages) {
        productImagesViewPager.removeAllSliders();
        ViewGroup.LayoutParams vp = productImagesViewPager.getLayoutParams();
        vp.height = (int) (DisplayArea.getDisplayWidth());
        productImagesViewPager.setLayoutParams(vp);
        for (String productImage : productImages) {
            DefaultSliderView sliderView = new DefaultSliderView(home);
            sliderView.image(productImage);
//            sliderView.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
//                @Override
//                public void onSliderClick(BaseSliderView slider) {
//                    home.showProductsFragment(Constants.PRODUCTS_MODE_BANNER, null);
//                }
//            });
            productImagesViewPager.addSlider(sliderView);
        }

        productImagesViewPager.setPresetTransformer(SliderLayout.Transformer.ZoomOutSlide);
        productImagesViewPager.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Invisible);
        productImagesViewPager.stopAutoCycle();
    }

    private void addProductImagesViewPagerMarkerToUI(ArrayList<String> productImages) {
        productImagesViewPagerMarkers = new ArrayList<>();
        productImagesViewPagerMarkerContainer.removeAllViews();
        productImagesViewPagerMarkerContainer = (LinearLayout) getView().findViewById(R.id.fragment_product_detail_product_images_view_pager_marker);
        int margin_5dp = Common.convertDpToPixel(home, 5);
        for (int i = 0; i < productImages.size(); i++) {
            ImageView imageView = new ImageView(home);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setMargins(margin_5dp, margin_5dp, 0, margin_5dp);
            imageView.setImageResource(R.drawable.current_offer_pager_icon);
            if (i==0) imageView.setImageResource(R.drawable.current_offer_pager_icon_selected);
            productImagesViewPagerMarkers.add(imageView);
            productImagesViewPagerMarkerContainer.addView(imageView, lp);
        }
    }

    private int selectedVariantId = -1;
    private void addProductToCart() {
        ArrayList<Variant> variants = product.getVariants();
        if(variants == null || variants.size() == 0) {
            Toast.makeText(getContext(), "For Developer: no variants available", Toast.LENGTH_LONG).show();
            return;
        }

        if(variants.size() == 1) {
            // only master variant, update id with this variant
            selectedVariantId = variants.get(0).getId();
        } else {
            // see if user has selected all options
            HashMap<String, String> selectedOptions = new HashMap<>();

            for(ProductDetailOptionHelper productDetailOptionHelper : productDetailOptionHelpers) {
                if(productDetailOptionHelper.getSelectedValue() == null || productDetailOptionHelper.getSelectedValue() == "") {
                    Toast.makeText(getContext(), "Please select " + productDetailOptionHelper.getType() + " first", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    selectedOptions.put(productDetailOptionHelper.getType(), productDetailOptionHelper.getSelectedValue());
                }
            }

            Variant variant = product.getVariantsWithSelectedOptions(selectedOptions);
            if(variant == null) {
                Toast.makeText(getContext(), "No variant with selected options. Please try other combination", Toast.LENGTH_LONG).show();
                return;
            } else {
                selectedVariantId = variant.getId();
            }
        }

        showLoader();

        if (SharedPreferencesHelper.getCache() != null && SharedPreferencesHelper.getCache().getUser()!=null) {
            // user is already logged in
            final User user = SharedPreferencesHelper.getCache().getUser();
            // Get current order if any
            Call<Order> callForOrder = ApiClient.getInstance().getApiService().getCurrentOrder(user.getApiKey());
            callForOrder.enqueue(new Callback<Order>() {
                @Override
                public void onResponse(Response<Order> response, Retrofit retrofit) {
                    if (response.isSuccess()) {
                        Log.d("Response success ProductDetailFragment > getCart");
                        final Order order = response.body();
                        LineItem lineItem = new LineItem(1, selectedVariantId);
                        if (order != null) {
                            LineItemWrapper lineItemWrapper = new LineItemWrapper(lineItem);
                            Call<Order> callForAddLineItem = ApiClient.getInstance().getApiService().addToCart(order.getId(), user.getApiKey(), lineItemWrapper);
                            callForAddLineItem.enqueue(new Callback<Order>() {
                                @Override
                                public void onResponse(Response<Order> response, Retrofit retrofit) {
                                    Order order1 = response.body();
                                    if (order1.getErrors().isEmpty()) {
                                        hideLoader();
                                        Log.d("PDFragment > Add line item > onSuccess");
                                        Toast.makeText(home, "Added to cart", Toast.LENGTH_SHORT).show();
                                        SharedPreferencesHelper.saveTotalItems(order1.getItemCount());
                                        BusProvider.getInstance().post(new RefreshCartEvent(order1));

                                        isFabGoToCart = true;
                                        setFab();
                                    }
                                    else {
                                        hideLoader();
                                        //TODO : handle errors
                                    }
                                }

                                @Override
                                public void onFailure(Throwable t) {
                                    hideLoader();
                                    Log.d("PDFragment > Add line item > onFailure");
                                }
                            });
                        }
                        else { // when there is no current order for this user
                            Order orderToBeCreated = new Order();
                            orderToBeCreated.addLineItemToOrder(lineItem);
                            OrderWrapper orderWrapper = new OrderWrapper(orderToBeCreated);
                            Call<Order> callForCreateOrder = ApiClient.getInstance().getApiService().createOrder(user.getApiKey(), orderWrapper);
                            callForCreateOrder.enqueue(new Callback<Order>() {
                                @Override
                                public void onResponse(Response<Order> response, Retrofit retrofit) {
                                    Order order1 = response.body();
                                    if (order1.getErrors().isEmpty()) {
                                        hideLoader();
                                        Log.d("PDFragment > Create order > onSuccess");
                                        Toast.makeText(home, "Added to cart", Toast.LENGTH_SHORT).show();
                                        SharedPreferencesHelper.saveTotalItems(order1.getItemCount());
                                        BusProvider.getInstance().post(new RefreshCartEvent(order1));

                                        isFabGoToCart = true;
                                        setFab();
                                    }
                                    else {
                                        hideLoader();
                                        // TODO: handle errors
                                    }
                                }

                                @Override
                                public void onFailure(Throwable t) {
                                    hideLoader();
                                    Log.d("PDFragment > Create order > onFailure");
                                }
                            });
                        }
                    }
                    else {
                        hideLoader();
                        Log.d("PDFragment Response for current order not success. Check for error");
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    hideLoader();
                    Log.d("PDFragment > get current order > onFailure");
                }
            });
        }
        else {
            hideLoader();
            home.showProfileFragment(true);
        }
    }

    private void showLoader() {
        addToCart.setVisibility(View.GONE);
        pbContainer.setVisibility(View.VISIBLE);
    }

    private void hideLoader() {
        pbContainer.setVisibility(View.GONE);
        addToCart.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage(String msg) {
        Snackbar snackbar = Snackbar.make(parent, msg, Snackbar.LENGTH_INDEFINITE)
                .setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        loadProduct();
                    }
                });
        snackbar.setActionTextColor(Color.RED);
        snackbar.show();
    }
}

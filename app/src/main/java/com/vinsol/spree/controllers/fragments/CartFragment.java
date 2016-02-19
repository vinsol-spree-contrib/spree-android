package com.vinsol.spree.controllers.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.otto.Subscribe;
import com.vinsol.spree.R;
import com.vinsol.spree.api.ApiClient;
import com.vinsol.spree.api.models.LineItemWrapper;
import com.vinsol.spree.api.models.OrderWrapper;
import com.vinsol.spree.cache.Cache;
import com.vinsol.spree.controllers.Home;
import com.vinsol.spree.events.CheckoutAddressSelectedEvent;
import com.vinsol.spree.events.CheckoutCardSelectedEvent;
import com.vinsol.spree.events.RefreshCartEvent;
import com.vinsol.spree.models.Address;
import com.vinsol.spree.models.LineItem;
import com.vinsol.spree.models.Order;
import com.vinsol.spree.models.Payment;
import com.vinsol.spree.models.User;
import com.vinsol.spree.utils.BusProvider;
import com.vinsol.spree.utils.Log;
import com.vinsol.spree.utils.SharedPreferencesHelper;
import com.vinsol.spree.utils.Strings;
import com.vinsol.spree.viewhelpers.AddressCardViewHelper;
import com.vinsol.spree.viewhelpers.CartConfirmViewHelper;
import com.vinsol.spree.viewhelpers.CartLineItemActionsInterface;
import com.vinsol.spree.viewhelpers.LineItemsCustomAdapter;
import com.vinsol.spree.viewhelpers.PaymentCardViewHelper;

import java.io.IOException;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by vaibhav on 11/2/15.
 */
public class CartFragment extends BaseFragment implements CartLineItemActionsInterface {
    private static final String TAG = "CART";
    private Home home;
    private LayoutInflater inflater;
    private boolean isTabBarShown;
    private static final String SHOW_TAB_BAR = "SHOW_TAB_BAR";
    private RelativeLayout pbContainer, checkoutBar, downContainer;
    private LinearLayout tabBar, checkoutBtn, blankCart;
    private static final String FAILURE_MSG = "Something went wrong. Please try again";
    private RecyclerView lineItemsList;
    private TextView totalItems, totalPrice;
    private User user;
    private Order order;
    private LineItemsCustomAdapter itemsCustomAdapter;
    // no items view
    private TextView continueShoppingBtn, blankTxt;
    // address view
    private RelativeLayout cartAddressContainer, addEditAddressBtn;
    private AddressCardViewHelper addressCardViewHelper;
    private TextView continueToPayment;
    // payment view
    private RelativeLayout addEditPaymentBtn, cartPaymentContainer;
    private PaymentCardViewHelper paymentCardViewHelper;
    private TextView pay;
    // refresh view
    private TextView refresh;
    // confirm view
    private RelativeLayout cartConfirmContainer;
    private TextView confirmItemsTxt, confirmPriceTxt;
    private LinearLayout confirmScrollView, confirmBtn;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CartFragment.
     */
    public static CartFragment newInstance(boolean showTabBar) {
        CartFragment fragment = new CartFragment();
        Bundle args = new Bundle();
        args.putBoolean(SHOW_TAB_BAR, showTabBar);
        fragment.setArguments(args);
        return fragment;
    }

    public CartFragment() {
        // Required empty public constructor
    }

    @Override
    public String getTagText() {
        return TAG;
    }

    @Override
    public boolean onBackPressed() {
        if (order==null || order.getState().equals("cart")) {
            if (isTabBarShown) {
                home.popBackStack();
                return true;
            }
            else return false;
        }
        else {
            moveBackCheckoutState();
            return true;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments()!=null) {
            isTabBarShown = getArguments().getBoolean(SHOW_TAB_BAR);
        }
        home = (Home) getActivity();
        BusProvider.getInstance().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.inflater = inflater;
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI(view);
        setListeners();
        showHideTabBar();

        getCachedUserAndFetchOrder(false);
    }

    private void initUI(View view) {
        tabBar          = (LinearLayout)   view.findViewById(R.id.fragment_cart_tab_bar);
        checkoutBtn     = (LinearLayout)   view.findViewById(R.id.fragment_cart_checkout_bar_checkout_btn_container);
        downContainer   = (RelativeLayout) view.findViewById(R.id.fragment_cart_tab_bar_back_img_container);
        lineItemsList   = (RecyclerView)   view.findViewById(R.id.fragment_cart_recycler_view);
        totalItems      = (TextView)       view.findViewById(R.id.fragment_cart_checkout_bar_total_items_txt);
        totalPrice      = (TextView)       view.findViewById(R.id.fragment_cart_checkout_bar_total_price_txt);
        pbContainer     = (RelativeLayout) view.findViewById(R.id.progress_bar_container);
        checkoutBar     = (RelativeLayout) view.findViewById(R.id.fragment_cart_checkout_bar);
        // no items view
        blankCart            = (LinearLayout)   view.findViewById(R.id.fragment_cart_blank_container);
        continueShoppingBtn  = (TextView)       view.findViewById(R.id.fragment_cart_blank_continue_shopping_btn);
        blankTxt             = (TextView)       view.findViewById(R.id.fragment_cart_blank_txt);
        // address view
        addEditAddressBtn    = (RelativeLayout) view.findViewById(R.id.cart_address_item_edit_img_container);
        cartAddressContainer = (RelativeLayout) view.findViewById(R.id.fragment_cart_address_container);
        continueToPayment    = (TextView)       view.findViewById(R.id.fragment_cart_address_to_payment_btn);
        // payment view
        addEditPaymentBtn    = (RelativeLayout) view.findViewById(R.id.cart_payment_item_edit_img_container);
        cartPaymentContainer = (RelativeLayout) view.findViewById(R.id.fragment_cart_payment_container);

        pay                  = (TextView)       view.findViewById(R.id.fragment_cart_payment_pay_btn);
        // refresh view
        refresh              = (TextView)       view.findViewById(R.id.fragment_cart_refresh_btn);
        // confirm view
        cartConfirmContainer = (RelativeLayout) view.findViewById(R.id.fragment_cart_confirm_container);
        confirmItemsTxt      = (TextView)       view.findViewById(R.id.fragment_cart_confirm_bar_total_items_txt);
        confirmPriceTxt      = (TextView)       view.findViewById(R.id.fragment_cart_confirm_bar_total_price_txt);
        confirmScrollView    = (LinearLayout)   view.findViewById(R.id.fragment_cart_confirm_scroll_view_child_container);
        confirmBtn           = (LinearLayout)   view.findViewById(R.id.fragment_cart_confirm_bar_confirm_btn_container);
    }

    private void setListeners() {
        downContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                home.popBackStack();
            }
        });
        checkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupAddressView(order.getShippingAddress());
            }
        });
        // no items view
        continueShoppingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                home.showDashboard();
            }
        });
        // address view
        addEditAddressBtn.setVisibility(View.VISIBLE);
        addEditAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                home.showAddressFragment(user.getApiKey(), true, order.getShippingAddress());
            }
        });
        continueToPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (order.getState().equals("cart") && Strings.isEmpty(addressCardViewHelper.getAddress())) {
                    handleErrorWithToast("Please select an address");
                    return;
                }
                moveToPaymentState();
            }
        });
        // payment view
        addEditPaymentBtn.setVisibility(View.VISIBLE);
        addEditPaymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                home.showPaymentFragment(user.getApiKey(), true, order.getPayments().isEmpty() ? null : order.getPayments().get(0).getSource());
            }
        });
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (order.getState().equals("payment") && Strings.isEmpty(paymentCardViewHelper.getCardNumber())) {
                    handleErrorWithToast("Please select a card");
                    return;
                }
                moveToConfirmState();
            }
        });
        // refresh view
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCurrentOrder();
            }
        });
        // confirm view
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToCompleteState();
            }
        });
    }

    private void showHideTabBar() {
        if (isTabBarShown) tabBar.setVisibility(View.VISIBLE);
        else tabBar.setVisibility(View.GONE);
    }

    private void getCurrentOrder() {
        showLoader();
        Call<Order> currentOrderCall = ApiClient.getInstance().getApiService().getCurrentOrder(user.getApiKey());
        currentOrderCall.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Response<Order> response, Retrofit retrofit) {
                hideLoader();
                order = response.body();
                Log.d("CartFragment > getCurrentOrder > onResponse");
                if (response.isSuccess()) {
                    if (order != null) { // order exists status 200
                        setViewBaserOnOrderState();
                    } else { // order does not exist status 204
                        showNoItemsView(false);
                        updateCache(0);
                    }
                } else {
                    // wont have a 422 error for this api call -- To be verified
                }
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoader();
                Log.d("CartFragment > get current order > onFailure");
                handleErrorWithToast(FAILURE_MSG);
                showRefreshButton();
            }
        });
    }

    private void showLoader() {
        pbContainer.setVisibility(View.VISIBLE);
    }

    private void hideLoader() {
        pbContainer.setVisibility(View.GONE);
    }

    private void setViewBaserOnOrderState() {
        if (order != null) { // order exists status 200
            updateCache(order.getItemCount());
            if (order.getState().equals("cart")) setupView();
            else if (order.getState().equals("address"))
                setupAddressView(order.getShippingAddress());
            else if (order.getState().equals("payment")) {
                setupPaymentView(order.getPayments().isEmpty() ? null : order.getPayments().get(0));
            } else if (order.getState().equals("confirm")) setupConfirmView();
        } else { // order does not exist status 204
            showNoItemsView(false);
            updateCache(0);
        }
    }

    private void setupView() {
        totalItems.setText("Total (" + order.getItemCount() + ")");
        totalPrice.setText("$ " + order.getTotal());
        itemsCustomAdapter = new LineItemsCustomAdapter(home, this, order.getLineItems());
        lineItemsList.setAdapter(itemsCustomAdapter);
        lineItemsList.setLayoutManager(new LinearLayoutManager(home));
        showItemsView();
    }

    private void showNoItemsView(boolean orderComplete) {
        cartAddressContainer.setVisibility(View.GONE);
        lineItemsList.setVisibility(View.GONE);
        checkoutBar.setVisibility(View.GONE);
        cartPaymentContainer.setVisibility(View.GONE);
        refresh.setVisibility(View.GONE);
        cartConfirmContainer.setVisibility(View.GONE);
        blankCart.setVisibility(View.VISIBLE);
        if (orderComplete) blankTxt.setVisibility(View.GONE);
        else blankTxt.setVisibility(View.VISIBLE);
    }

    private void showItemsView() {
        blankCart.setVisibility(View.GONE);
        cartAddressContainer.setVisibility(View.GONE);
        cartPaymentContainer.setVisibility(View.GONE);
        refresh.setVisibility(View.GONE);
        cartConfirmContainer.setVisibility(View.GONE);
        lineItemsList.setVisibility(View.VISIBLE);
        checkoutBar.setVisibility(View.VISIBLE);
    }

    private void showAddressView() {
        blankCart.setVisibility(View.GONE);
        lineItemsList.setVisibility(View.GONE);
        checkoutBar.setVisibility(View.GONE);
        cartConfirmContainer.setVisibility(View.GONE);
        cartPaymentContainer.setVisibility(View.GONE);
        refresh.setVisibility(View.GONE);
        cartAddressContainer.setVisibility(View.VISIBLE);
    }

    private void showPaymentView() {
        blankCart.setVisibility(View.GONE);
        lineItemsList.setVisibility(View.GONE);
        refresh.setVisibility(View.GONE);
        checkoutBar.setVisibility(View.GONE);
        cartConfirmContainer.setVisibility(View.GONE);
        cartAddressContainer.setVisibility(View.GONE);
        cartPaymentContainer.setVisibility(View.VISIBLE);
    }

    private void showConfirmView() {
        blankCart.setVisibility(View.GONE);
        lineItemsList.setVisibility(View.GONE);
        checkoutBar.setVisibility(View.GONE);
        cartAddressContainer.setVisibility(View.GONE);
        cartPaymentContainer.setVisibility(View.GONE);
        refresh.setVisibility(View.GONE);
        cartConfirmContainer.setVisibility(View.VISIBLE);
    }

    private void showRefreshButton() {
        blankCart.setVisibility(View.GONE);
        lineItemsList.setVisibility(View.GONE);
        checkoutBar.setVisibility(View.GONE);
        cartAddressContainer.setVisibility(View.GONE);
        cartConfirmContainer.setVisibility(View.GONE);
        cartPaymentContainer.setVisibility(View.GONE);
        refresh.setVisibility(View.VISIBLE);
    }

    private void setupAddressView(Address address) {
        addressCardViewHelper = new AddressCardViewHelper(cartAddressContainer);
        addressCardViewHelper.setUpCard(address);

        showAddressView();
    }

    private void setupPaymentView(Payment payment) {
        paymentCardViewHelper = new PaymentCardViewHelper(cartPaymentContainer);
        paymentCardViewHelper.setUpCard(payment);
        showPaymentView();
    }

    private void setupConfirmView() {
        confirmItemsTxt.setText("Total (" + order.getItemCount() + ")");
        confirmPriceTxt.setText("$ " + order.getTotal());
        confirmScrollView.removeAllViews();
        CartConfirmViewHelper cartConfirmViewHelper = new CartConfirmViewHelper(home, inflater, confirmScrollView);
        cartConfirmViewHelper.createView(order);
        showConfirmView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BusProvider.getInstance().unregister(this);
    }

    @Subscribe
    public void onCheckoutAddressSelectedEvent(CheckoutAddressSelectedEvent event) {
        showLoader();
        Order orderWithAddress = new Order();
        orderWithAddress.setShipAddressId(event.addressId);
        orderWithAddress.setBillAddressId(event.addressId);
        OrderWrapper orderWrapper = new OrderWrapper(orderWithAddress);
        Call<Order> call = ApiClient.getInstance().getApiService().addAddressForOrder(order.getId(), user.getApiKey(), orderWrapper);
        call.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Response<Order> response, Retrofit retrofit) {
                hideLoader();
                if (response.isSuccess()) {
                    Log.d("CartFragment > addAddressForOrder > onResponse");
                    order = response.body();
                    setupAddressView(order.getShippingAddress());
                    BusProvider.getInstance().post(new RefreshCartEvent(order));
                } else {
                    Log.d("CartFragment > addAddressForOrder > onResponse > 422");
                    try {
                        String responseBody = response.errorBody().string();
                        order = new Gson().fromJson(responseBody, Order.class);
                        handleErrorWithToast(order.getErrors().get(0));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("CartFragment > addAddressForOrder > onFailure");
                hideLoader();
                handleErrorWithToast(FAILURE_MSG);
                showRefreshButton();
            }
        });
    }

    @Subscribe
    public void onCheckoutCardSelectedEvent(CheckoutCardSelectedEvent event) {
        showLoader();
        Order orderWithPayment = new Order();
        orderWithPayment.setExistingCard(event.cardId);
        OrderWrapper orderWrapper = new OrderWrapper(orderWithPayment);

        Call<Order> call = ApiClient.getInstance().getApiService().moveToConfirmState(order.getId(), user.getApiKey(), orderWrapper);
        call.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Response<Order> response, Retrofit retrofit) {
                hideLoader();
                if (response.isSuccess()) {
                    Log.d("CartFragment > moveToConfirmState > onResponse");
                    order = response.body();
                    setupPaymentView(order.getPayments().isEmpty() ? null : order.getPayments().get(0));
                    BusProvider.getInstance().post(new RefreshCartEvent(order));
                } else {
                    Log.d("CartFragment > moveToConfirmState > onResponse > 422");
                    try {
                        String responseBody = response.errorBody().string();
                        order = new Gson().fromJson(responseBody, Order.class);
                        handleErrorWithToast(order.getErrors().get(0));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("CartFragment > moveToConfirmState > onFailure");
                hideLoader();
                handleErrorWithToast(FAILURE_MSG);
                showRefreshButton();
            }
        });
    }

    public void editQuantity(LineItem lineItem) {
        showLoader();
        LineItemWrapper lineItemWrapper = new LineItemWrapper(lineItem);
        Call<Order> call = ApiClient.getInstance().getApiService().editQuantity(order.getId(), lineItem.getId(), user.getApiKey(), lineItemWrapper);
        call.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Response<Order> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    Log.d("CartFragment > editQuantity > onResponse");
                    order = response.body();
                    hideLoader();
                    setupView();
                    updateCache(order.getItemCount());
                    BusProvider.getInstance().post(new RefreshCartEvent(order));
                }
                else {
                    Log.d("CartFragment > editQuantity > onResponse > 422");
                    hideLoader();
                    try {
                        String responseBody = response.errorBody().string();
                        order = new Gson().fromJson(responseBody, Order.class);
                        handleErrorWithToast(order.getErrors().get(0));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoader();
                Log.d("CartFragment > editQuantity > onFailure");
                handleErrorWithToast(FAILURE_MSG);
                showRefreshButton();
            }
        });
    }

    public void removeLineItem(LineItem lineItem) {
        showLoader();
        Call<Order> call = ApiClient.getInstance().getApiService().removeProduct(order.getId(), lineItem.getId(), user.getApiKey());
        call.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Response<Order> response, Retrofit retrofit) {
                hideLoader();
                if (response.isSuccess()) {
                    order = response.body();
                    if (order!=null) {
                        Log.d("CartFragment > removeLineItem > onResponse");
                        setupView();
                        updateCache(order.getItemCount());
                        BusProvider.getInstance().post(new RefreshCartEvent(order));
                    }
                    else {
                        Log.d("order is null here");
                        showNoItemsView(false);
                        updateCache(0);
                    }
                }
                else {
                    Log.d("CartFragment > removeLineItem > onResponse > 422");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoader();
                Log.d("CartFragment > removeLineItem > onFailure");
                handleErrorWithToast(FAILURE_MSG);
                showRefreshButton();
            }
        });
    }

    private void moveToPaymentState() {
        showLoader();
        // make an api call for moving to next state
        Call<Order> call1 = ApiClient.getInstance().getApiService().moveToNextState(order.getId(), user.getApiKey());
        call1.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Response<Order> response, Retrofit retrofit) {
                // if order success of call1
                if (response.isSuccess()) {
                    Log.d("CartFragment > moveToPaymentState > call1 > onResponse");
                    order = response.body();
                    // if order is in payment state then setup payemnt view
                    if (order.getState().equals("payment")) {
                        hideLoader();
                        setupPaymentView(order.getPayments().isEmpty() ? null : order.getPayments().get(0));
                        BusProvider.getInstance().post(new RefreshCartEvent(order));
                    }
                    // else if order in address state then make another api call for moving to next state
                    else {
                        Call<Order> call2 = ApiClient.getInstance().getApiService().moveToNextState(order.getId(), user.getApiKey());
                        call2.enqueue(new Callback<Order>() {
                            @Override
                            public void onResponse(Response<Order> response, Retrofit retrofit) {
                                hideLoader();
                                // if order success of call2
                                if (response.isSuccess()) {
                                    order = response.body();
                                    setupPaymentView(order.getPayments().isEmpty() ? null : order.getPayments().get(0));
                                    BusProvider.getInstance().post(new RefreshCartEvent(order));
                                }
                                // if order NOT success of call2
                                else {
                                    Log.d("CartFragment > moveToPaymentState > call2 > onResponse > 422");
                                    try {
                                        String responseBody = response.errorBody().string();
                                        order = new Gson().fromJson(responseBody, Order.class);
                                        handleErrorWithToast(order.getErrors().get(0));
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                Log.d("CartFragment > moveToPaymentState > call2 > onFailure");
                                hideLoader();
                                handleErrorWithToast(FAILURE_MSG);
                                showRefreshButton();
                            }
                        });
                    }

                }
                // if order NOT success of call1
                else {
                    Log.d("CartFragment > moveToPaymentState > call1 > onResponse > 422");
                    hideLoader();
                    try {
                        String responseBody = response.errorBody().string();
                        order = new Gson().fromJson(responseBody, Order.class);
                        handleErrorWithToast(order.getErrors().get(0));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("CartFragment > moveToPaymentState > call1 > onFailure");
                handleErrorWithToast(FAILURE_MSG);
                hideLoader();
                showRefreshButton();
            }
        });
    }

    private void moveToConfirmState() {
        showLoader();
        // make an api call for moving to next state
        Call<Order> call1 = ApiClient.getInstance().getApiService().moveToNextState(order.getId(), user.getApiKey());
        call1.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Response<Order> response, Retrofit retrofit) {
                // if order success of call1
                if (response.isSuccess()) {
                    Log.d("CartFragment > moveToConfirmState > call1 > onResponse");
                    order = response.body();
                    // if order is in confirm state then setup confirm view
                    if (order.getState().equals("confirm")) {
                        hideLoader();
                        setupConfirmView();
                        BusProvider.getInstance().post(new RefreshCartEvent(order));
                    }
                    // else if order in address state then make another api call for moving to next state
                    else {
                        Call<Order> call2 = ApiClient.getInstance().getApiService().moveToNextState(order.getId(), user.getApiKey());
                        call2.enqueue(new Callback<Order>() {
                            @Override
                            public void onResponse(Response<Order> response, Retrofit retrofit) {
                                // if order success of call2
                                if (response.isSuccess()) {
                                    Log.d("CartFragment > moveToConfirmState > call2 > onResponse");
                                    order = response.body();
                                    // if order is in confirm state then setup confirm view
                                    if (order.getState().equals("confirm")) {
                                        hideLoader();
                                        setupConfirmView();
                                        BusProvider.getInstance().post(new RefreshCartEvent(order));
                                    }
                                    // else if order in payment state then make another api call for moving to next state
                                    else {
                                        Call<Order> call3 = ApiClient.getInstance().getApiService().moveToNextState(order.getId(), user.getApiKey());
                                        call3.enqueue(new Callback<Order>() {
                                            @Override
                                            public void onResponse(Response<Order> response, Retrofit retrofit) {
                                                // if order success of call3
                                                if (response.isSuccess()) {
                                                    Log.d("CartFragment > moveToConfirmState > call3 > onResponse");
                                                    order = response.body();
                                                    hideLoader();
                                                    setupConfirmView();
                                                    BusProvider.getInstance().post(new RefreshCartEvent(order));
                                                }
                                                // if order NOT success of call3
                                                else {
                                                    Log.d("CartFragment > moveToConfirmState > call3 > onResponse > 422");
                                                    hideLoader();
                                                    try {
                                                        String responseBody = response.errorBody().string();
                                                        order = new Gson().fromJson(responseBody, Order.class);
                                                        handleErrorWithToast(order.getErrors().get(0));
                                                    } catch (IOException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onFailure(Throwable t) {
                                                Log.d("CartFragment > moveToConfirmState > call3 > onFailure");
                                                hideLoader();
                                                handleErrorWithToast(FAILURE_MSG);
                                                showRefreshButton();
                                            }
                                        });
                                    }
                                }
                                // if order NOT success of call2
                                else {
                                    Log.d("CartFragment > moveToConfirmState > call 2 > onResponse > 422");
                                    hideLoader();
                                    try {
                                        String responseBody = response.errorBody().string();
                                        order = new Gson().fromJson(responseBody, Order.class);
                                        handleErrorWithToast(order.getErrors().get(0));
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                Log.d("CartFragment > moveToConfirmState > call 2 > onFailure");
                                hideLoader();
                                handleErrorWithToast(FAILURE_MSG);
                                showRefreshButton();
                            }
                        });
                    }

                }
                // if order NOT success of call1
                else {
                    Log.d("CartFragment > moveToConfirmState > call 1 > onResponse > 422");
                    hideLoader();
                    try {
                        String responseBody = response.errorBody().string();
                        order = new Gson().fromJson(responseBody, Order.class);
                        handleErrorWithToast(order.getErrors().get(0));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("CartFragment > moveToConfirmState > call1 > onFailure");
                handleErrorWithToast(FAILURE_MSG);
                hideLoader();
                showRefreshButton();
            }
        });
    }

    private void moveToCompleteState() {
        showLoader();
        Call<Order> call = ApiClient.getInstance().getApiService().moveToNextState(order.getId(), user.getApiKey());
        call.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Response<Order> response, Retrofit retrofit) {
                hideLoader();
                if (response.isSuccess()) {
                    Log.d("CartFragment > moveToCompleteState > onResponse");
                    order = response.body();
                    handleErrorWithToast("Order successful");
                    showNoItemsView(true);
                    updateCache(0);
                    BusProvider.getInstance().post(new RefreshCartEvent(order));
                }
                else {
                    Log.d("CartFragment > moveToCompleteState > onResponse > 422");
                    try {
                        String responseBody = response.errorBody().string();
                        order = new Gson().fromJson(responseBody, Order.class);
                        handleErrorWithToast(order.getErrors().get(0));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("CartFragment > moveToCompleteState > onFailure");
                hideLoader();
                handleErrorWithToast(FAILURE_MSG);
                showRefreshButton();
            }
        });
    }

    private void moveBackCheckoutState() {
        Call<Order> call = ApiClient.getInstance().getApiService().moveBackAState(order.getId(), user.getApiKey());
        call.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Response<Order> response, Retrofit retrofit) {
                order = response.body();
                hideLoader();
                if (response.isSuccess()) {
                    setViewBaserOnOrderState();
                } else {
                    // wont have a 422 error for this api call -- To be verified
                }

                BusProvider.getInstance().post(new RefreshCartEvent(order));
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("CartFragment > moveBackCheckoutState > onFailure");
                hideLoader();
                handleErrorWithToast(FAILURE_MSG);
                showRefreshButton();
            }
        });
    }

    private void handleErrorWithToast(String msg) {
        Toast.makeText(home, msg, Toast.LENGTH_SHORT).show();
    }

    private void updateCache(int items) {
        SharedPreferencesHelper.saveTotalItems(items);
    }

    @Subscribe
    public void onRefreshCartEvent(RefreshCartEvent event) {
        if(!isTabBarShown) {
            order = event.order;
            getCachedUserAndFetchOrder(true);
        }
    }

    private void getCachedUserAndFetchOrder(final boolean isRefreshingCart) {
        new AsyncTask<Void, Void, Cache>() {
            @Override
            protected Cache doInBackground(Void... voids) {
                return SharedPreferencesHelper.getCache();
            }

            @Override
            protected void onPostExecute(Cache cache) {
                if (cache !=null && cache.getUser()!=null) {
                    user = cache.getUser();
                    if(isRefreshingCart) {
                        setViewBaserOnOrderState();
                    } else {
                        getCurrentOrder();
                    }
                }
                else showNoItemsView(false);
            }
        }.execute();
    }
}
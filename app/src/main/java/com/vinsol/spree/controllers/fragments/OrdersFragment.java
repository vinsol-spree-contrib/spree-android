package com.vinsol.spree.controllers.fragments;

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
import com.balysv.materialmenu.MaterialMenuDrawable;
import com.balysv.materialmenu.MaterialMenuView;
import com.vinsol.spree.R;
import com.vinsol.spree.api.ApiClient;
import com.vinsol.spree.api.models.OrdersResponse;
import com.vinsol.spree.controllers.Home;
import com.vinsol.spree.models.Order;
import com.vinsol.spree.utils.BusProvider;
import com.vinsol.spree.viewhelpers.OrdersCustomAdapter;

import java.util.ArrayList;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by vaibhav on 12/30/15.
 */
public class OrdersFragment extends BaseFragment {
    private static final String TAG = "ORDERS";
    private Home home;
    private MaterialMenuView back;
    private TextView startShopping;
    private LinearLayout noOrderFoundContainer;
    private RecyclerView ordersListView;
    private ArrayList<Order> orders = new ArrayList<>();
    private RelativeLayout pbContainer;
    private String userToken;
    private OrdersCustomAdapter adapter;
    private static final String USER_TOKEN = "user_token";

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment OrdersFragment.
     */
    public static OrdersFragment newInstance(String token) {
        OrdersFragment fragment = new OrdersFragment();
        Bundle args = new Bundle();
        args.putString(USER_TOKEN, token);
        fragment.setArguments(args);
        return fragment;
    }

    public OrdersFragment() {
        // Required empty public constructor
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
        if (getArguments()!=null) {
            userToken = getArguments().getString(USER_TOKEN);
        }
        home = (Home) getActivity();
        BusProvider.getInstance().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_orders, container, false);
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
        loadOrders();
    }

    private void initUI(View view) {
        // common
        pbContainer             = (RelativeLayout)       view.findViewById(R.id.progress_bar_container);
        back                    = (MaterialMenuView)     view.findViewById(R.id.fragment_orders_tab_bar_back_img);
        back.setState(MaterialMenuDrawable.IconState.ARROW);
        // View mode
        noOrderFoundContainer   = (LinearLayout)         view.findViewById(R.id.fragment_orders_blank_container);
        startShopping           = (TextView)             view.findViewById(R.id.fragment_orders_start_shopping_btn);
        ordersListView          = (RecyclerView)         view.findViewById(R.id.fragment_orders_list_view);
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
        startShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                home.showDashboard();
            }
        });
    }

    private void loadOrders() {
        showLoader();
        Call<OrdersResponse> call = ApiClient.getInstance().getApiService().getMyOrders(userToken);
        call.enqueue(new Callback<OrdersResponse>() {
            @Override
            public void onResponse(Response<OrdersResponse> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    hideLoader();
                    OrdersResponse ordersResponse = response.body();
                    orders =  (ArrayList<Order>) ordersResponse.getOrders();
                    if (orders.isEmpty()) {
                        ordersListView.setVisibility(View.GONE);
                        noOrderFoundContainer.setVisibility(View.VISIBLE);
                    } else {
                        adapter = new OrdersCustomAdapter(home, orders);
                        ordersListView.setLayoutManager(new LinearLayoutManager(home));
                        ordersListView.setAdapter(adapter);
                        ordersListView.setVisibility(View.VISIBLE);

                    }
                } else {
                    hideLoader();
                    handleError(response.message());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoader();
                handleError(home.getResources().getString(R.string.api_call_failure_error));
            }
        });
    }

    private void handleError(String msg) {
        Toast.makeText(home, msg, Toast.LENGTH_SHORT).show();
    }

    private void showLoader() {
        pbContainer.setVisibility(View.VISIBLE);
    }

    private void hideLoader() {
        pbContainer.setVisibility(View.GONE);
    }
}


package com.vinsol.spree.controllers.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.balysv.materialmenu.MaterialMenuDrawable;
import com.balysv.materialmenu.MaterialMenuView;
import com.google.gson.Gson;
import com.squareup.otto.Subscribe;
import com.vinsol.spree.R;
import com.vinsol.spree.api.ApiClient;
import com.vinsol.spree.api.models.ProductsByTaxonsResponse;
import com.vinsol.spree.api.models.ProductsResponse;
import com.vinsol.spree.controllers.Home;
import com.vinsol.spree.enums.ViewMode;
import com.vinsol.spree.events.ApplyFiltersClickEvent;
import com.vinsol.spree.models.Filter;
import com.vinsol.spree.models.Product;
import com.vinsol.spree.utils.BusProvider;
import com.vinsol.spree.utils.Common;
import com.vinsol.spree.utils.Log;
import com.vinsol.spree.utils.NetworkStateHelper;
import com.vinsol.spree.utils.SharedPreferencesHelper;
import com.vinsol.spree.viewhelpers.ProductsCustomAdapter;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by vaibhav on 11/2/15.
 */
public class ProductsFragment extends BaseFragment {
    public static final int PRODUCTS_MODE_TAXON  = -20;
    public static final int PRODUCTS_MODE_BANNER = -30;

    private static final String TAG = "PRODUCT_LIST";
    private RecyclerView recyclerView;
    private ProductsCustomAdapter cardAdapter;
    private ImageView sort, search, switchView;
    private Home home;
    private MaterialMenuView materialMenuViewCross;
    private final int VIEW_MODE_LIST = 0;
    private final int VIEW_MODE_GRID = 1;
    private final int VIEW_MODE_CARD = 2;
    private int viewMode = VIEW_MODE_LIST;
    private List<Product> products;
//    private List<Filter> filters;
    private List<Filter> selectedFilters = new ArrayList<>();
    private int mode;
    private String queryParam;
    private FloatingActionButton filterButton;
    private RelativeLayout pbContainer;
    private static final String MODE_PARAM = "mode_param";
    private static final String QUERY_PARAM = "query_param";
    private boolean dataReceived = false;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ProductsFragment.
     */
    public static ProductsFragment newInstance(int mode, String queryParam) {
        ProductsFragment fragment = new ProductsFragment();
        Bundle args = new Bundle();
        args.putInt(MODE_PARAM, mode);
        args.putString(QUERY_PARAM, queryParam);
        fragment.setArguments(args);
        return fragment;
    }

    public ProductsFragment() {
        // Required empty public constructor
    }

    @Override
    public String getTagText() {
        return TAG;
    }

    @Override
    public boolean onBackPressed() {
        Log.d("ProductListFragment : onBackPressed");
        home.popBackStack();
        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mode = getArguments().getInt(MODE_PARAM);
            queryParam = getArguments().getString(QUERY_PARAM);
        }
        BusProvider.getInstance().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        home = (Home) getActivity();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_products, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI(view);
        setListeners();
        if (!dataReceived) loadProductsData();
        else setupView();
    }

    private void initUI(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.fragment_product_list_recycler_view);
        sort         = (ImageView)    view.findViewById(R.id.fragment_product_list_tab_bar_sort_img);
        search       = (ImageView)    view.findViewById(R.id.fragment_product_list_tab_bar_search_img);
        switchView   = (ImageView)    view.findViewById(R.id.fragment_product_list_tab_bar_switch_view_img);
        filterButton = (FloatingActionButton) view.findViewById(R.id.fragment_product_list_filter_fab);
        pbContainer  = (RelativeLayout) view.findViewById(R.id.progress_bar_container);
        materialMenuViewCross = (MaterialMenuView) view.findViewById(R.id.fragment_product_list_tab_bar_back_img);
        materialMenuViewCross.setState(MaterialMenuDrawable.IconState.ARROW);
        if (SharedPreferencesHelper.getCache()!=null && SharedPreferencesHelper.getCache().getUser()!=null && SharedPreferencesHelper.getTotalItems()>0) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)filterButton.getLayoutParams();
            layoutParams.bottomMargin += Common.convertDpToPixel(home, 45);
            RelativeLayout.LayoutParams recyclerViewParams = (RelativeLayout.LayoutParams) recyclerView.getLayoutParams();
            recyclerViewParams.bottomMargin += Common.convertDpToPixel(home, 45);
        }
    }

    private void setListeners() {
        materialMenuViewCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        switchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!dataReceived) return;
                if (viewMode == VIEW_MODE_CARD) viewMode = VIEW_MODE_LIST;
                else viewMode++;
                switch (viewMode) {
                    case VIEW_MODE_LIST:
                        switchView.setImageResource(R.drawable.grid_view_icon);
                        setupList();
                        break;
                    case VIEW_MODE_GRID:
                        switchView.setImageResource(R.drawable.card_view_icon);
                        setupGrid();
                        break;
                    case VIEW_MODE_CARD:
                        switchView.setImageResource(R.drawable.list_view_icon);
                        setupCard();
                        break;
                }
            }
        });
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!dataReceived) return;
//                home.showFiltersFragment((ArrayList<Filter>) filters, (ArrayList<Filter>) selectedFilters);
            }
        });
    }

    private void setupView() {
        cardAdapter = new ProductsCustomAdapter(home, (ArrayList<Product>)products);
        recyclerView.setAdapter(cardAdapter);
        setupList();
    }

    private void setupList() {
        recyclerView.setLayoutManager(new LinearLayoutManager(home));
        cardAdapter.setViewMode(ViewMode.LIST);
        cardAdapter.notifyDataSetChanged();
    }

    private void setupGrid() {
        recyclerView.setLayoutManager(new GridLayoutManager(home, 2));
        cardAdapter.setViewMode(ViewMode.GRID);
        cardAdapter.notifyDataSetChanged();
    }

    private void setupCard() {
        recyclerView.setLayoutManager(new LinearLayoutManager(home));
        cardAdapter.setViewMode(ViewMode.CARD);
        cardAdapter.notifyDataSetChanged();
    }

    private void loadProductsData() {
        Call<ProductsResponse> call;
        pbContainer.setVisibility(View.VISIBLE);
        switch (mode) {
            case PRODUCTS_MODE_BANNER :
                loadAllProductsData();
                break;
            case PRODUCTS_MODE_TAXON  :
                loadProductsByTaxonData();
                break;
            default:
                // do nothing
        }
    }

    private void loadAllProductsData() {
        Call<ProductsResponse> call;
        pbContainer.setVisibility(View.VISIBLE);
        call = ApiClient.getInstance().getApiService().getAllProducts();
        call.enqueue(new Callback<ProductsResponse>() {
            @Override
            public void onResponse(Response<ProductsResponse> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    pbContainer.setVisibility(View.GONE);
                    ProductsResponse productsResponse = response.body();
                    products = productsResponse.getProducts();
//                    filters = productsResponse.getFilters();
                    if (products.isEmpty()) {
                        Toast.makeText(home, "No products found", Toast.LENGTH_LONG).show();
                    } else {
                        dataReceived = true;
                        setupView();
                    }
                } else {
                    pbContainer.setVisibility(View.GONE);
                    handleError(response.message());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                pbContainer.setVisibility(View.GONE);
                //Log.d(t.getLocalizedMessage());
                handleError("Products load failed!");
            }
        });
    }

    private void loadProductsByTaxonData() {
        Call<ProductsByTaxonsResponse> call;
        pbContainer.setVisibility(View.VISIBLE);
        List<String> taxonNames = new ArrayList<>();
        taxonNames.add(queryParam);
        call = ApiClient.getInstance().getApiService().getProductsByTaxons(taxonNames);
        call.enqueue(new Callback<ProductsByTaxonsResponse>() {
            @Override
            public void onResponse(Response<ProductsByTaxonsResponse> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    pbContainer.setVisibility(View.GONE);
                    ProductsByTaxonsResponse productsResponse = response.body();
                    products = productsResponse.getProducts();
//                    filters = productsResponse.getFilters();
                    if (products.isEmpty()) {
                        Toast.makeText(home, "No products found", Toast.LENGTH_LONG).show();
                    } else {
                        dataReceived = true;
                        setupView();
                    }
                } else {
                    pbContainer.setVisibility(View.GONE);
                    handleError(response.message());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                pbContainer.setVisibility(View.GONE);
                //Log.d(t.getLocalizedMessage());
                handleError("Products load failed!");
            }
        });
    }

    private void handleError(String msg) {
        AlertDialog.Builder alert = new AlertDialog.Builder(home)
                .setTitle("Error!")
                .setMessage(msg)
                .setNeutralButton("RETRY", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        loadProductsData();
                    }
                });
        Dialog errorDialog = alert.create();
        errorDialog.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BusProvider.getInstance().unregister(this);
    }

    @Subscribe
    public void onApplyFiltersClickEvent(ApplyFiltersClickEvent event) {
        home.popBackStack();
        selectedFilters = event.selectedFilters;
        if (selectedFilters!=null && !selectedFilters.isEmpty()) filterButton.setImageResource(R.drawable.filter_applied_icon);
        else filterButton.setImageResource(R.drawable.filter_icon);
        loadFilteredProducts(event.queryParams);
    }

    private void loadFilteredProducts(String queryParam) {
        String query = "http://shop-spree.herokuapp.com/api/ams/products?";
        query += queryParam;
        if (NetworkStateHelper.isConnected(home)) {
//            dataReceived = false;
            new FilteredProductRequest().execute(query);
        }
        else {
            Toast.makeText(home, "Your network connection seems unavailable. Please try again", Toast.LENGTH_SHORT).show();
        }

    }
    //TODO : to be used when retrofit 2.1 is out
    /*private void loadFilteredProducts(Map<String, String> map) {
        Call<ProductsResponse> call;
        dataReceived = false;
        pb.setVisibility(View.VISIBLE);
        call = ApiClient.getInstance().getApiService().getProductsByFilter(map);


        call.enqueue(new Callback<ProductsResponse>() {
            @Override
            public void onResponse(Response<ProductsResponse> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    pb.setVisibility(View.GONE);
                    ProductsResponse productsResponse = response.body();
                    products = productsResponse.getProducts();
                    filters = productsResponse.getFilters();
                    if (products.isEmpty()) {
                        Toast.makeText(home, "No products found", Toast.LENGTH_LONG).show();
                    } else {
                        dataReceived = true;
                        setupView();
                    }
                } else {
                    pb.setVisibility(View.GONE);
                    handleError(response.message());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                pb.setVisibility(View.GONE);
                //Log.d(t.getLocalizedMessage());
                handleError("Products load failed!");
            }
        });
    }*/


    class FilteredProductRequest extends AsyncTask<String, Void, String> {
        Exception mException = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pbContainer.setVisibility(View.VISIBLE);
            dataReceived = false;
            this.mException = null;
        }

        @Override
        protected String doInBackground(String... params) {
            StringBuilder urlString = new StringBuilder();
            urlString.append(params[0]);
            Log.d(urlString.toString());
            StringBuilder result = new StringBuilder();
            HttpURLConnection urlConnection = null;
            URL url = null;
            InputStream inStream = null;
            try {
                String temp = urlString.toString().replaceAll(" ", "%20");
                url = new URL(temp);

                urlConnection = (HttpURLConnection) url.openConnection();
                inStream = new BufferedInputStream(urlConnection.getInputStream());

                BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                Log.d(result);

            } catch (Exception e) {
                this.mException = e;
                e.printStackTrace();
            } finally {
                if (inStream != null) {
                    try {
                        inStream.close();
                    } catch (IOException ignored) {
                    }
                }
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }

            return (result.toString());
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pbContainer.setVisibility(View.GONE);
            if (this.mException != null){
                Log.d(this.mException.getLocalizedMessage());
            }
            else {
                ProductsResponse productsResponse = new Gson().fromJson(result, ProductsResponse.class);
                dataReceived = true;
                products = productsResponse.getProducts();
//                filters = productsResponse.getFilters();
                setupView();
                if (products.isEmpty()) {
                    Toast.makeText(home, "No products found", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

}

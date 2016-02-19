package com.vinsol.spree.controllers;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.otto.Subscribe;
import com.vinsol.spree.R;
import com.vinsol.spree.controllers.fragments.AddReviewFragment;
import com.vinsol.spree.controllers.fragments.AddressFragment;
import com.vinsol.spree.controllers.fragments.BaseFragment;
import com.vinsol.spree.controllers.fragments.CartFragment;
import com.vinsol.spree.controllers.fragments.FiltersFragment;
import com.vinsol.spree.controllers.fragments.OrdersFragment;
import com.vinsol.spree.controllers.fragments.PasswordFragment;
import com.vinsol.spree.controllers.fragments.PaymentFragment;
import com.vinsol.spree.controllers.fragments.ProductDetailFragment;
import com.vinsol.spree.controllers.fragments.ProductsFragment;
import com.vinsol.spree.controllers.fragments.ProfileFragment;
import com.vinsol.spree.controllers.fragments.ReviewsFragment;
import com.vinsol.spree.controllers.fragments.SignupFragment;
import com.vinsol.spree.controllers.fragments.drawerFragment.DrawerFragment;
import com.vinsol.spree.controllers.fragments.drawerFragment.HostActivityDrawerInterface;
import com.vinsol.spree.events.OpenProductDetailEvent;
import com.vinsol.spree.models.Address;
import com.vinsol.spree.models.Card;
import com.vinsol.spree.models.Filter;
import com.vinsol.spree.utils.BusProvider;
import com.vinsol.spree.utils.Common;
import com.vinsol.spree.utils.Constants;
import com.vinsol.spree.utils.DisplayArea;
import com.vinsol.spree.utils.Log;
import com.vinsol.spree.utils.SharedPreferencesHelper;
import com.vinsol.spree.viewhelpers.HomePagerAdapter;

import java.util.ArrayList;


public class Home extends AppCompatActivity implements HostActivityInterface, HostActivityDrawerInterface {
    private static final int TAB_BAR_ICON_CART = 1;

    private BaseFragment    selectedFragment;
    private DisplayMetrics  displayMetrics;
    private DrawerLayout    navigationDrawer;
    private DrawerFragment  drawerFragment;
    private ViewPager       homeViewPager;
    private HomePagerAdapter adapter;
    private int             lastPageSelected = 0;
    // Checkout bar
    private LinearLayout    checkoutBar;
    private TextView        totalItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        setDisplayArea();
        BusProvider.getInstance().register(this);
        initUI();
        setupActionBar();
        setupPager();
        setListeners();
        showDrawerFragment();
        setUpDrawer();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_overflow_menu_actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                navigationDrawer.openDrawer(GravityCompat.START);
                return true;
            case R.id.action_about_us:
                Common.openBrowserWithURL(this, Constants.ABOUT_US_URL);
                return true;
            case R.id.action_faqs:
                Common.openBrowserWithURL(this, Constants.FAQS_URL);
                return true;
            case R.id.action_contact_us:
                Common.openBrowserWithURL(this, Constants.CONTACT_US_URL);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BusProvider.getInstance().unregister(this);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount()>0) {
            if(selectedFragment == null || !selectedFragment.onBackPressed()) {
                Log.d("Home : onBackPressed");
                super.onBackPressed();
            }
        } else if(lastPageSelected == TAB_BAR_ICON_CART && adapter.cartFragment.onBackPressed()) {
            return;
        }
        else super.onBackPressed();
    }

    @Override
    public void setSelectedFragment(BaseFragment fragment) {
        this.selectedFragment = fragment;
    }

    @Override
    public void popBackStack() {
        getSupportFragmentManager().popBackStackImmediate();
        hideCheckoutBar();
        if (selectedFragment instanceof ProductsFragment) showCheckoutBar();
        Log.d("selected fragment : " + selectedFragment);
        Log.d("Count : " + getSupportFragmentManager().getBackStackEntryCount());
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            hideCheckoutBar();
        }
    }

    @Override
    public void addFragment(BaseFragment fragment, boolean addToBackStack, boolean withAnimation) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        hideCheckoutBar();
        if(withAnimation) {
            ft.setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_left, R.anim.slide_in_from_left, R.anim.slide_out_to_right);
        }
        if (fragment instanceof ProductsFragment) showCheckoutBar();
        ft.replace(R.id.home_fragment_container, fragment, fragment.getTagText());
        if(addToBackStack) ft.addToBackStack(fragment.getTagText());
        ft.commit();
    }

    @Override
    public DisplayMetrics getDisplayMetrics() {
        return displayMetrics;
    }

    @Override
    public void onDrawerClose() {
        closeNavigationDrawer();
    }

    @Override
    public void onDrawerSelectTaxonomy(String queryParam) {
        closeNavigationDrawer();
        showProductsFragment(ProductsFragment.PRODUCTS_MODE_TAXON, queryParam);
    }

    private void showDrawerFragment() {
        drawerFragment = new DrawerFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.home_drawer_fragment_container, drawerFragment, drawerFragment.getTagText());
        ft.commit();
    }

    private void initUI() {
        navigationDrawer     = (DrawerLayout)   findViewById(R.id.home_container);
        homeViewPager        = (ViewPager)      findViewById(R.id.home_view_pager);
        // checkout bar
        checkoutBar          = (LinearLayout)   findViewById(R.id.home_checkout_bar);
        totalItems           = (TextView)       findViewById(R.id.home_checkout_bar_total_items_txt);
    }

    private void setupActionBar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.menu_icon);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void setupPager() {
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = (ViewPager) findViewById(R.id.home_view_pager);
        adapter = new HomePagerAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(1);


        // Give the TabLayout the ViewPager
        final TabLayout tabLayout = (TabLayout) findViewById(R.id.home_tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        // Iterate over all tabs and set the custom view
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(adapter.getTabView(i));
        }


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position != 2) Common.hideKeyboard(Home.this);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        tabLayout.getTabAt(0).getCustomView().setSelected(true);
    }

    private void setListeners() {
        homeViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Common.hideKeyboard(Home.this);
                lastPageSelected=position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        // checkout bar
        checkoutBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideCheckoutBar();
                showCartFragment(true);
            }
        });
    }

    public void closeNavigationDrawer() {
        navigationDrawer.closeDrawer(Gravity.LEFT);
    }
    private void setUpDrawer() {
        navigationDrawer.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                Common.hideKeyboard(Home.this);
            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                drawerFragment.reset();
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }

    private void setDisplayArea() {
        displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int displayWidth;
        displayWidth = displayMetrics.widthPixels;
        DisplayArea.setDisplayWidth(displayWidth);
    }

    public void showProductsFragment(int mode, String queryParam) {
        ProductsFragment fragment = ProductsFragment.newInstance(mode, queryParam);
        addFragment(fragment, true, true);
    }

    public void showSignupFragment() {
        SignupFragment fragment = SignupFragment.newInstance();
        addFragment(fragment, true, true);
    }

    public void showPasswordFragment(int mode, String token) {
        PasswordFragment fragment = PasswordFragment.newInstance(mode, token);
        addFragment(fragment, true, true);
    }

    public void showAddressFragment(String token, boolean isSelector, Address selectedAddressForCheckout) {
        AddressFragment fragment = AddressFragment.newInstance(token, isSelector, selectedAddressForCheckout);
        addFragment(fragment, true, true);
    }

    public void showOrdersFragment(String token) {
        OrdersFragment fragment = OrdersFragment.newInstance(token);
        addFragment(fragment, true, true);
    }

    public void showPaymentFragment(String token, boolean isSelector, Card selectedCardForCheckout) {
        PaymentFragment fragment = PaymentFragment.newInstance(token, isSelector, selectedCardForCheckout);
        addFragment(fragment, true, true);
    }

    public void showProductDetailFragment(int productId) {
        ProductDetailFragment fragment = ProductDetailFragment.newInstance(productId);
        addFragment(fragment, true, true);
    }

    public void showReviewFragment(int productId, String productName, String productPrice) {
        ReviewsFragment fragment = ReviewsFragment.newInstance(productId, productName, productPrice);
        addFragment(fragment, true, true);
    }

    public void showAddReviewFragment(int productId, String productName, String productPrice) {
        AddReviewFragment fragment = AddReviewFragment.newInstance(productId, productName, productPrice);
        addFragment(fragment, true, true);
    }

    public void showFiltersFragment(ArrayList<Filter> filters, ArrayList<Filter> selectedFilters) {
        FiltersFragment fragment = FiltersFragment.newInstance(filters, selectedFilters);
        addFragment(fragment, true, true);
    }

    public void showCartFragment(boolean showTabBar) {
        CartFragment fragment = CartFragment.newInstance(showTabBar);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_from_bottom, R.anim.slide_out_to_top, R.anim.in_no_anim, R.anim.slide_out_to_bottom);
        ft.replace(R.id.home_fragment_container, fragment, fragment.getTagText());
        ft.addToBackStack(fragment.getTagText());
        ft.commit();
    }

    public void showProfileFragment(boolean showTabBar) {
        ProfileFragment fragment = ProfileFragment.newInstance(showTabBar);
        addFragment(fragment, true, true);
    }

    public void showCheckoutBar() {
        if (SharedPreferencesHelper.getCache()!=null && SharedPreferencesHelper.getCache().getUser()!=null && SharedPreferencesHelper.getTotalItems()>0) {

            checkoutBar.setVisibility(View.VISIBLE);
            totalItems.setText("Items " + String.valueOf(SharedPreferencesHelper.getTotalItems()));
        }
    }

    public void hideCheckoutBar() {
        checkoutBar.setVisibility(View.GONE);
    }

    @Subscribe
    public void onOpenProductDetailEvent(OpenProductDetailEvent event) {
        showProductDetailFragment(event.productId);
    }

    public void showDashboard() {
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        homeViewPager.setCurrentItem(0);
        hideCheckoutBar();
    }

}

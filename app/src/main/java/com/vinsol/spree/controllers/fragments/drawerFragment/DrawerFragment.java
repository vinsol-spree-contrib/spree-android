package com.vinsol.spree.controllers.fragments.drawerFragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.balysv.materialmenu.MaterialMenuDrawable;
import com.balysv.materialmenu.MaterialMenuView;
import com.squareup.okhttp.ResponseBody;
import com.vinsol.spree.R;
import com.vinsol.spree.api.ApiClient;
import com.vinsol.spree.api.models.TaxonomiesResponse;
import com.vinsol.spree.cache.Cache;
import com.vinsol.spree.controllers.fragments.BaseFragment;
import com.vinsol.spree.models.Taxon;
import com.vinsol.spree.models.Taxonomy;
import com.vinsol.spree.utils.Log;
import com.vinsol.spree.utils.SharedPreferencesHelper;

import java.util.ArrayList;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Infernus on 23/12/15.
 */
public class DrawerFragment extends BaseFragment implements DrawerActionListener {

    private MaterialMenuView actionButton;
    private TextView titleLabel;
    private DrawerPage page1;
    private DrawerPage page2;
    private ProgressBar progressBar;
    private RelativeLayout pagesContainer;

    private DrawerPage currentPage;

    private HostActivityDrawerInterface hostActivityDrawerInterface;

    private ArrayList<Taxon> taxons = new ArrayList<>();

    @Override
    public String getTagText() {
        return DrawerFragment.class.getSimpleName();
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.drawer_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(getActivity() instanceof HostActivityDrawerInterface) {
            hostActivityDrawerInterface = (HostActivityDrawerInterface) getActivity();
        } else {
            throw new RuntimeException("Host Activity of DrawerFragment must implement HostActivityDrawerInterface");
        }

        initUI();
        setListeners();
        getTaxonomies();
    }

    public void reset() {
        showPage1(true);
    }

    @Override
    public void onShowPage1(boolean isInstant) {
        showPage1(isInstant);
    }

    @Override
    public void onShowPage2(boolean isInstant, Taxon taxon) {
        showPage2(isInstant, taxon);
    }

    @Override
    public void onTaxonSelected(Taxon taxon) {
        if(taxon.getChildren().size() == 0) {
            hostActivityDrawerInterface.onDrawerSelectTaxonomy(taxon.getId());
        } else {
            showPage2(false, taxon);
        }
    }



    private void getTaxonomies() {
        if (SharedPreferencesHelper.getCache()!=null && !SharedPreferencesHelper.getCache().getCachedTaxonomies().isEmpty()) {
            setup(SharedPreferencesHelper.getCache().getCachedTaxonomies());
        } else {
            pagesContainer.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);

            Call<TaxonomiesResponse> call = ApiClient.getInstance().getApiService().getAllTaxonomies();
            call.enqueue(new Callback<TaxonomiesResponse>() {
                @Override
                public void onResponse(Response<TaxonomiesResponse> response, Retrofit retrofit) {
                    Log.d("Taxonomies data request succeeded");
                    if (response.isSuccess()) {
                        Log.d("Taxonomies received");
                        TaxonomiesResponse taxonomiesResponse = response.body();
                        ArrayList<Taxonomy> taxonomies = new ArrayList<Taxonomy>();
                        taxonomies.addAll(taxonomiesResponse.getTaxonomies());
                        Cache cache = Cache.get();
                        cache.getCachedTaxonomies().clear();
                        cache.getCachedTaxonomies().addAll(taxonomies);
                        SharedPreferencesHelper.saveCache(cache);
                        pagesContainer.setVisibility(View.GONE);
                        progressBar.setVisibility(View.VISIBLE);

                        setup(taxonomies);
                    } else {
                        int statusCode = response.code();
                        Log.d("Taxonomies not received. Error code : " + statusCode);
                        ResponseBody errorBody = response.errorBody();
                        Log.d("Tax Error : " + errorBody.toString());
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    Log.d("Taxonomies data request failed");
                }
            });
        }
    }

    private void initUI() {
        actionButton    = (MaterialMenuView) getView().findViewById(R.id.drawer_fragment_action);
        titleLabel      = (TextView) getView().findViewById(R.id.drawer_fragment_title);
        page1           = (DrawerPage) getView().findViewById(R.id.drawer_fragment_page_1);
        page2           = (DrawerPage) getView().findViewById(R.id.drawer_fragment_page_2);
        progressBar     = (ProgressBar) getView().findViewById(R.id.drawer_fragment_progress_bar);
        pagesContainer  = (RelativeLayout) getView().findViewById(R.id.drawer_fragment_pages_container);
    }

    private void setup(ArrayList<Taxonomy> taxonomies) {
        taxons.clear();
        for(Taxonomy taxonomy : taxonomies) {
            taxons.add(taxonomy.getRootTaxon());
        }

        page1.setup(getActivity(), taxons, this);
        actionButton.setState(MaterialMenuDrawable.IconState.X);
        currentPage = page1;
    }

    private void setListeners() {
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentPage.equals(page1)) {
                    hostActivityDrawerInterface.onDrawerClose();
                } else {
                    showPage1(false);
                }
            }
        });
    }

    private void showPage1(boolean isInstant) {
        currentPage = page1;
        if(isInstant) {
            page1.showInstantly();
            page2.hideInstantly();
        } else {
            page1.show();
            page2.hide();
        }

        actionButton.animateState(MaterialMenuDrawable.IconState.X);
        titleLabel.setText("HOME");
    }

    private void showPage2(final boolean isInstant, Taxon taxon) {
        currentPage = page2;

        page2.setup(getActivity(), (ArrayList<Taxon>) taxon.getChildren(), this);
        actionButton.animateState(MaterialMenuDrawable.IconState.ARROW);
        titleLabel.setText(taxon.getName());

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                if(isInstant) {
                    page2.showInstantly();
                    page1.hideInstantly();
                } else {
                    page2.show();
                    page1.hide();
                }
            }
        });
    }
}

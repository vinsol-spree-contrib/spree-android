package com.vinsol.spree.controllers.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.balysv.materialmenu.MaterialMenuDrawable;
import com.balysv.materialmenu.MaterialMenuView;
import com.vinsol.spree.R;
import com.vinsol.spree.api.models.NameValuePair;
import com.vinsol.spree.controllers.Home;
import com.vinsol.spree.events.ApplyFiltersClickEvent;
import com.vinsol.spree.models.Filter;
import com.vinsol.spree.utils.BusProvider;
import com.vinsol.spree.views.BaseFilterLayout;
import com.vinsol.spree.views.MultiSelectFilterLayout;
import com.vinsol.spree.views.RangeFilterLayout;
import com.vinsol.spree.views.SingleSelectFilterLayout;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FiltersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FiltersFragment extends BaseFragment {
    private static final String FILTER_TYPE_MULTI_SELECT  = "Multi-Select";
    private static final String FILTER_TYPE_SINGLE_SELECT = "Single-Select";
    private static final String FILTER_TYPE_RANGE         = "Range";

    private static final String TAG = "FILTERS";
    private Home home;
    private MaterialMenuView back;
    private ScrollView scrollView;
    private ArrayList<BaseFilterLayout> filterLayouts;
    private LinearLayout scrollViewChildContainer;
    private TextView clear, apply;
    private ArrayList<Filter> filters;
    private ArrayList<Filter> selectedFilters;
    private static final String FILTER_DATA = "filter_data";
    private static final String SELECTED_FILTER_DATA = "selected_filter_data";

    public FiltersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param filters
     * @return A new instance of fragment FiltersFragment.
     */
    public static FiltersFragment newInstance(ArrayList<Filter> filters, ArrayList<Filter> selectedFilters) {
        FiltersFragment fragment = new FiltersFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(FILTER_DATA, filters);
        args.putParcelableArrayList(SELECTED_FILTER_DATA, selectedFilters);
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
            filters = getArguments().getParcelableArrayList(FILTER_DATA);
            selectedFilters = getArguments().getParcelableArrayList(SELECTED_FILTER_DATA);
        }
        home = (Home) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_filters, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI(view);
        addFiltersToUI();
        setListeners();
    }

    private void initUI(View view) {
        clear = (TextView) view.findViewById(R.id.fragment_filters_clear_btn);
        apply = (TextView) view.findViewById(R.id.fragment_filters_apply_btn);
        scrollView = (ScrollView) view.findViewById(R.id.fragment_filters_scroll_view);
        scrollViewChildContainer = (LinearLayout) view.findViewById(R.id.fragment_filters_scroll_view_child_container);
        back  = (MaterialMenuView) view.findViewById(R.id.fragment_filters_tab_bar_back_img);
        back.setState(MaterialMenuDrawable.IconState.X);
    }

    private void addFiltersToUI() {
        BaseFilterLayout filterLayout = null;
        filterLayouts = new ArrayList<>();
        // iterate over filters
        for (Filter filter :
                filters) {
            if (filter.getFilterType().equalsIgnoreCase(FILTER_TYPE_MULTI_SELECT)) {
                filterLayout = new MultiSelectFilterLayout(home, filter, scrollViewChildContainer);
            }
            if (filter.getFilterType().equalsIgnoreCase(FILTER_TYPE_SINGLE_SELECT)) {
                filterLayout = new SingleSelectFilterLayout(home, filter, scrollViewChildContainer);
            }
            if (filter.getFilterType().equalsIgnoreCase(FILTER_TYPE_RANGE)) {
                filterLayout = new RangeFilterLayout(home, filter, scrollViewChildContainer);
            }
            if (!selectedFilters.isEmpty()) {
                for (Filter selectedFilter :
                        selectedFilters) {
                    if (selectedFilter.getName().equals(filter.getName())) {
                        filterLayout.setSelectedFilter(selectedFilter.getValues());
                    }
                }
            }
            filterLayouts.add(filterLayout);
        }

        /*LinearLayout linearLayout = new LinearLayout(home);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Common.convertDpToPixel(home, 20));
        linearLayout.setLayoutParams(layoutParams);
        scrollViewChildContainer.addView(linearLayout);*/
    }


    private void setListeners() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (BaseFilterLayout baseFilterLayout :
                        filterLayouts) {
                    baseFilterLayout.clear();
                }
            }
        });
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyFilters();
            }
        });
    }

    private void applyFilters() {
        //Map<String, String> map;
        String queryParams = "";
        List<NameValuePair> pairList = new ArrayList<>();
        selectedFilters = new ArrayList<>();
        for (BaseFilterLayout baseFilterLayout :
                filterLayouts) {
            //TODO : to be used when retrofit 2.1 is out
            //pairList.addAll(baseFilterLayout.getQueryString());
            queryParams += baseFilterLayout.getQueryString();
            if (baseFilterLayout.getSelectedFilter()!=null) selectedFilters.add(baseFilterLayout.getSelectedFilter());
        }
        //map = Common.getPathMap(pairList);
        BusProvider.getInstance().post(new ApplyFiltersClickEvent(selectedFilters, queryParams));
    }
}

package com.vinsol.spree.events;

import com.vinsol.spree.models.Filter;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by vaibhav on 12/17/15.
 */
public class ApplyFiltersClickEvent {
    public ArrayList<Filter> selectedFilters;
    public String queryParams;

    public ApplyFiltersClickEvent(ArrayList<Filter> selectedFilters, String queryParams) {
        this.selectedFilters = selectedFilters;
        this.queryParams = queryParams;
    }
}

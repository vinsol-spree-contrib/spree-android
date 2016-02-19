package com.vinsol.spree.controllers.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vinsol.spree.R;

/**
 * Created by vaibhav on 11/2/15.
 */
public class MoreOptionsFragment extends Fragment {
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MoreOptionsFragment.
     */
    public static MoreOptionsFragment newInstance() {
        MoreOptionsFragment fragment = new MoreOptionsFragment();
        return fragment;
    }

    public MoreOptionsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_more_options, container, false);
    }


}

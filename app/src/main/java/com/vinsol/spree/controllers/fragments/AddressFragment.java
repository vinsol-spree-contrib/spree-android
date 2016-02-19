package com.vinsol.spree.controllers.fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.balysv.materialmenu.MaterialMenuDrawable;
import com.balysv.materialmenu.MaterialMenuView;
import com.squareup.otto.Subscribe;
import com.vinsol.spree.R;
import com.vinsol.spree.api.ApiClient;
import com.vinsol.spree.api.models.ErrorResponse;
import com.vinsol.spree.events.AddressesListOptionClickEvent;
import com.vinsol.spree.events.CheckoutAddressSelectedEvent;
import com.vinsol.spree.controllers.Home;
import com.vinsol.spree.models.Address;
import com.vinsol.spree.models.State;
import com.vinsol.spree.utils.BusProvider;
import com.vinsol.spree.utils.Common;
import com.vinsol.spree.api.ErrorUtils;
import com.vinsol.spree.utils.Log;
import com.vinsol.spree.utils.SharedPreferencesHelper;
import com.vinsol.spree.viewhelpers.AddressesCustomAdapter;

import java.util.ArrayList;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by vaibhav on 12/21/15.
 */
public class AddressFragment extends BaseFragment {
    private static final String TAG = "ADDRESS";
    private static final int MODE_ADD = 1;
    private static final int MODE_EDIT = 2;
    private static final int MODE_VIEW = 3;
    private static final String USER_TOKEN = "user_token";
    private static final String IS_SELECTOR = "is_selector";
    private static final String SELECTED_ADDRESS_FOR_CHECKOUT = "selected_address_for_checkout";

    private Home home;
    private int mode=MODE_VIEW;
    private String userToken;

    private ArrayList<Address> addresses = new ArrayList<>();
    private ArrayList<State> stateArrayList = new ArrayList<>();

    private boolean                 isSelector;
    private MaterialMenuView        back;
    private TextView                title;
    private LinearLayout            noAddressFoundContainer;
    private FloatingActionButton    addAddressBtn;
    private ListView                addressListView;
    private RelativeLayout          pbContainer;

    private ScrollView              addOrEditContainer;
    private int                     stateSpinnerSelectedItem;
    private AddressesCustomAdapter adapter;
    private Address selectedAddressForEditOrRemoval, newAddress, selectedAddressForCheckout;

    // Add/ edit mode
    private EditText firstName, lastName, addressLine1, addressLine2;
    private EditText city, pincode, phone;
    private ImageButton save;
    private Spinner stateSpinner;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AddressFragment.
     */
    public static AddressFragment newInstance(String token, boolean isSelector, Address selectedAddressForCheckout) {
        AddressFragment fragment = new AddressFragment();
        Bundle args = new Bundle();
        args.putString(USER_TOKEN, token);
        args.putBoolean(IS_SELECTOR, isSelector);
        args.putSerializable(SELECTED_ADDRESS_FOR_CHECKOUT, selectedAddressForCheckout);
        fragment.setArguments(args);
        return fragment;
    }

    public AddressFragment() {
        // Required empty public constructor
    }

    @Override
    public String getTagText() {
        return TAG;
    }

    @Override
    public boolean onBackPressed() {
        Log.d("AddressFragment : onBackPressed");
        if(mode == MODE_ADD || mode == MODE_EDIT) {
            mode = MODE_VIEW;
            showViewModeContent();
            showAddresses();
            setValuesBasedOnMode();
        } else {
            home.popBackStack();
        }
        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments()!=null) {
            userToken = getArguments().getString(USER_TOKEN);
            isSelector = getArguments().getBoolean(IS_SELECTOR);
            selectedAddressForCheckout = getArguments().getSerializable(SELECTED_ADDRESS_FOR_CHECKOUT)==null ? null : (Address) getArguments().getSerializable(SELECTED_ADDRESS_FOR_CHECKOUT);
        }
        home = (Home) getActivity();
        BusProvider.getInstance().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_address, container, false);
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
        loadAddresses();
        setValuesBasedOnMode();
    }

    private void initUI(View view) {
       // common
        pbContainer             = (RelativeLayout)       view.findViewById(R.id.progress_bar_container);
        back                    = (MaterialMenuView)     view.findViewById(R.id.fragment_address_tab_bar_back_img);
        title                   = (TextView)             view.findViewById(R.id.fragment_address_tab_bar_txt);
        back.setState(MaterialMenuDrawable.IconState.ARROW);
        // View mode
        noAddressFoundContainer = (LinearLayout)         view.findViewById(R.id.fragment_address_blank_container);
        addAddressBtn           = (FloatingActionButton) view.findViewById(R.id.fragment_address_fab);
        addressListView         = (ListView)             view.findViewById(R.id.fragment_address_list_view);
        // ADD or EDIT mode
        addOrEditContainer      = (ScrollView)           view.findViewById(R.id.fragment_address_scroll_view);
        firstName               = (EditText)             view.findViewById(R.id.fragment_address_first_name_txt);
        lastName                = (EditText)             view.findViewById(R.id.fragment_address_last_name_txt);
        addressLine1            = (EditText)             view.findViewById(R.id.fragment_address_line1_txt);
        addressLine2            = (EditText)             view.findViewById(R.id.fragment_address_line2_txt);
        city                    = (EditText)             view.findViewById(R.id.fragment_address_city_txt);
        pincode                 = (EditText)             view.findViewById(R.id.fragment_address_pincode_txt);
        phone                   = (EditText)             view.findViewById(R.id.fragment_address_phone_txt);
        save                    = (ImageButton)          view.findViewById(R.id.fragment_address_save_btn);
        stateSpinner            = (Spinner)              view.findViewById(R.id.fragment_address_state_spinner);
        setStateSpinnerAdapter();
    }

    private void setValuesBasedOnMode() {
        switch (mode) {
            case MODE_VIEW: title.setText(home.getResources().getString(R.string.addresses));
                if (isSelector) title.setText("Delivery Address");
                break;
            case MODE_ADD: title.setText(home.getResources().getString(R.string.add_address));
                firstName.setText("");
                lastName.setText("");
                addressLine1.setText("");
                addressLine2.setText("");
                city.setText("");
                phone.setText("");
                pincode.setText("");
                break;
            case MODE_EDIT: title.setText(home.getResources().getString(R.string.edit_address));
                firstName.setText(selectedAddressForEditOrRemoval.getFirstName());
                lastName.setText(selectedAddressForEditOrRemoval.getLastName());
                addressLine1.setText(selectedAddressForEditOrRemoval.getAddressLine1());
                addressLine2.setText(selectedAddressForEditOrRemoval.getAddressLine2());
                city.setText(selectedAddressForEditOrRemoval.getCity());
                for (State state :
                        stateArrayList) {
                    if (selectedAddressForEditOrRemoval.getStateId().equals(state.getId())) stateSpinner.setSelection(stateArrayList.indexOf(state));
                }
                phone.setText(selectedAddressForEditOrRemoval.getPhone());
                pincode.setText(selectedAddressForEditOrRemoval.getZipcode());
                break;
        }
    }

    private void setStateSpinnerAdapter() {
        if (SharedPreferencesHelper.getCache()!=null && SharedPreferencesHelper.getCache().getStatesList()!=null
                && !SharedPreferencesHelper.getCache().getStatesList().isEmpty()) {
            stateArrayList = SharedPreferencesHelper.getCache().getStatesList();
            String[] stateNames = new String[stateArrayList.size()];
            for (int i = 0; i < stateNames.length; i++) {
                State state = stateArrayList.get(i);
                stateNames[i] = state.getName();
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(home, android.R.layout.simple_spinner_item, stateNames);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            stateSpinner.setAdapter(adapter);
        }
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
        addAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mode = MODE_ADD;
                showAddOrEditModeContent();
            }
        });
        addressListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isSelector) {
                    for (Address address : addresses) {
                        address.setIsSelected(false);
                    }
                    addresses.get(position).setIsSelected(true);
                    adapter.notifyDataSetChanged();
                    BusProvider.getInstance().post(new CheckoutAddressSelectedEvent(addresses.get(position).getId()));
                    home.popBackStack();
                }
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.hideKeyboard(home);
                if (validateForm())
                    switch (mode) {
                        case MODE_ADD:
                            addAddress();
                            break;
                        case MODE_EDIT:
                            editAddress();
                            break;
                    }
            }
        });

        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                stateSpinnerSelectedItem = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void loadAddresses() {
        showLoader();
        Call call = ApiClient.getInstance().getApiService().getUserAddresses(userToken);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Response response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    hideLoader();
                    addresses = (ArrayList<Address>) response.body();
                    showAddresses();
                } else {
                    hideLoader();
                    ErrorResponse errorResponse = ErrorUtils.parseError(response, retrofit);
                    if (errorResponse.getErrors().getZipCode() != null && errorResponse.getErrors().getZipCode().get(0).equalsIgnoreCase("is invalid"))
                        handleError("zipcode is in valid");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoader();
                handleError(home.getResources().getString(R.string.api_call_failure_error));
            }
        });
    }

    private void showAddresses() {
        if (addresses.isEmpty()) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) addAddressBtn.getLayoutParams();
            params.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            params.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            params.setMargins(0, (int) home.getResources().getDimension(R.dimen.margin_10dp), 0, 0);
            params.addRule(RelativeLayout.BELOW, R.id.fragment_address_blank_container);
            params.addRule(RelativeLayout.CENTER_HORIZONTAL);
            addAddressBtn.setLayoutParams(params);
            addAddressBtn.setVisibility(View.VISIBLE);
            addressListView.setVisibility(View.GONE);
            noAddressFoundContainer.setVisibility(View.VISIBLE);
        } else {
            if (isSelector && selectedAddressForCheckout != null) {
                for (Address address :
                        addresses) {
                    if (address.getId().equals(selectedAddressForCheckout.getId()))
                        address.setIsSelected(true);
                }
            }
            adapter = new AddressesCustomAdapter(home, R.layout.addresses_list_view_item, addresses);
            addressListView.setAdapter(adapter);
            addressListView.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) addAddressBtn.getLayoutParams();
            params.removeRule(RelativeLayout.BELOW);
            params.removeRule(RelativeLayout.CENTER_HORIZONTAL);
            params.setMargins(0, 0, (int) home.getResources().getDimension(R.dimen.margin_15dp), (int) home.getResources().getDimension(R.dimen.margin_15dp));
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            addAddressBtn.setLayoutParams(params);
        }
    }

    private boolean validateForm() {
        if (TextUtils.isEmpty(firstName.getText())) {
            firstName.setError("cannot be empty");
            return false;
        }
        if (TextUtils.isEmpty(lastName.getText())) {
            lastName.setError("cannot be empty");
            return false;
        }
        if (TextUtils.isEmpty(addressLine1.getText())) {
            addressLine1.setError("cannot be empty");
            return false;
        }
        if (TextUtils.isEmpty(addressLine2.getText())) {
            addressLine2.setError("cannot be empty");
            return false;
        }
        if (TextUtils.isEmpty(city.getText())) {
            city.setError("cannot be empty");
            return false;
        }
        if (TextUtils.isEmpty(pincode.getText())) {
            pincode.setError("cannot be empty");
            return false;
        }
        if (TextUtils.isEmpty(phone.getText())) {
            phone.setError("cannot be empty");
            return false;
        }
        newAddress = new Address();
        newAddress.setFirstName(firstName.getText().toString());
        newAddress.setLastName(lastName.getText().toString());
        newAddress.setAddressLine1(addressLine1.getText().toString());
        newAddress.setAddressLine2(addressLine2.getText().toString());
        newAddress.setCity(city.getText().toString());
        newAddress.setStateId(stateArrayList.get(stateSpinnerSelectedItem).getId());
        newAddress.setZipcode(pincode.getText().toString());
        newAddress.setPhone(phone.getText().toString());
        return true;
    }

    private void handleError(String msg) {
        Toast.makeText(home, msg, Toast.LENGTH_LONG).show();
    }

    private void showLoader() {
        pbContainer.setVisibility(View.VISIBLE);
        addAddressBtn.setVisibility(View.GONE);
    }

    private void hideLoader() {
        pbContainer.setVisibility(View.GONE);
        if (mode==MODE_VIEW) addAddressBtn.setVisibility(View.VISIBLE);
    }

    private void showViewModeContent() {
        noAddressFoundContainer.setVisibility(View.GONE);
        addressListView.setVisibility(View.VISIBLE);
        addAddressBtn.setVisibility(View.VISIBLE);
        addOrEditContainer.setVisibility(View.GONE);
        setValuesBasedOnMode();
    }

    private void showAddOrEditModeContent() {
        noAddressFoundContainer.setVisibility(View.GONE);
        addressListView.setVisibility(View.GONE);
        addAddressBtn.setVisibility(View.GONE);
        addOrEditContainer.setVisibility(View.VISIBLE);
        setValuesBasedOnMode();
    }

    @Subscribe
    public void onAddressesListOptionClickEvent(AddressesListOptionClickEvent event) {
        selectedAddressForEditOrRemoval = event.address;
        if (event.operationType.equals("Edit")) {
            mode = MODE_EDIT;
            showAddOrEditModeContent();
        }
        else {
            mode = MODE_VIEW;
            setValuesBasedOnMode();
            removeAddress();
        }
    }

    private void addAddress() {
        showLoader();
        Call<Address> call = ApiClient.getInstance().getApiService().createAddress(userToken, newAddress);
        call.enqueue(new Callback<Address>() {
            @Override
            public void onResponse(Response<Address> response, Retrofit retrofit) {
                hideLoader();
                if (response.isSuccess()) {
                    handleError("Address added");
                    addOrEditContainer.setVisibility(View.GONE);
                    loadAddresses();
                    mode = MODE_VIEW;
                    setValuesBasedOnMode();
                }
                else {
                    handleError(response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoader();
                Log.d("Address add failure > " + t.getLocalizedMessage());
                handleError(home.getResources().getString(R.string.api_call_failure_error));
            }
        });
    }

    private void editAddress() {
        showLoader();
        Call<Address> call = ApiClient.getInstance().getApiService().updateAddress(selectedAddressForEditOrRemoval.getId(), userToken, newAddress);
        call.enqueue(new Callback<Address>() {
            @Override
            public void onResponse(Response<Address> response, Retrofit retrofit) {
                hideLoader();
                if (response.isSuccess()) {
                    handleError("Address edited");
                    addOrEditContainer.setVisibility(View.GONE);
                    loadAddresses();
                    mode = MODE_VIEW;
                    setValuesBasedOnMode();
                }
                else {
                    handleError(response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoader();
                Log.d("Address edit failure > " + t.getLocalizedMessage());
                handleError(home.getResources().getString(R.string.api_call_failure_error));
            }
        });
    }

    private void removeAddress() {
        showLoader();
        Call<Address> call = ApiClient.getInstance().getApiService().removeAddress(selectedAddressForEditOrRemoval.getId(), userToken);
        call.enqueue(new Callback<Address>() {
            @Override
            public void onResponse(Response<Address> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    handleError("Address removed");
                    loadAddresses();
                    mode = MODE_VIEW;
                    setValuesBasedOnMode();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoader();
                Log.d("Address remove failure > " + t.getLocalizedMessage());
                handleError(home.getResources().getString(R.string.api_call_failure_error));
            }
        });
    }
}

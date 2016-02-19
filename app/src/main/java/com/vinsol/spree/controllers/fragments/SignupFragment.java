package com.vinsol.spree.controllers.fragments;

import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vinsol.spree.R;
import com.vinsol.spree.api.ApiClient;
import com.vinsol.spree.api.ErrorUtils;
import com.vinsol.spree.api.models.ErrorResponse;
import com.vinsol.spree.controllers.Home;
import com.vinsol.spree.events.SignupSuccessfulEvent;
import com.vinsol.spree.models.User;
import com.vinsol.spree.utils.BusProvider;
import com.vinsol.spree.utils.Common;
import com.vinsol.spree.utils.Log;

import retrofit.Call;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by vaibhav on 12/2/15.
 */
public class SignupFragment extends BaseFragment {
    private static final String TAG = "SIGNUP";
    private Home home;
    private ImageView back, showPwd;
    private EditText email, name, password, phone;
    private TextView signup;
    private User user;
    private RelativeLayout pbContainer;
    private boolean isShown = false;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SignupFragment.
     */
    public static SignupFragment newInstance() {
        SignupFragment fragment = new SignupFragment();
        return fragment;
    }

    public SignupFragment() {
        // Required empty public constructor
    }

    @Override
    public String getTagText() {
        return TAG;
    }

    @Override
    public boolean onBackPressed() {
        Log.d("SignupFragment : onBackPressed");
        home.popBackStack();
        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        home = (Home) getActivity();
        user = new User();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signup, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI(view);
        setListeners();
    }

    private void initUI(View view) {
        back     = (ImageView) view.findViewById(R.id.fragment_signup_tab_bar_back_img);
        email    = (EditText)  view.findViewById(R.id.fragment_signup_email_txt);
        password = (EditText)  view.findViewById(R.id.fragment_signup_password_txt);
        name     = (EditText)  view.findViewById(R.id.fragment_signup_name_txt);
        phone    = (EditText)  view.findViewById(R.id.fragment_signup_number_txt);
        signup   = (TextView)  view.findViewById(R.id.fragment_signup_btn);
        showPwd  = (ImageView) view.findViewById(R.id.fragment_signup_show_password_img);
        pbContainer = (RelativeLayout) view.findViewById(R.id.progress_bar_container);
        password.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
    }

    private void setListeners() {
        /*email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (TextUtils.isEmpty(email.getText()) || !Patterns.EMAIL_ADDRESS.matcher(email.getText()).matches()) {
                        email.setError("Invalid Email");
                    }
                }
            }
        });
        name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (TextUtils.isEmpty(name.getText())) {
                        name.setError("Password cannot be empty");
                    }
                }
            }
        });
        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (TextUtils.isEmpty(password.getText())) {
                        password.setError("Password cannot be empty");
                    }
                }
            }
        });*/
        showPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isShown) {
                    password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    isShown = true;
                }
                else {
                    password.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                    isShown = false;
                }
            }
        });


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateForm()) signupUser();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private boolean validateForm() {
        if (TextUtils.isEmpty(email.getText()) || !Patterns.EMAIL_ADDRESS.matcher(email.getText()).matches()) {
            email.setError("Invalid Email");
            return false;
        }
        if (TextUtils.isEmpty(name.getText())) {
            name.setError("Password cannot be empty");
            return false;
        }
        if (TextUtils.isEmpty(password.getText())) {
            password.setError("Password cannot be empty");
            return false;
        }
        return true;
    }

    private void signupUser() {
        Common.hideKeyboard(home);
        pbContainer.setVisibility(View.VISIBLE);
        user = new User(email.getText().toString(), password.getText().toString(), name.getText().toString());
        Call<User> call = ApiClient.getInstance().getApiService().signup(user);
        call.enqueue(new retrofit.Callback<User>() {
            @Override
            public void onResponse(Response<User> response, Retrofit retrofit) {
                pbContainer.setVisibility(View.GONE);
                if (response.isSuccess()) {
                    Log.d("User signed up");
                    user = response.body();
                    BusProvider.getInstance().post(new SignupSuccessfulEvent(user));
                }
                else {
                    Log.d("User not signed up");
                    ErrorResponse errorResponse = ErrorUtils.parseError(response, retrofit);
                    if (errorResponse.getErrors().getEmail() != null && errorResponse.getErrors().getEmail().get(0).equalsIgnoreCase("has already been taken")) {
                        handleError("Email already registered");
                    }
                    if (errorResponse.getErrors().getPassword() != null && errorResponse.getErrors().getPassword().get(0).equalsIgnoreCase("is too short (minimum is 6 characters)")) {
                        handleError("Password is too short (minimum is 6 characters)");
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("User signed up failure");
                handleError("User sign up failure. Pls try again");
            }
        });
    }

    private void handleError(String msg) {
        Toast.makeText(home, msg, Toast.LENGTH_SHORT).show();
    }
}

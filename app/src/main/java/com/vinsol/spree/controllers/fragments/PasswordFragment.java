package com.vinsol.spree.controllers.fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.balysv.materialmenu.MaterialMenuDrawable;
import com.balysv.materialmenu.MaterialMenuView;
import com.vinsol.spree.R;
import com.vinsol.spree.api.ApiClient;
import com.vinsol.spree.api.models.ErrorResponse;
import com.vinsol.spree.api.models.PasswordResetResponse;
import com.vinsol.spree.controllers.Home;
import com.vinsol.spree.models.EmailWrapper;
import com.vinsol.spree.models.PasswordChange;
import com.vinsol.spree.models.User;
import com.vinsol.spree.utils.Common;
import com.vinsol.spree.api.ErrorUtils;
import com.vinsol.spree.utils.Log;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PasswordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PasswordFragment extends BaseFragment {
    public static final int MODE_CHANGE_PASSWORD = -40;
    public static final int MODE_RESET_PASSWORD = -50;

    private static final String TAG = "PASSWORD";
    private Home home;
    private MaterialMenuView back;
    private EditText email;
    private TextView reset, header;
    private RelativeLayout parent;
    private EditText currentPwd, newPwd;
    private ImageView showCurrentPwd, showNewPwd;
    private ImageButton changePwdBtn;
    private int mode;
    private String userToken;
    private boolean isCurrentPwdShown = false;
    private boolean isNewPwdShown = false;
    private static final String MODE = "mode";
    private static final String USER_TOKEN = "user_token";

    public PasswordFragment() {
        // Required empty public constructor
    }


    public static PasswordFragment newInstance(int mode, String token) {
        PasswordFragment fragment = new PasswordFragment();
        Bundle args = new Bundle();
        args.putInt(MODE, mode);
        args.putString(USER_TOKEN, token);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public String getTagText() {
        return TAG;
    }

    @Override
    public boolean onBackPressed() {
        Log.d("PasswordFragment : onBackPressed");
        home.popBackStack();
        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mode = getArguments().getInt(MODE);
            userToken = getArguments().getString(USER_TOKEN);
        }
        home = (Home) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_password, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI(view);
        setListeners();
        setupViewBasedOnMode();
    }

    private void initUI(View view) {
        // common
        parent          = (RelativeLayout)   view.findViewById(R.id.fragment_password_container);
        header          = (TextView)         view.findViewById(R.id.fragment_password_tab_bar_txt);
        back            = (MaterialMenuView) view.findViewById(R.id.fragment_password_tab_bar_back_img);
        back.setState(MaterialMenuDrawable.IconState.X);
        // reset pwd
        email           = (EditText)         view.findViewById(R.id.fragment_password_email_txt);
        reset           = (TextView)         view.findViewById(R.id.fragment_password_reset_btn);
        // change pwd
        currentPwd      = (EditText)         view.findViewById(R.id.fragment_password_current_txt);
        newPwd          = (EditText)         view.findViewById(R.id.fragment_password_new_txt);
        showCurrentPwd  = (ImageView)        view.findViewById(R.id.fragment_password_show_current_pwd_img);
        showNewPwd      = (ImageView)        view.findViewById(R.id.fragment_password_show_new_pwd_img);
        changePwdBtn    = (ImageButton)      view.findViewById(R.id.fragment_password_change_btn);
        currentPwd.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
        newPwd.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
    }

    private void setListeners() {
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(email.getText()) && Patterns.EMAIL_ADDRESS.matcher(email.getText()).matches()) {
                    resetPassword();
                }
                else email.setError("Invalid Email");
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        showCurrentPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isCurrentPwdShown) {
                    currentPwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    isCurrentPwdShown = true;
                }
                else {
                    currentPwd.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                    isCurrentPwdShown = false;
                }
            }
        });

        showNewPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isNewPwdShown) {
                    newPwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    isNewPwdShown = true;
                }
                else {
                    newPwd.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                    isNewPwdShown = false;
                }
            }
        });

        changePwdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(currentPwd.getText())) {
                    if (!TextUtils.isEmpty(newPwd.getText())) {
                        changePassword();
                    }
                    else newPwd.setError("New password cannot be empty");
                }
                else currentPwd.setError("New password cannot be empty");
            }
        });
    }

    private void setupViewBasedOnMode() {
        if (mode== MODE_RESET_PASSWORD) {
            header.setText(home.getResources().getString(R.string.reset_password));
            email.setVisibility(View.VISIBLE);
            reset.setVisibility(View.VISIBLE);
        }
        else {
            header.setText(home.getResources().getString(R.string.change_password));
            currentPwd.setVisibility(View.VISIBLE);
            newPwd.setVisibility(View.VISIBLE);
            showCurrentPwd.setVisibility(View.VISIBLE);
            showNewPwd.setVisibility(View.VISIBLE);
            changePwdBtn.setVisibility(View.VISIBLE);
        }
    }

    private void resetPassword() {
        Common.hideKeyboard(home);
        EmailWrapper emailWrapper = new EmailWrapper();
        emailWrapper.setEmail(email.getText().toString());
        Call<PasswordResetResponse> call = ApiClient.getInstance().getApiService().resetPwd(emailWrapper);
        call.enqueue(new Callback<PasswordResetResponse>() {
            @Override
            public void onResponse(Response<PasswordResetResponse> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    Log.d("Reset pwd success");
                    PasswordResetResponse resetResponse = response.body();
                    showSuccessMessage(resetResponse.getMessage());
                } else {
                    Log.d("Reset pwd error user not exist");
                    showErrorMessage(response.message());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("Reset pwd onFailure");
                showErrorMessage(home.getResources().getString(R.string.api_call_failure_error));
            }
        });
    }

    private void showSuccessMessage(String msg) {
        Snackbar snackbar = Snackbar.make(parent, msg, Snackbar.LENGTH_LONG)
                .setAction("Ok", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });
        snackbar.setActionTextColor(Color.RED);
        snackbar.show();
    }

    private void showErrorMessage(String msg) {
        Snackbar snackbar = Snackbar.make(parent, msg, Snackbar.LENGTH_SHORT)
                .setAction("Ok", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Do nothing
                    }
                });
        snackbar.setActionTextColor(Color.RED);
        snackbar.show();
    }

    private void changePassword() {
        Common.hideKeyboard(home);
        PasswordChange passwordChangeObject = new PasswordChange();
        passwordChangeObject.setToken(userToken);
        passwordChangeObject.setCurrentPwd(currentPwd.getText().toString());
        passwordChangeObject.setPwd(newPwd.getText().toString());
        Call<User> call = ApiClient.getInstance().getApiService().changePwd(passwordChangeObject);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Response<User> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    showSuccessMessage("Password change successful");
                }
                else {
                    ErrorResponse errorResponse = ErrorUtils.parseError(response, retrofit);
                    if (errorResponse.getErrors().getCurrentPasswords() != null && errorResponse.getErrors().getCurrentPasswords().get(0).equalsIgnoreCase("is invalid")) {
                        showErrorMessage("Current password entered is invalid");
                    }
                    if (errorResponse.getErrors().getPassword() != null && errorResponse.getErrors().getPassword().get(0).equalsIgnoreCase("is too short (minimum is 6 characters)")) {
                        showErrorMessage("New password should be min 6 characters");
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                showErrorMessage(home.getResources().getString(R.string.api_call_failure_error));
            }
        });
    }
}

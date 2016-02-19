package com.vinsol.spree.controllers.fragments;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.balysv.materialmenu.MaterialMenuDrawable;
import com.balysv.materialmenu.MaterialMenuView;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.vinsol.spree.R;
import com.vinsol.spree.api.ApiClient;
import com.vinsol.spree.api.ErrorUtils;
import com.vinsol.spree.api.models.ErrorResponse;
import com.vinsol.spree.cache.Cache;
import com.vinsol.spree.controllers.Home;
import com.vinsol.spree.events.RefreshCartEvent;
import com.vinsol.spree.events.SignupSuccessfulEvent;
import com.vinsol.spree.events.UserLoggedInEvent;
import com.vinsol.spree.models.Order;
import com.vinsol.spree.models.SocialProfile;
import com.vinsol.spree.models.User;
import com.vinsol.spree.utils.BusProvider;
import com.vinsol.spree.utils.Common;
import com.vinsol.spree.utils.Log;
import com.vinsol.spree.utils.SharedPreferencesHelper;
import com.vinsol.spree.views.CircleTransformation;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import retrofit.Call;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by vaibhav on 11/2/15.
 */
public class ProfileFragment extends BaseFragment implements GoogleApiClient.OnConnectionFailedListener {
    private static final String PROVIDER_FACEBOOK = "Facebook";
    private static final String PROVIDER_GOOGLE = "Google";

    private static final String TAG = "PROFILE";
    private Home home;
    private MaterialMenuView back;
    private LinearLayout tabBar;
    private CallbackManager callbackManager;
    private GoogleApiClient googleApiClient;
    private User user;
    private boolean isTabBarShown;
    private static final String SHOW_TAB_BAR = "SHOW_TAB_BAR";
    private RelativeLayout container, pbContainer, loggedInContainer, loggedOutContainer, editContainer;
    // loggedOutContainer
    private EditText email, password;
    private TextView facebookTxt, login, signup, googleTxt, forgotPwd;
    private ImageView facebookImg, googleImg;
    // loggedInContainer
    private RelativeLayout editProfile;
    private ImageView profileImg;
    private TextView profileName, logout;
    private CardView myOrders, myAddresses, myWislist, savedCards;
    // editContainer
    private ImageView editProfileImg;
    private TextView editProfileNameTxt, editProfileEmailTxt, changePassword;
    private EditText editName, editPhone;
    private ImageButton saveProfile;

    private GoogleSignInOptions gso;
    private static final List<String> READ_PERMISSIONS = Arrays.asList("public_profile", "email");
    private static final int RC_SIGN_IN =4;
    private static final int USER_LOGIN_NORMAL   = 1001;
    private static final int USER_LOGIN_FACEBOOK = 1002;
    private static final int USER_LOGIN_GOOGLE   = 1003;
    private int loggedInMode = 1000;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ProfileFragment.
     */
    public static ProfileFragment newInstance(boolean showTabBar) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putBoolean(SHOW_TAB_BAR, showTabBar);
        fragment.setArguments(args);
        return fragment;
    }

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public String getTagText() {
        return TAG;
    }

    @Override
    public boolean onBackPressed() {
        if (isTabBarShown) {
            home.popBackStack();
            return true;
        }
        return false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments()!=null) {
            isTabBarShown = getArguments().getBoolean(SHOW_TAB_BAR);
        }
        BusProvider.getInstance().register(this);
        home = (Home) getActivity();
        user = new User();
        // manages the callbacks into the FacebookSdk from an Activity's or Fragment's onActivityResult() method
        callbackManager = CallbackManager.Factory.create();
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestProfile()
                .requestEmail()
                .build();

        if (googleApiClient==null) {
            googleApiClient = new GoogleApiClient.Builder(home)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        if (googleApiClient != null)
            googleApiClient.connect();
    }

    @Override
    public void onStop() {
        if (googleApiClient != null && googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BusProvider.getInstance().unregister(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI(view);
        setListeners();
        showHideTabBar();
        if (SharedPreferencesHelper.getCache() != null && SharedPreferencesHelper.getCache().getUser()!=null) {
            user = SharedPreferencesHelper.getCache().getUser();
            userLoggedIn();
        }
    }

    private void initUI(View view) {
        tabBar             = (LinearLayout)   view.findViewById(R.id.fragment_profile_tab_bar);
        container          = (RelativeLayout) view.findViewById(R.id.fragment_profile_container);
        pbContainer        = (RelativeLayout) view.findViewById(R.id.progress_bar_container);
        back               = (MaterialMenuView) view.findViewById(R.id.fragment_profile_tab_bar_back_img);
        back.setState(MaterialMenuDrawable.IconState.ARROW);
        // logged out view
        loggedOutContainer = (RelativeLayout) view.findViewById(R.id.fragment_profile_user_logged_out_container);
        email              = (EditText)       view.findViewById(R.id.profile_email_txt);
        password           = (EditText)       view.findViewById(R.id.profile_pwd_txt);
        login              = (TextView)       view.findViewById(R.id.profile_login_btn);
        forgotPwd          = (TextView)       view.findViewById(R.id.profile_forgot_pwd_btn);
        signup             = (TextView)       view.findViewById(R.id.profile_signup_btn);
        facebookTxt        = (TextView)       view.findViewById(R.id.profile_facebook_txt);
        googleTxt          = (TextView)       view.findViewById(R.id.profile_google_txt);
        facebookImg        = (ImageView)      view.findViewById(R.id.profile_facebook_img);
        googleImg          = (ImageView)      view.findViewById(R.id.profile_google_img);
        // logged in view
        loggedInContainer  = (RelativeLayout) view.findViewById(R.id.fragment_profile_user_logged_in_container);
        profileImg         = (ImageView)      view.findViewById(R.id.profile_user_img);
        profileName        = (TextView)       view.findViewById(R.id.profile_user_name);
        editProfile        = (RelativeLayout) view.findViewById(R.id.profile_edit_img_container);
        myOrders           = (CardView)       view.findViewById(R.id.profile_user_orders_card);
        myAddresses        = (CardView)       view.findViewById(R.id.profile_user_addresses_card);
        savedCards         = (CardView)       view.findViewById(R.id.profile_user_payment_card);
        myWislist          = (CardView)       view.findViewById(R.id.profile_user_wishlist_card);
        logout             = (TextView)       view.findViewById(R.id.profile_logout_btn);
        // edit profile view
        editContainer      = (RelativeLayout) view.findViewById(R.id.fragment_profile_user_edit_container);
        editProfileImg     = (ImageView)      view.findViewById(R.id.profile_edit_user_img);
        editProfileNameTxt = (TextView)       view.findViewById(R.id.profile_edit_user_name_txt);
        editProfileEmailTxt= (TextView)       view.findViewById(R.id.profile_edit_user_email_txt);
        editName           = (EditText)       view.findViewById(R.id.profile_edit_user_name);
        editPhone          = (EditText)       view.findViewById(R.id.profile_edit_user_number);
        changePassword     = (TextView)       view.findViewById(R.id.profile_edit_user_change_pwd_btn);
        saveProfile        = (ImageButton)    view.findViewById(R.id.profile_edit_user_save_btn);
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
        // logged out view
        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (!TextUtils.isEmpty(email.getText()) && Patterns.EMAIL_ADDRESS.matcher(email.getText()).matches()) {
                        user.setEmail(email.getText().toString());
                    } else email.setError("Invalid Email");
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.hideKeyboard(home);
                if (TextUtils.isEmpty(password.getText())) {
                    password.setError("Password cannot be empty");
                } else {
                    pbContainer.setVisibility(View.VISIBLE);
                    user.setPassword(password.getText().toString());
                    loggedInMode = USER_LOGIN_NORMAL;
                    loginUser(user.getProvider() != null);
                }
            }
        });

        forgotPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.hideKeyboard(home);
                home.showPasswordFragment(PasswordFragment.MODE_RESET_PASSWORD, null);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.hideKeyboard(home);
                home.showSignupFragment();
            }
        });

        facebookImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginWithFacebook();
            }
        });
        facebookTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginWithFacebook();
            }
        });
        googleImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginWithGoogle();
            }
        });
        googleTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginWithGoogle();
            }
        });

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("FB Login success");
                final AccessToken accessToken = AccessToken.getCurrentAccessToken();
                final Profile profile = Profile.getCurrentProfile();
                pbContainer.setVisibility(View.VISIBLE);
                GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {

                        try {
                            String imgUrl = "https://graph.facebook.com/" + profile.getId() + "/picture";
                            SocialProfile socialProfile = new SocialProfile(PROVIDER_FACEBOOK, profile.getId(), imgUrl);
                            user = new User(object.get("email").toString(), profile.getName(), socialProfile);
                            loggedInMode = USER_LOGIN_FACEBOOK;
                            loginUser(user.getProvider() != null);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,picture.width(480).height(480)");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Log.d("FB Login cancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("FB Login error" + error.getMessage());
                LoginManager.getInstance().logOut();
            }
        });

        // logged in view
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loggedInContainer.setVisibility(View.GONE);
                editContainer.setVisibility(View.VISIBLE);
                setupEditProfileView();
            }
        });

        myOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                home.showOrdersFragment(user.getApiKey());
            }
        });

        myAddresses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                home.showAddressFragment(user.getApiKey(), false, null);
            }
        });

        savedCards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                home.showPaymentFragment(user.getApiKey(), false, null);
            }
        });

        myWislist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: open wishlist fragment v2
                Toast.makeText(home, "My Wishlist clicked", Toast.LENGTH_SHORT).show();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = new User();
                cacheUser(null);
                userLoggedOut();
            }
        });

        // edit profile view
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                home.showPasswordFragment(PasswordFragment.MODE_CHANGE_PASSWORD, user.getApiKey());
            }
        });

        saveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateEditForm()) patchEditProfileRequest();
            }
        });
    }

    private void showHideTabBar() {
        if (isTabBarShown) tabBar.setVisibility(View.VISIBLE);
        else tabBar.setVisibility(View.GONE);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    // -----------------Social login methods---------------

    private void loginWithFacebook() {
        LoginManager.getInstance().logInWithReadPermissions(ProfileFragment.this, READ_PERMISSIONS);
    }

    private void loginWithGoogle() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    // -----------------------------------------------------

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
        else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }


    // login user in our system
    private void loginUser(final boolean hasProvider) {
        Call<User> call = ApiClient.getInstance().getApiService().login(user);
        call.enqueue(new retrofit.Callback<User>() {
            @Override
            public void onResponse(Response<User> response, Retrofit retrofit) {
                if (response.isSuccess()) { // Login on server is successful
                    Log.d("User Login success");
                    pbContainer.setVisibility(View.GONE);
                    user = response.body();
                    setValuesBasedOnLoggedInMode();
                    cacheUser(user);
                    userLoggedIn();
                } else { // Login on server failed due to some error
                    ErrorResponse errorResponse = ErrorUtils.parseError(response, retrofit);
                    if (hasProvider) {
                        if (errorResponse.getErrors().getEmail() != null && errorResponse.getErrors().getEmail().get(0).equalsIgnoreCase("not found")) {
                            signupUser();
                        }
                    } else {
                        pbContainer.setVisibility(View.GONE);
                        handleError("Invalid Username/Password");
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                pbContainer.setVisibility(View.GONE);
                Log.d("User Login failure" + t.getMessage());
                handleError(home.getResources().getString(R.string.api_call_failure_error));
            }
        });
    }

    // handle error from api
    private void handleError(String msg) {

        Snackbar snackbar = Snackbar.make(container, msg, Snackbar.LENGTH_INDEFINITE)
                .setAction("Ok", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
        snackbar.setActionTextColor(Color.RED);
        snackbar.show();
    }

    // handle google sign in result
    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            Log.d("G + login success");
            GoogleSignInAccount acct = result.getSignInAccount();
            SocialProfile socialProfile = new SocialProfile(PROVIDER_GOOGLE, acct.getId(), acct.getPhotoUrl()==null?null:acct.getPhotoUrl().toString());
            user = new User(acct.getEmail(), acct.getDisplayName(), socialProfile);
            loggedInMode = USER_LOGIN_GOOGLE;
            loginUser(user.getProvider() != null);
        } else {
            // Signed out, show unauthenticated UI.
            Log.d("G + logout");
        }
    }

    // sign up user in our system only if user tries to login from
    private void signupUser() {
        Call<User> call = ApiClient.getInstance().getApiService().signup(user);
        call.enqueue(new retrofit.Callback<User>() {
            @Override
            public void onResponse(Response<User> response, Retrofit retrofit) {
                pbContainer.setVisibility(View.GONE);
                if (response.isSuccess()) {
                    Log.d("User signed up");
                    user = response.body();
                    setValuesBasedOnLoggedInMode();
                    cacheUser(user);
                    userLoggedIn();
                }
                else {
                    Log.d("User not signed up");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("User signed up failure");
            }
        });
    }

    private void setValuesBasedOnLoggedInMode() {
        switch (loggedInMode) {
            case USER_LOGIN_NORMAL : break;
            case USER_LOGIN_FACEBOOK :
                for (SocialProfile socialProfile : user.getProfiles()) {
                    if (socialProfile.getProvider().equalsIgnoreCase(PROVIDER_FACEBOOK)) {
                        user.setUid(socialProfile.getUid());
                        user.setProvider(socialProfile.getProvider());
                        String image = socialProfile.getImage() + "?width=" + Common.convertDpToPixel(home, 100) + "&height=" + Common.convertDpToPixel(home, 100);
                        user.setProfilePicUrl(image);
                    }
                }
                break;
            case USER_LOGIN_GOOGLE :
                for (SocialProfile socialProfile : user.getProfiles()) {
                    if (socialProfile.getProvider().equalsIgnoreCase(PROVIDER_GOOGLE)) {
                        user.setUid(socialProfile.getUid());
                        user.setProvider(socialProfile.getProvider());
                        user.setProfilePicUrl(socialProfile.getImage());
                    }
                }
                break;
        }
    }

    private void userLoggedIn() {
        if(isTabBarShown) {
            BusProvider.getInstance().post(new UserLoggedInEvent());
            home.popBackStack();
            return;
        }

        retrieveUserCart();
        loggedOutContainer.setVisibility(View.GONE);
        loggedInContainer.setVisibility(View.VISIBLE);
        showUserPicAndName(profileImg, profileName);
    }

    private void showUserPicAndName(ImageView imageView, TextView textView) {
        if (loggedInMode == USER_LOGIN_NORMAL) {
            Picasso.with(home).load(R.drawable.profile_pic_placeholder).transform(new CircleTransformation())
                    .into(imageView);
        }
        else {
            Picasso.with(home).load(user.getProfilePicUrl())
                    .transform(new CircleTransformation())
                    .placeholder(R.drawable.profile_pic_placeholder)
                    .resize(Common.convertDpToPixel(home, 100), Common.convertDpToPixel(home, 100))
                    .centerCrop()
                    .into(imageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            Log.d("Picasso profile pic load Success");
                        }

                        @Override
                        public void onError() {
                            Log.d("Picasso profile pic load Error");
                        }
                    });
        }
        textView.setText(user.getName());
    }

    private void userLoggedOut() {
        loggedInContainer.setVisibility(View.GONE);
        loggedOutContainer.setVisibility(View.VISIBLE);

        BusProvider.getInstance().post(new RefreshCartEvent(null));
    }

    @Subscribe
    public void onSignupSuccessfulEvent(SignupSuccessfulEvent event) {
        user = event.user;
        home.popBackStack();
        loggedInMode = USER_LOGIN_NORMAL;
        setValuesBasedOnLoggedInMode();
        cacheUser(user);
        userLoggedIn();
    }

    @Subscribe
    public void onUserLoggedInEvent(UserLoggedInEvent event) {
        if(!isTabBarShown) {
            if (SharedPreferencesHelper.getCache() != null && SharedPreferencesHelper.getCache().getUser() != null) {
                user = SharedPreferencesHelper.getCache().getUser();
                userLoggedIn();
            }
        }
    }

    private void cacheUser(User user) {
        Cache cache = Cache.get();
        cache.setUser(user);
        SharedPreferencesHelper.saveCache(cache);
    }

    // setup edit container
    private void setupEditProfileView() {
        showUserPicAndName(editProfileImg, editProfileNameTxt);
        editProfileEmailTxt.setText(user.getEmail());
        editName.setText(user.getName());
        editPhone.setText(user.getPhone());
    }

    private boolean validateEditForm() {
        if (!TextUtils.isEmpty(editName.getText())) {
            if (Patterns.PHONE.matcher(editPhone.getText()).matches()) return true;
            else {
                editPhone.setError("Enter a valid 10 digit phone number");
                return false;
            }
        }
        editName.setError("Name cannot be empty");
        return false;

    }

    private void patchEditProfileRequest() {
        User userToBeSent = new User();
        userToBeSent.setName(editName.getText().toString());
        userToBeSent.setPhone(editPhone.getText().toString());
        pbContainer.setVisibility(View.VISIBLE);
        Call<User> call = ApiClient.getInstance().getApiService().editUser(user.getApiKey(), userToBeSent);
        call.enqueue(new retrofit.Callback<User>() {
            @Override
            public void onResponse(Response<User> response, Retrofit retrofit) {
                pbContainer.setVisibility(View.GONE);
                if (response.isSuccess()) {
                    Log.d("Edit User success");
                    User editedUser = response.body();
                    user.setPhone(editedUser.getPhone());
                    user.setName(editedUser.getName());
                    editContainer.setVisibility(View.GONE);
                    loggedInContainer.setVisibility(View.VISIBLE);
                    profileName.setText(user.getName());
                } else {
                    Log.d("Edit User error");
                    //TODO : handle error with snackbar
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("Edit User failure");
                Toast.makeText(home, "User sign up failure. Pls try again ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void retrieveUserCart() {
        Call<Order> call = ApiClient.getInstance().getApiService().getCurrentOrder(user.getApiKey());
        call.enqueue(new retrofit.Callback<Order>() {
            @Override
            public void onResponse(Response<Order> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    Log.d("Response success > getCart");
                    Order order = response.body();
                    if (order!=null) {
                        SharedPreferencesHelper.saveTotalItems(order.getItemCount());
                        onBackPressed();
                        BusProvider.getInstance().post(new RefreshCartEvent(order));
                    }
                }
                else {
                    Log.d("Response not success. Check for error");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("Response failure.");
            }
        });
    }
}

package com.vinsol.spree.controllers.fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.balysv.materialmenu.MaterialMenuDrawable;
import com.balysv.materialmenu.MaterialMenuView;
import com.squareup.otto.Subscribe;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.exception.AuthenticationException;
import com.stripe.android.model.Token;
import com.vinsol.spree.R;
import com.vinsol.spree.api.ApiClient;
import com.vinsol.spree.api.models.CardWrapper;
import com.vinsol.spree.controllers.Home;
import com.vinsol.spree.events.CardsListOptionClickEvent;
import com.vinsol.spree.events.CheckoutCardSelectedEvent;
import com.vinsol.spree.models.Card;
import com.vinsol.spree.utils.BusProvider;
import com.vinsol.spree.utils.Common;
import com.vinsol.spree.utils.Constants;
import com.vinsol.spree.utils.Log;
import com.vinsol.spree.utils.Strings;
import com.vinsol.spree.viewhelpers.CardsCustomAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by vaibhav on 12/21/15.
 */
public class PaymentFragment extends BaseFragment {
    private static final String TAG = "PAYMENT";
    private Home home;
    private MaterialMenuView back;
    private TextView title;
    private LinearLayout noCardFoundContainer, addContainer;
    private FloatingActionButton addCardBtn;
    private ListView cardsListView;
    private ArrayList<Card> cards = new ArrayList<>();
    private RelativeLayout pbContainer;
    private static final int MODE_ADD = 2;
    private static final int MODE_VIEW = 1;
    private int mode=MODE_VIEW;
    private String userToken;
    private boolean isSelector;
    private CardsCustomAdapter adapter;
    private Card selectedCard, selectedCardForCheckout;
    private Card card;
    private com.stripe.android.model.Card stripeCard;
    private static final String USER_TOKEN = "user_token";
    private static final String IS_SELECTOR = "is_selector";
    private static final String SELECTED_CARD_FOR_CHECKOUT = "selected_card_for_checkout";
    // Add mode
    private EditText cardNumber, name, month, year, cvv;
    private ImageButton save;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AddressFragment.
     */
    public static PaymentFragment newInstance(String token, boolean isSelector, Card selectedCardForCheckout) {
        PaymentFragment fragment = new PaymentFragment();
        Bundle args = new Bundle();
        args.putString(USER_TOKEN, token);
        args.putBoolean(IS_SELECTOR, isSelector);
        args.putSerializable(SELECTED_CARD_FOR_CHECKOUT, selectedCardForCheckout);
        fragment.setArguments(args);
        return fragment;
    }

    public PaymentFragment() {
        // Required empty public constructor
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
        if (getArguments()!=null) {
            userToken = getArguments().getString(USER_TOKEN);
            isSelector = getArguments().getBoolean(IS_SELECTOR);
            selectedCardForCheckout = getArguments().getSerializable(SELECTED_CARD_FOR_CHECKOUT)==null ? null : (Card) getArguments().getSerializable(SELECTED_CARD_FOR_CHECKOUT);
        }
        home = (Home) getActivity();
        BusProvider.getInstance().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_payment, container, false);
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
        loadCards();
        setValuesBasedOnMode();
    }

    private void initUI(View view) {
       // common
        pbContainer             = (RelativeLayout)       view.findViewById(R.id.progress_bar_container);
        back                    = (MaterialMenuView)     view.findViewById(R.id.fragment_payment_tab_bar_back_img);
        title                   = (TextView)             view.findViewById(R.id.fragment_payment_tab_bar_txt);
        back.setState(MaterialMenuDrawable.IconState.ARROW);
        // View mode
        noCardFoundContainer    = (LinearLayout)         view.findViewById(R.id.fragment_payment_blank_container);
        addCardBtn              = (FloatingActionButton) view.findViewById(R.id.fragment_payment_fab);
        cardsListView           = (ListView)             view.findViewById(R.id.fragment_payment_list_view);
        // ADD or EDIT mode
        addContainer            = (LinearLayout)         view.findViewById(R.id.fragment_payment_form_container);
        cardNumber              = (EditText)             view.findViewById(R.id.fragment_payment_card_number_txt);
        name                    = (EditText)             view.findViewById(R.id.fragment_payment_name_txt);
        month                   = (EditText)             view.findViewById(R.id.fragment_payment_month_txt);
        year                    = (EditText)             view.findViewById(R.id.fragment_payment_year_txt);
//        cvv                     = (EditText)             view.findViewById(R.id.fragment_payment_cvv_txt);
        save                    = (ImageButton)          view.findViewById(R.id.fragment_payment_save_btn);
    }

    private void setValuesBasedOnMode() {
        switch (mode) {
            case MODE_VIEW: title.setText(home.getResources().getString(R.string.payment_methods));
                break;
            case MODE_ADD: title.setText(home.getResources().getString(R.string.add_payment_method));
                cardNumber.setText("");
                name.setText("");
                month.setText("");
                year.setText("");
                break;
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
        addCardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mode = MODE_ADD;
                showAddModeContent();
            }
        });
        cardNumber.addTextChangedListener(new FourDigitCardFormatWatcher());
        cardsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isSelector) {
                    for (Card card : cards) {
                        card.setIsSelected(false);
                    }
                    cards.get(position).setIsSelected(true);
                    adapter.notifyDataSetChanged();
                    BusProvider.getInstance().post(new CheckoutCardSelectedEvent(cards.get(position).getId()));
                    home.popBackStack();
                }
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.hideKeyboard(home);
                if (validateForm()) addCardToStripe();
            }
        });
    }

    private void loadCards() {
        showLoader();
        Call<List<Card>> call = ApiClient.getInstance().getApiService().getUserCards(userToken);
        call.enqueue(new Callback<List<Card>>() {
            @Override
            public void onResponse(Response<List<Card>> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    hideLoader();
                    cards = (ArrayList<Card>) response.body();
                    if (cards.isEmpty()) {
                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) addCardBtn.getLayoutParams();
                        params.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                        params.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                        params.setMargins(0, (int) home.getResources().getDimension(R.dimen.margin_10dp), 0, 0);
                        params.addRule(RelativeLayout.BELOW, R.id.fragment_payment_blank_container);
                        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
                        addCardBtn.setLayoutParams(params);
                        cardsListView.setVisibility(View.GONE);
                        noCardFoundContainer.setVisibility(View.VISIBLE);
                    } else {
                        if (isSelector && selectedCardForCheckout!=null) {
                            for (Card card :
                                    cards) {
                                if (card.getId().equals(selectedCardForCheckout.getId()))
                                    card.setIsSelected(true);
                            }
                        }
                        adapter = new CardsCustomAdapter(home, R.layout.cards_list_view_item, cards);
                        cardsListView.setAdapter(adapter);
                        cardsListView.setVisibility(View.VISIBLE);
                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) addCardBtn.getLayoutParams();
                        params.removeRule(RelativeLayout.BELOW);
                        params.removeRule(RelativeLayout.CENTER_HORIZONTAL);
                        params.setMargins(0, 0, (int) home.getResources().getDimension(R.dimen.margin_15dp), (int) home.getResources().getDimension(R.dimen.margin_15dp));
                        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                        addCardBtn.setLayoutParams(params);
                    }
                } else {
                    hideLoader();
                    handleError(response.message());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoader();
                handleError(home.getResources().getString(R.string.api_call_failure_error));
            }
        });
    }
    private boolean validateForm() {
        if(Strings.isEmpty(name.getText().toString())) {
            name.setError("Name can't be blank");
            return false;
        }

        if(Strings.isEmpty(cardNumber.getText().toString())) {
            cardNumber.setError("Number can't be blank");
            return false;
        }

        if(Strings.isEmpty(month.getText().toString())) {
            month.setError("Month not valid");
            return false;
        }

        if(Strings.isEmpty(year.getText().toString())) {
            year.setError("Year not valid");
            return false;
        }

        stripeCard = new com.stripe.android.model.Card(cardNumber.getText().toString(), Integer.parseInt(month.getText().toString()), Integer.parseInt(year.getText().toString()), null);
        if (!stripeCard.validateNumber()) {
            cardNumber.setError("Number not valid");
            return false;
        }
        if (!stripeCard.validateExpiryDate()) {
            year.setError("Year not valid");
            return false;
        }

        card = new Card(stripeCard.getLast4(), stripeCard.getExpMonth(), stripeCard.getExpYear(), stripeCard.getType());
        String cardNum = cardNumber.getText().toString().replace("-", "");
        card.setNumber(cardNum);
        return true;
    }

    private void handleError(String msg) {
        Toast.makeText(home, msg, Toast.LENGTH_LONG).show();
    }

    private void showLoader() {
        pbContainer.setVisibility(View.VISIBLE);
        addCardBtn.setVisibility(View.GONE);
    }

    private void hideLoader() {
        pbContainer.setVisibility(View.GONE);
        if (mode==MODE_VIEW) addCardBtn.setVisibility(View.VISIBLE);
    }

    private void showAddModeContent() {
        noCardFoundContainer.setVisibility(View.GONE);
        cardsListView.setVisibility(View.GONE);
        addCardBtn.setVisibility(View.GONE);
        addContainer.setVisibility(View.VISIBLE);
        setValuesBasedOnMode();
    }

    @Subscribe
    public void onCardRemoveClickEvent(CardsListOptionClickEvent event) {
        selectedCard = event.card;
        mode = MODE_VIEW;
        setValuesBasedOnMode();
        removeCard();

    }

    private void addCardToStripe() {
        showLoader();
        try {
            Stripe stripe = new Stripe(Constants.STRIPE_APP_KEY);
            stripe.createToken(stripeCard, new TokenCallback() {
                @Override
                public void onError(Exception error) {
                    hideLoader();
                    handleError(error.getLocalizedMessage());
                }

                @Override
                public void onSuccess(Token token) {
                    hideLoader();
                    card.setStripeTokenId(token.getId());
                    Log.d("Stripe token : " + token.getId());
                    addCard();
                }
            });
        } catch (AuthenticationException e) {
            e.printStackTrace();
        }
    }

    private void addCard() {
        showLoader();
        CardWrapper cardWrapper = new CardWrapper(card);
        Call<Card> call = ApiClient.getInstance().getApiService().createCard(userToken, cardWrapper);
        call.enqueue(new Callback<Card>() {
            @Override
            public void onResponse(Response<Card> response, Retrofit retrofit) {
                hideLoader();
                if (response.isSuccess()) {
                    handleError("Card added");
                    addContainer.setVisibility(View.GONE);
                    loadCards();
                    mode = MODE_VIEW;
                    setValuesBasedOnMode();
                } else {
                    handleError(response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoader();
                Log.d("Card add failure > " + t.getLocalizedMessage());
                handleError(home.getResources().getString(R.string.api_call_failure_error));
            }
        });
    }

    private void removeCard() {
        showLoader();
        Call<Card> call = ApiClient.getInstance().getApiService().removeCard(selectedCard.getId(), userToken);
        call.enqueue(new Callback<Card>() {
            @Override
            public void onResponse(Response<Card> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    handleError("Card removed");
                    loadCards();
                    mode = MODE_VIEW;
                    setValuesBasedOnMode();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoader();
                Log.d("Card remove failure > " + t.getLocalizedMessage());
                handleError(home.getResources().getString(R.string.api_call_failure_error));
            }
        });
    }

    class FourDigitCardFormatWatcher implements TextWatcher {

        // Change this to what you want... ' ', '-' etc..
        private static final char space = '-';

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            // Remove spacing char
            if (s.length() > 0 && (s.length() % 5) == 0) {
                final char c = s.charAt(s.length() - 1);
                if (space == c) {
                    s.delete(s.length() - 1, s.length());
                }
            }
            // Insert char where needed.
            if (s.length() > 0 && (s.length() % 5) == 0) {
                char c = s.charAt(s.length() - 1);
                // Only if its a digit where there should be a space we insert a space
                if (Character.isDigit(c) && TextUtils.split(s.toString(), String.valueOf(space)).length <= 3) {
                    s.insert(s.length() - 1, String.valueOf(space));
                }
            }
        }
    }
}

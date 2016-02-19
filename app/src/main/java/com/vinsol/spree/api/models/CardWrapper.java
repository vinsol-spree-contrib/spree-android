package com.vinsol.spree.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vinsol.spree.models.Card;

import java.io.Serializable;

/**
 * Created by vaibhav on 12/24/15.
 */
public class CardWrapper implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("credit_card")
    @Expose
    private Card card;

    public CardWrapper(Card card) {
        this.card = card;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }
}

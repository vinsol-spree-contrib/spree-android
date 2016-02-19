package com.vinsol.spree.events;

import com.vinsol.spree.models.Card;

/**
 * Created by vaibhav on 12/21/15.
 */
public class CardsListOptionClickEvent {
    public Card card;

    public CardsListOptionClickEvent(Card card) {
        this.card = card;
    }
}

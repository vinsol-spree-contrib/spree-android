package com.vinsol.spree.events;

/**
 * Created by vaibhav on 12/4/15.
 */
public class OpenProductDetailEvent {
    public int productId;

    public OpenProductDetailEvent(int productId) {
        this.productId = productId;
    }
}

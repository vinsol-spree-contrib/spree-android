package com.vinsol.spree.viewhelpers;

import com.vinsol.spree.models.LineItem;

public interface CartLineItemActionsInterface {
    public void editQuantity(LineItem lineItem);
    public void removeLineItem(LineItem lineItem);
}

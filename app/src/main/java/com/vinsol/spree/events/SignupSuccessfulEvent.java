package com.vinsol.spree.events;

import com.vinsol.spree.models.User;

/**
 * Created by vaibhav on 12/3/15.
 */
public class SignupSuccessfulEvent {
    public User user;

    public SignupSuccessfulEvent(User user) {
        this.user = user;
    }
}

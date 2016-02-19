package com.vinsol.spree.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vinsol.spree.models.Errors;

import java.io.Serializable;

/**
 * Created by vaibhav on 9/29/15.
 */
public class ErrorResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("errors")
    @Expose
    private Errors errors;

    public Errors getErrors() {
        return errors;
    }

    public void setErrors(Errors errors) {
        this.errors = errors;
    }
}

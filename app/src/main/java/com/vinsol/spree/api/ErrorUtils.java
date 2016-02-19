package com.vinsol.spree.api;

import com.squareup.okhttp.ResponseBody;
import com.vinsol.spree.api.models.ErrorResponse;

import java.io.IOException;
import java.lang.annotation.Annotation;

import retrofit.Converter;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by vaibhav on 11/30/15.
 */
public class ErrorUtils {

    public static ErrorResponse parseError(Response response, Retrofit retrofit) {
        Converter<ResponseBody, ErrorResponse> converter =
                retrofit.responseConverter(ErrorResponse.class, new Annotation[0]);

        ErrorResponse error;

        try {
            error = converter.convert(response.errorBody());
        } catch (IOException e) {
            return new ErrorResponse();
        }

        return error;
    }
}
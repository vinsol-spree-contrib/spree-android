package com.vinsol.spree.models;

import com.google.gson.annotations.SerializedName;

public class RatingsDistribution {

    @SerializedName("1")
    private String one;

    @SerializedName("2")
    private String two;

    @SerializedName("3")
    private String three;

    @SerializedName("4")
    private String four;

    @SerializedName("5")
    private String five;

    public int getOne() {
        if(one == null) {
            return 0;
        }

        try {
            return Integer.parseInt(one);
        } catch (NumberFormatException nfe) {
            return 0;
        }
    }

    public void setOne(String one) {
        this.one = one;
    }

    public int getTwo() {
        if(two == null) {
            return 0;
        }

        try {
            return Integer.parseInt(two);
        } catch (NumberFormatException nfe) {
            return 0;
        }
    }

    public void setTwo(String two) {
        this.two = two;
    }

    public int getThree() {
        if(three == null) {
            return 0;
        }

        try {
            return Integer.parseInt(three);
        } catch (NumberFormatException nfe) {
            return 0;
        }
    }

    public void setThree(String three) {
        this.three = three;
    }

    public int getFour() {
        if(four == null) {
            return 0;
        }

        try {
            return Integer.parseInt(four);
        } catch (NumberFormatException nfe) {
            return 0;
        }
    }

    public void setFour(String four) {
        this.four = four;
    }

    public int getFive() {
        if(five == null) {
            return 0;
        }

        try {
            return Integer.parseInt(five);
        } catch (NumberFormatException nfe) {
            return 0;
        }
    }

    public void setFive(String five) {
        this.five = five;
    }
}
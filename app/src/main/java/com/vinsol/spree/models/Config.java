package com.vinsol.spree.models;


import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Expose;

public class Config {

    @SerializedName("states_checksum")
    @Expose
    private String statesChecksum;

    @SerializedName("home_checksum")
    @Expose
    private String homeChecksum;

    @SerializedName("taxonomies_checksum")
    @Expose
    private String taxonomiesChecksum;

    /**
     *
     * @return
     *     The statesChecksum
     */
    public String getStatesChecksum() {
        return statesChecksum;
    }

    /**
     *
     * @param statesChecksum
     *     The states_checksum
     */
    public void setStatesChecksum(String statesChecksum) {
        this.statesChecksum = statesChecksum;
    }

    /**
     *
     * @return
     *     The homeChecksum
     */
    public String getHomeChecksum() {
        return homeChecksum;
    }

    /**
     *
     * @param homeChecksum
     *     The home_checksum
     */
    public void setHomeChecksum(String homeChecksum) {
        this.homeChecksum = homeChecksum;
    }

    /**
     *
     * @return
     *     The taxonomiesChecksum
     */
    public String getTaxonomiesChecksum() {
        return taxonomiesChecksum;
    }

    /**
     *
     * @param taxonomiesChecksum
     *     The taxonomies_checksum
     */
    public void setTaxonomiesChecksum(String taxonomiesChecksum) {
        this.taxonomiesChecksum = taxonomiesChecksum;
    }
}
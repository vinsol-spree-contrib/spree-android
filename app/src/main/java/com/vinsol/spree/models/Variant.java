package com.vinsol.spree.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * Created by vaibhav on 10/5/15.
 */
//@Parcel
public class Variant implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("price")
    @Expose
    private String price;

    @SerializedName("display_price")
    @Expose
    private String displayPrice;

    @SerializedName("sku")
    @Expose
    private String sku;

    @SerializedName("slug")
    @Expose
    private String slug;

    @SerializedName("options_text")
    @Expose
    private String optionsText;

    @SerializedName("options")
    @Expose
    private HashMap<String, String> options;

    @SerializedName("cost_price")
    @Expose
    private String costPrice;

    @SerializedName("weight")
    @Expose
    private String weight;

    @SerializedName("shipping_category_id")
    @Expose
    private Integer shippingCategoryId;

    @SerializedName("stock_on_hand")
    @Expose
    private Integer stockOnHand;

    @SerializedName("can_supply")
    @Expose
    private Boolean canSupply;

    @SerializedName("is_master")
    @Expose
    private Boolean isMaster;

    @SerializedName("track_inventory")
    @Expose
    private Boolean trackInventory;

    @SerializedName("in_stock")
    @Expose
    private Boolean inStock;

    @SerializedName("backorderable")
    @Expose
    private Boolean isBackOrderable;

    @SerializedName("is_destroyed")
    @Expose
    private Boolean isDestroyed;

    @SerializedName("images")
    @Expose
    private ArrayList<Image> images = new ArrayList<>();

    public Variant(){}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDisplayPrice() {
        return displayPrice;
    }

    public void setDisplayPrice(String displayPrice) {
        this.displayPrice = displayPrice;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public Integer getShippingCategoryId() {
        return shippingCategoryId;
    }

    public void setShippingCategoryId(Integer shippingCategoryId) {
        this.shippingCategoryId = shippingCategoryId;
    }

    public String getOptionsText() {
        return optionsText;
    }

    public void setOptionsText(String optionsText) {
        this.optionsText = optionsText;
    }

    public HashMap<String, String> getOptions() {
        return options;
    }

    public void setOptions(HashMap<String, String> options) {
        this.options = options;
    }

    public String getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(String costPrice) {
        this.costPrice = costPrice;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public Boolean getIsMaster() {
        return isMaster;
    }

    public void setIsMaster(Boolean isMaster) {
        this.isMaster = isMaster;
    }

    public Boolean getTrackInventory() {
        return trackInventory;
    }

    public void setTrackInventory(Boolean trackInventory) {
        this.trackInventory = trackInventory;
    }

    public Boolean getInStock() {
        return inStock;
    }

    public void setInStock(Boolean inStock) {
        this.inStock = inStock;
    }

    public Boolean getIsBackOrderable() {
        return isBackOrderable;
    }

    public void setIsBackOrderable(Boolean isBackOrderable) {
        this.isBackOrderable = isBackOrderable;
    }

    public Boolean getIsDestroyed() {
        return isDestroyed;
    }

    public void setIsDestroyed(Boolean isDestroyed) {
        this.isDestroyed = isDestroyed;
    }

    public ArrayList<Image> getImages() {
        return images;
    }

    public void setImages(ArrayList<Image> images) {
        this.images = images;
    }

    public Integer getStockOnHand() {
        return stockOnHand;
    }

    public void setStockOnHand(Integer stockOnHand) {
        this.stockOnHand = stockOnHand;
    }

    public Boolean getCanSupply() {
        return canSupply;
    }

    public void setCanSupply(Boolean canSupply) {
        this.canSupply = canSupply;
    }

    public boolean isOfOption(AvailableOption option, String optionValue) {
        HashMap<String, String> options = getOptions();
        Iterator it = options.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            if(pair.getKey().equals(option.getType()) && pair.getValue().equals(optionValue)) {
                return true;
            }
        }

        return false;
    }

    public boolean isOfOptions(HashMap<String, String> selectedOptions) {
        HashMap<String, String> options = getOptions();


        Iterator it = selectedOptions.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();

            if(!options.containsKey(pair.getKey()) || !options.get(pair.getKey()).equals(pair.getValue())) {
                return false;
            }
        }

        return true;
    }

    public String getValueOfOption(AvailableOption option) {
        HashMap<String, String> options = getOptions();
        Iterator it = options.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> pair = (Map.Entry)it.next();
            if(pair.getKey().equals(option.getType())) {
                return pair.getValue();
            }
        }

        return "";
    }
}

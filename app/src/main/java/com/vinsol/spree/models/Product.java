package com.vinsol.spree.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vinsol.spree.utils.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by vaibhav on 9/29/15.
 */
public class Product implements Serializable {
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

    @SerializedName("reviews_count")
    @Expose
    private String ratingsCount;

    @SerializedName("avg_rating")
    @Expose
    private String averageRating;

    @SerializedName("reviews_with_content_count")
    @Expose
    private Integer reviewsCount;

    @SerializedName("available_on")
    @Expose
    private String availableOn;

    @SerializedName("slug")
    @Expose
    private String slug;

    @SerializedName("meta_description")
    @Expose
    private String metaDescription;

    @SerializedName("meta_keywords")
    @Expose
    private String metaKeywords;

    @SerializedName("shipping_category_id")
    @Expose
    private Integer shippingCategoryId;

    @SerializedName("taxon_ids")
    @Expose
    private List<Integer> taxonIds = new ArrayList<Integer>();

    @SerializedName("total_on_hand")
    @Expose
    private Integer totalOnHand;

    @SerializedName("has_variants")
    @Expose
    private Boolean hasVariants;

    @SerializedName("images")
    @Expose
    private ArrayList<Image> images = new ArrayList<>();

    @SerializedName("variants_including_master")
    @Expose
    private ArrayList<Variant> variants = new ArrayList<Variant>();

    @SerializedName("product_properties")
    @Expose
    private List<ProductProperty> productProperties = new ArrayList<ProductProperty>();

    @SerializedName("available_options")
    @Expose
    private ArrayList<AvailableOption> availableOptions = new ArrayList<AvailableOption>();


    public Product(){}

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

    public String getAvailableOn() {
        return availableOn;
    }

    public void setAvailableOn(String availableOn) {
        this.availableOn = availableOn;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getMetaDescription() {
        return metaDescription;
    }

    public void setMetaDescription(String metaDescription) {
        this.metaDescription = metaDescription;
    }

    public String getMetaKeywords() {
        return metaKeywords;
    }

    public void setMetaKeywords(String metaKeywords) {
        this.metaKeywords = metaKeywords;
    }

    public Integer getShippingCategoryId() {
        return shippingCategoryId;
    }

    public void setShippingCategoryId(Integer shippingCategoryId) {
        this.shippingCategoryId = shippingCategoryId;
    }

    public List<Integer> getTaxonIds() {
        return taxonIds;
    }

    public void setTaxonIds(List<Integer> taxonIds) {
        this.taxonIds = taxonIds;
    }

    public Integer getTotalOnHand() {
        return totalOnHand;
    }

    public void setTotalOnHand(Integer totalOnHand) {
        this.totalOnHand = totalOnHand;
    }

    public Boolean getHasVariants() {
        return hasVariants;
    }

    public void setHasVariants(Boolean hasVariants) {
        this.hasVariants = hasVariants;
    }

    public ArrayList<Image> getImages() {
        return images;
    }

    public void setImages(ArrayList<Image> images) {
        this.images = images;
    }

    public int getRatingsCount() {
        int ratingsCountInt = 0;
        try {
            ratingsCountInt = Integer.parseInt(ratingsCount);
        } catch (Exception e) {
            ratingsCountInt = 0;
            Log.d("Error in parsing rating count");
        }

        return ratingsCountInt;
    }

    public void setRatingsCount(String ratingsCount) {
        this.ratingsCount = ratingsCount;
    }

    public float getAverageRating() {
        if(averageRating == null) {
            return 0;
        }

        try {
            return Float.parseFloat(averageRating);
        } catch (NumberFormatException nfe) {
            return 0;
        }
    }

    public void setAverageRating(String averageRating) {
        this.averageRating = averageRating;
    }

    public Integer getReviewsCount() {
        return reviewsCount;
    }

    public void setReviewsCount(Integer reviewsCount) {
        this.reviewsCount = reviewsCount;
    }

    public ArrayList<AvailableOption> getAvailableOptions() {
        return availableOptions != null ? availableOptions : new ArrayList<AvailableOption>();
    }

    public void setAvailableOptions(ArrayList<AvailableOption> availableOptions) {
        this.availableOptions = availableOptions;
    }


    public List<ProductProperty> getProductProperties() {
        return productProperties;
    }

    public HashMap<String, ArrayList<ProductProperty>> getGroupedProductProperties() {
        HashMap<String, ArrayList<ProductProperty>> groupedProductProperties = new HashMap<>();
        if(productProperties != null && productProperties.size() > 0) {
            for(ProductProperty productProperty : productProperties) {
                if(groupedProductProperties.containsKey(productProperty.getTypeName())) {
                    groupedProductProperties.get(productProperty.getTypeName()).add(productProperty);
                } else {
                    groupedProductProperties.put(productProperty.getTypeName(), new ArrayList<ProductProperty>());
                    groupedProductProperties.get(productProperty.getTypeName()).add(productProperty);
                }
            }
        }
        return groupedProductProperties;
    }

    public void setProductProperties(List<ProductProperty> productProperties) {
        this.productProperties = productProperties;
    }

    public ArrayList<Variant> getVariants() {
        return variants == null ? new ArrayList<Variant>() : variants;
    }

    public void setVariants(ArrayList<Variant> variants) {
        this.variants = variants;
    }

    public AvailableOption getAvailableOptionsAccordingToSelectedOption(AvailableOption selectedOption, String selectedOptionValue, AvailableOption optionToBeFiltered) {
        ArrayList<Variant> variants = getVariantsWithOptionValue(selectedOption, selectedOptionValue);

        AvailableOption availableOption = new AvailableOption();
        availableOption.setType(optionToBeFiltered.getType());

        ArrayList<String> options = new ArrayList<>();
        for(Variant variant : variants) {
            boolean isVariantOfSelectedValue = variant.isOfOption(selectedOption, selectedOptionValue);
            if(isVariantOfSelectedValue) {
                String valueOfOptionToBeFiltered = variant.getValueOfOption(optionToBeFiltered);
                if(options.contains(valueOfOptionToBeFiltered)) {
                    // do nothing
                } else {
                    options.add(valueOfOptionToBeFiltered);
                }
            }
        }

        availableOption.setValues(options);

        return availableOption;
    }

    public Variant getVariantsWithSelectedOptions(HashMap<String, String> selectedOptions) {
        for(Variant variant : variants) {
            if(variant.isOfOptions(selectedOptions)) {
                 return variant;
            }
        }

        return null;
    }

    private ArrayList<Variant> getVariantsWithOptionValue(AvailableOption option, String optionValue) {
        ArrayList<Variant> variants = getVariants();

        for(Variant variant : variants) {
            variant.getOptionsText();
        }

        return variants;
    }
}

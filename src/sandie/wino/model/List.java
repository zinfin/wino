
package sandie.wino.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "Appellation",
    "Community",
    "Description",
    "GeoLocation",
    "Id",
    "Labels",
    "Name",
    "PriceMax",
    "PriceMin",
    "PriceRetail",
    "ProductAttributes",
    "Ratings",
    "Retail",
    "Type",
    "Url",
    "Varietal",
    "Vineyard",
    "Vintage",
    "Vintages"
})
public class List {

    /**
     * 
     */
    @JsonProperty("Appellation")
    private Appellation appellation;
    /**
     * 
     */
    @JsonProperty("Community")
    private Community community;
    /**
     * 
     */
    @JsonProperty("Description")
    private String description;
    /**
     * 
     */
    @JsonProperty("GeoLocation")
    private GeoLocation geoLocation;
    /**
     * 
     */
    @JsonProperty("Id")
    private Double id;
    /**
     * 
     */
    @JsonProperty("Labels")
    private java.util.List<Label> labels = new ArrayList<Label>();
    /**
     * 
     */
    @JsonProperty("Name")
    private String name;
    /**
     * 
     */
    @JsonProperty("PriceMax")
    private Double priceMax;
    /**
     * 
     */
    @JsonProperty("PriceMin")
    private Double priceMin;
    /**
     * 
     */
    @JsonProperty("PriceRetail")
    private Double priceRetail;
    /**
     * 
     */
    @JsonProperty("ProductAttributes")
    private java.util.List<ProductAttribute> productAttributes = new ArrayList<ProductAttribute>();
    /**
     * 
     */
    @JsonProperty("Ratings")
    private Ratings ratings;
    /**
     * 
     */
    @JsonProperty("Retail")
    private Object retail;
    /**
     * 
     */
    @JsonProperty("Type")
    private String type;
    /**
     * 
     */
    @JsonProperty("Url")
    private String url;
    /**
     * 
     */
    @JsonProperty("Varietal")
    private Varietal varietal;
    /**
     * 
     */
    @JsonProperty("Vineyard")
    private Vineyard vineyard;
    /**
     * 
     */
    @JsonProperty("Vintage")
    private String vintage;
    /**
     * 
     */
    @JsonProperty("Vintages")
    private Vintages vintages;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     */
    @JsonProperty("Appellation")
    public Appellation getAppellation() {
        return appellation;
    }

    /**
     * 
     */
    @JsonProperty("Appellation")
    public void setAppellation(Appellation appellation) {
        this.appellation = appellation;
    }

    /**
     * 
     */
    @JsonProperty("Community")
    public Community getCommunity() {
        return community;
    }

    /**
     * 
     */
    @JsonProperty("Community")
    public void setCommunity(Community community) {
        this.community = community;
    }

    /**
     * 
     */
    @JsonProperty("Description")
    public String getDescription() {
        return description;
    }

    /**
     * 
     */
    @JsonProperty("Description")
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 
     */
    @JsonProperty("GeoLocation")
    public GeoLocation getGeoLocation() {
        return geoLocation;
    }

    /**
     * 
     */
    @JsonProperty("GeoLocation")
    public void setGeoLocation(GeoLocation geoLocation) {
        this.geoLocation = geoLocation;
    }

    /**
     * 
     */
    @JsonProperty("Id")
    public Double getId() {
        return id;
    }

    /**
     * 
     */
    @JsonProperty("Id")
    public void setId(Double id) {
        this.id = id;
    }

    /**
     * 
     */
    @JsonProperty("Labels")
    public java.util.List<Label> getLabels() {
        return labels;
    }

    /**
     * 
     */
    @JsonProperty("Labels")
    public void setLabels(java.util.List<Label> labels) {
        this.labels = labels;
    }

    /**
     * 
     */
    @JsonProperty("Name")
    public String getName() {
        return name;
    }

    /**
     * 
     */
    @JsonProperty("Name")
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     */
    @JsonProperty("PriceMax")
    public Double getPriceMax() {
        return priceMax;
    }

    /**
     * 
     */
    @JsonProperty("PriceMax")
    public void setPriceMax(Double priceMax) {
        this.priceMax = priceMax;
    }

    /**
     * 
     */
    @JsonProperty("PriceMin")
    public Double getPriceMin() {
        return priceMin;
    }

    /**
     * 
     */
    @JsonProperty("PriceMin")
    public void setPriceMin(Double priceMin) {
        this.priceMin = priceMin;
    }

    /**
     * 
     */
    @JsonProperty("PriceRetail")
    public Double getPriceRetail() {
        return priceRetail;
    }

    /**
     * 
     */
    @JsonProperty("PriceRetail")
    public void setPriceRetail(Double priceRetail) {
        this.priceRetail = priceRetail;
    }

    /**
     * 
     */
    @JsonProperty("ProductAttributes")
    public java.util.List<ProductAttribute> getProductAttributes() {
        return productAttributes;
    }

    /**
     * 
     */
    @JsonProperty("ProductAttributes")
    public void setProductAttributes(java.util.List<ProductAttribute> productAttributes) {
        this.productAttributes = productAttributes;
    }

    /**
     * 
     */
    @JsonProperty("Ratings")
    public Ratings getRatings() {
        return ratings;
    }

    /**
     * 
     */
    @JsonProperty("Ratings")
    public void setRatings(Ratings ratings) {
        this.ratings = ratings;
    }

    /**
     * 
     */
    @JsonProperty("Retail")
    public Object getRetail() {
        return retail;
    }

    /**
     * 
     */
    @JsonProperty("Retail")
    public void setRetail(Object retail) {
        this.retail = retail;
    }

    /**
     * 
     */
    @JsonProperty("Type")
    public String getType() {
        return type;
    }

    /**
     * 
     */
    @JsonProperty("Type")
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 
     */
    @JsonProperty("Url")
    public String getUrl() {
        return url;
    }

    /**
     * 
     */
    @JsonProperty("Url")
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 
     */
    @JsonProperty("Varietal")
    public Varietal getVarietal() {
        return varietal;
    }

    /**
     * 
     */
    @JsonProperty("Varietal")
    public void setVarietal(Varietal varietal) {
        this.varietal = varietal;
    }

    /**
     * 
     */
    @JsonProperty("Vineyard")
    public Vineyard getVineyard() {
        return vineyard;
    }

    /**
     * 
     */
    @JsonProperty("Vineyard")
    public void setVineyard(Vineyard vineyard) {
        this.vineyard = vineyard;
    }

    /**
     * 
     */
    @JsonProperty("Vintage")
    public String getVintage() {
        return vintage;
    }

    /**
     * 
     */
    @JsonProperty("Vintage")
    public void setVintage(String vintage) {
        this.vintage = vintage;
    }

    /**
     * 
     */
    @JsonProperty("Vintages")
    public Vintages getVintages() {
        return vintages;
    }

    /**
     * 
     */
    @JsonProperty("Vintages")
    public void setVintages(Vintages vintages) {
        this.vintages = vintages;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object other) {
        return EqualsBuilder.reflectionEquals(this, other);
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}

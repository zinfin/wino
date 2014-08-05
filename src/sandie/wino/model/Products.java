
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
    "List",
    "Offset",
    "Total",
    "Url"
})
public class Products {

    /**
     * 
     */
    @JsonProperty("List")
    private java.util.List<List> list = new ArrayList<List>();
    /**
     * 
     */
    @JsonProperty("Offset")
    private Double offset;
    /**
     * 
     */
    @JsonProperty("Total")
    private Double total;
    /**
     * 
     */
    @JsonProperty("Url")
    private String url;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     */
    @JsonProperty("List")
    public java.util.List<List> getList() {
        return list;
    }

    /**
     * 
     */
    @JsonProperty("List")
    public void setList(java.util.List<List> list) {
        this.list = list;
    }

    /**
     * 
     */
    @JsonProperty("Offset")
    public Double getOffset() {
        return offset;
    }

    /**
     * 
     */
    @JsonProperty("Offset")
    public void setOffset(Double offset) {
        this.offset = offset;
    }

    /**
     * 
     */
    @JsonProperty("Total")
    public Double getTotal() {
        return total;
    }

    /**
     * 
     */
    @JsonProperty("Total")
    public void setTotal(Double total) {
        this.total = total;
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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
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

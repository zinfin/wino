
package sandie.wino.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    "Description",
    "Id",
    "Name",
    "Refinements"
})
public class Category {

    /**
     * 
     */
    @JsonProperty("Description")
    private String description;
    /**
     * 
     */
    @JsonProperty("Id")
    private Double id;
    /**
     * 
     */
    @JsonProperty("Name")
    private String name;
    /**
     * 
     */
    @JsonProperty("Refinements")
    private List<Refinement> refinements = new ArrayList<Refinement>();
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

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
    @JsonProperty("Refinements")
    public List<Refinement> getRefinements() {
        return refinements;
    }

    /**
     * 
     */
    @JsonProperty("Refinements")
    public void setRefinements(List<Refinement> refinements) {
        this.refinements = refinements;
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

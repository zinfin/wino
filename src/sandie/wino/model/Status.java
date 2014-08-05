
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
    "Messages",
    "ReturnCode"
})
public class Status {

    /**
     * 
     */
    @JsonProperty("Messages")
    private List<Object> messages = new ArrayList<Object>();
    /**
     * 
     */
    @JsonProperty("ReturnCode")
    private Double returnCode;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     */
    @JsonProperty("Messages")
    public List<Object> getMessages() {
        return messages;
    }

    /**
     * 
     */
    @JsonProperty("Messages")
    public void setMessages(List<Object> messages) {
        this.messages = messages;
    }

    /**
     * 
     */
    @JsonProperty("ReturnCode")
    public Double getReturnCode() {
        return returnCode;
    }

    /**
     * 
     */
    @JsonProperty("ReturnCode")
    public void setReturnCode(Double returnCode) {
        this.returnCode = returnCode;
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

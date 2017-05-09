package co.firetools.copperink.models;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class Account {
    private String id;
    private String name;
    private String imageUrl;
    private Type   type;


    /**
     * Account Constructor
     */
    @JsonCreator
    public Account(
            @JsonProperty("id")         String id,
            @JsonProperty("name")       String name,
            @JsonProperty("image_url")  String imageUrl,
            @JsonProperty("type")       Type   type) {
        this.id       = id;
        this.name     = name;
        this.imageUrl = imageUrl;
        this.type     = type;
    }



    /**
     * Account Type Enumerations
     */
    private enum Type {
        @JsonProperty("facebook") Facebook
    }



    /**
     * Serializes the Account object into a
     * JSON String using the Jackson library
     */
    public static String serialize(Account account) throws JsonProcessingException {
        // Serialize using private/public fields only,
        // ignore everything else (Setters, Getters, etc.)
        return new ObjectMapper()
                .setVisibility(PropertyAccessor.ALL,   JsonAutoDetect.Visibility.NONE)
                .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
                .writeValueAsString(account);
    }


    /**
     * Deserializes JSON string back into an Account
     * object using the Jackson library
     */
    public static Account deserialize(String json) throws IOException {
        return new ObjectMapper().readValue(json, Account.class);
    }
}

package co.firetools.copperink.models;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import co.firetools.copperink.behaviors.Model;
import co.firetools.copperink.db.DBContract;

public class Account implements Model {
    private String id;
    private String name;
    private String type;
    private String imageUrl;


    /**
     * Account Constructor
     */
    @JsonCreator
    public Account(
            @JsonProperty("id")    String id,
            @JsonProperty("name")  String name,
            @JsonProperty("type")  String type,
            @JsonProperty("image") String imageUrl) {
        this.id       = id;
        this.name     = name;
        this.type     = type;
        this.imageUrl = imageUrl;
    }


    /**
     * Getters
     */
    public String getID()       { return id;       }
    public String getName()     { return name;     }
    public String getType()     { return type;     }
    public String getImageUrl() { return imageUrl; }



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


    /**
     * Return appropriate contract
     */
    @Override
    public Contract getContract() {
        return new DBContract.AccountTable();
    }
}

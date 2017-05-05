package co.firetools.copperink.models;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class User {
    private String id;
    private String name;
    private String email;
    private String token;


    /**
     * User Constructor
     */
    @JsonCreator
    public User(
        @JsonProperty("id")    String id,
        @JsonProperty("name")  String name,
        @JsonProperty("email") String email,
        @JsonProperty("token") String token) {
            this.id    = id;
            this.name  = name;
            this.email = email;
            this.token = token;
    }


    /**
     * Serializes the Account object into a
     * JSON String using the Jackson library
     */
    public static String serialize(User user) throws JsonProcessingException {
        // Serialize using private/public fields only,
        // ignore everything else (Setters, Getters, etc.)
        return new ObjectMapper()
                .setVisibility(PropertyAccessor.ALL,   JsonAutoDetect.Visibility.NONE)
                .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
                .writeValueAsString(user);
    }


    /**
     * Deserializes JSON string back into an Account
     * object using the Jackson library
     */
    public static User deserialize(String json) throws IOException {
        return new ObjectMapper().readValue(json, User.class);
    }
}

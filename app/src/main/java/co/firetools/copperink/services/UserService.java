package co.firetools.copperink.services;

import org.json.JSONObject;

import java.io.IOException;

import co.firetools.copperink.db.DB;
import co.firetools.copperink.models.User;

import static co.firetools.copperink.services.GlobalService.getStore;

public class UserService {
    private static final String KEY_USER = "CopperUser";
    private static User user;


    /**
     * Checks if a user exists
     */
    public static boolean userExists() {
        return user != null;
    }



    /**
     * Get the Current User
     */
    public static User getUser() {
        return user;
    }


    /**
     * Loads JSON data from SharedPreferences using TinyDB
     * and then deserializes them to Account objects.
     * Generates their Twitter objects as well
     */
    public static void loadUser() {
        String userString = getStore().getString(KEY_USER, null);

        user = null;

        if (userString != null)
            try {
                // Deserialize and load account
                user = User.deserialize(userString);
            } catch (IOException e) {
                e.printStackTrace();
            }
    }


    /**
     * Save User Data + Token to SharedPreferences
     */
    public static void saveUser(JSONObject userObject) {
        try {
            String userString = userObject.toString();
            user = User.deserialize(userString);
            GlobalService.getStore().putString(KEY_USER, userString);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }


    public static void destroyUser() {
        DB.reset();
        GlobalService.getStore().remove(KEY_USER);
    }


}

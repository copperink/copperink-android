package co.firetools.copperink.services;

import android.content.Context;

import java.io.IOException;

import co.firetools.copperink.models.User;
import co.firetools.copperink.utils.TinyDB;

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
    public static void loadUser(Context context) {
        TinyDB tinydb = new TinyDB(context);
        String userString = tinydb.getString(KEY_USER, null);

        user = null;

        if (userString != null)
            try {
                // Deserialize and load account
                user = User.deserialize(userString);
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
}

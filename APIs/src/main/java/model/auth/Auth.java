package model.auth;


import org.json.JSONObject;

public interface Auth {


    /**
     * <b>Preconditions:</b><br>
     *     correct api key.
     * <br>
     * <b>Postconditions:</b><br>
     *     able to access other functionalities.
     * <br>
     *
     * @param key The authentication key to verify this operation with. May not be null.
     * @return return if login successfully.
     * @throws SecurityException If the authentication token is null or fails verification.
     * @throws IllegalArgumentException If any of the other preconditions are violated.
     */
    boolean login(String key)throws IllegalArgumentException, SecurityException;


    /**
     * <b>Preconditions:</b><br>
     *     api key is not null.
     * <br>
     * <b>Postconditions:</b><br>
     *     return the api key.
     * <br>
     *
     * @return return api key.
     */
    String getApiKey();

    /**
     * Check if it is dummy or not.
     * <b>Preconditions:</b><br>
     *     none.
     * <br>
     * <b>Postconditions:</b><br>
     *     return the version to be displayed.
     * <br>
     * @return version number.
     */
    int checkVersion();

}


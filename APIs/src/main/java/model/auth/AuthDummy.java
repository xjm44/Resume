package model.auth;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class AuthDummy implements Auth {
    private int version;
    private String apiKey;

    @Override
    public boolean login(String apiKey) throws IllegalArgumentException, SecurityException {
//        String apiKey = "8b030e14-dd33-45c2-9246-8345115f6829";
        this.apiKey = apiKey;
        return true;
    }

    @Override
    public String getApiKey() {
        return apiKey;
    }


    @Override
    public int checkVersion() {
        return version;
    }
}

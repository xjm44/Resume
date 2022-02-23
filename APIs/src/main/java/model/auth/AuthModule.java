package model.auth;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class AuthModule implements Auth {
    private int version;
    private String apiKey;

    @Override
    public boolean login(String apiKey) throws IllegalArgumentException, SecurityException {
//        String apiKey = "8b030e14-dd33-45c2-9246-8345115f6829";
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://pro-api.coinmarketcap.com/v1/key/info"))
//                    .setHeader("ACCEPT","application/json")
                    .header("X-CMC_PRO_API_KEY", apiKey)
                    .build();

            Integer code = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::statusCode)
                    .join();
//        System.out.println(code);

            this.apiKey = apiKey;
            return code == 200;
        }catch (Exception e){
            return false;
        }
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

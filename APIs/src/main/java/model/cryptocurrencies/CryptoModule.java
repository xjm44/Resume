package model.cryptocurrencies;

import model.auth.Auth;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class CryptoModule implements Crypto {
    private Auth auth;

    public CryptoModule(Auth auth){
        this.auth=auth;
    }

    @Override
    public JSONObject getInfoID(String id) throws IllegalArgumentException{
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://pro-api.coinmarketcap.com/v1/cryptocurrency/info?id=" + id))
//                    .setHeader("ACCEPT","application/json")
                    .header("X-CMC_PRO_API_KEY", auth.getApiKey())
                    .build();

            Integer code = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::statusCode)
                    .join();

            if (code == 200) {

                String response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                        .thenApply(HttpResponse::body)
                        .join();

                return new JSONObject(response);

            }
        }catch (Exception e){
            return null;
        }
        return null;

    }

    @Override
    public JSONObject getInfoSlug(String slug) throws IllegalArgumentException{
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://pro-api.coinmarketcap.com/v1/cryptocurrency/info?slug=" + slug))
//                    .setHeader("ACCEPT","application/json")
                    .header("X-CMC_PRO_API_KEY", auth.getApiKey())
                    .build();

            Integer code = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::statusCode)
                    .join();

            if (code == 200) {

                String response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                        .thenApply(HttpResponse::body)
                        .join();


                return new JSONObject(response);
            }
        }catch(Exception e){
            return null;
        }
        return null;
    }

    @Override
    public JSONObject getInfoSymbol(String symbol) throws IllegalArgumentException{
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://pro-api.coinmarketcap.com/v1/cryptocurrency/info?symbol=" + symbol))
//                    .setHeader("ACCEPT","application/json")
                    .header("X-CMC_PRO_API_KEY", auth.getApiKey())
                    .build();

            Integer code = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::statusCode)
                    .join();

            if (code == 200) {

                String response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                        .thenApply(HttpResponse::body)
                        .join();

                return new JSONObject(response);
            }
        }catch (Exception e){
            return null;
        }
        return null;
    }

    @Override
    public JSONObject getAllInfo(String start, String limit, String sort) throws IllegalArgumentException {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://pro-api.coinmarketcap.com/v1/cryptocurrency/map?limit=" + limit + "&start=" + start + "&sort=" + sort))
//                    .setHeader("ACCEPT","application/json")
                    .header("X-CMC_PRO_API_KEY", auth.getApiKey())
                    .build();
            Integer code = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::statusCode)
                    .join();

            if (code == 200) {
                String response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                        .thenApply(HttpResponse::body)
                        .join();

                return new JSONObject(response);
            }
        }catch(Exception e){
            return null;
        }
        return null;
    }
}

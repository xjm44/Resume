package model.conversion;

import model.MerchantFee;
import model.MerchantFeeImpl;
import model.auth.Auth;
import model.cryptocurrencies.Crypto;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ConverseModule implements Converse {

    private Auth auth;
    private MerchantFee mf = new MerchantFeeImpl();

    public ConverseModule(Auth auth){
        this.auth=auth;
    }


    @Override
    public String converseSymbol(String cur1, String cur2, String amount,String fee) throws IllegalArgumentException {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://pro-api.coinmarketcap.com/v1/tools/price-conversion?amount=" + amount + "&symbol=" + cur1 + "&convert=" + cur2))
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
                JSONObject obj = new JSONObject(response);
                String str = "";
                if (obj.getJSONObject("data") == null) {
                    return "invalid input";
                } else {

                    JSONObject data = obj.getJSONObject("data");

                    for (Object key : data.keySet()) {

                        if (key.equals("quote")) {
                            str += key + ":\n";
                            JSONObject temp = (JSONObject) data.get((String) key);
                            for (Object qk : temp.keySet()) {
                                JSONObject curr = (JSONObject) temp.get((String) qk);
                                str += "\t" + qk + ":\n";
                                for (Object ck : curr.keySet()) {
                                    // for extension *********************
                                    if(ck.equals("price")){
                                        String v = String.valueOf(curr.get((String) ck));
                                        String newFee = mf.currency(fee,v);
                                        if(newFee==null){
                                            newFee = "no merchant fee entered during login!";
                                        }
                                        str += "\t\t" + ck + ": " + newFee + "\n";
                                    }else{
                                        str += "\t\t" + ck + ": " + curr.get((String) ck) + "\n";
                                    }
                                    // ***********************************
                                }
                            }
                        } else {
                            Object value = data.get((String) key);
                            str += key + ": " + value + "\n";
                        }
                    }
                }
                return str;
            }
        }catch(Exception e){
            return "invalid input";
        }
        return "invalid input";
    }

    @Override
    public String converseID(String cur1, String cur2, String amount, String fee) throws IllegalArgumentException {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://pro-api.coinmarketcap.com/v1/tools/price-conversion?amount=" + amount + "&id=" + cur1 + "&convert_id=" + cur2))
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
                JSONObject obj = new JSONObject(response);
                String str = "";
                if (obj.getJSONObject("data") == null) {
                    return "invalid input";
                } else {

                    JSONObject data = obj.getJSONObject("data");

                    for (Object key : data.keySet()) {

                        if (key.equals("quote")) {
                            str += key + ":\n";
                            JSONObject temp = (JSONObject) data.get((String) key);
                            for (Object qk : temp.keySet()) {
                                JSONObject curr = (JSONObject) temp.get((String) qk);
                                str += "\t" + qk + ":\n";
                                for (Object ck : curr.keySet()) {
                                    // for extension *********************
                                    if(ck.equals("price")){
                                        String v = String.valueOf(curr.get((String) ck));
                                        String newFee = mf.currency(fee,v);
                                        if(newFee==null){
                                            newFee = "no merchant fee entered during login!";
                                        }
                                        str += "\t\t" + ck + ": " + newFee + "\n";

                                    }else{
                                        str += "\t\t" + ck + ": " + curr.get((String) ck) + "\n";
                                    }
                                    // ***********************************
                                }
                            }
                        } else {
                            Object value = data.get((String) key);
                            str += key + ": " + value + "\n";
                        }
                    }
                }
                return str;
            }
        }catch (Exception e){
            return "invalid input";
        }
        return "invalid input";
    }
}

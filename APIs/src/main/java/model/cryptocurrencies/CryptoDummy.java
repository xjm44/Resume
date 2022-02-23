package model.cryptocurrencies;

import org.json.JSONObject;

public class CryptoDummy implements Crypto {
// I did not edit these methods because the output is according to Detail class.

    @Override
    public JSONObject getInfoID(String id) throws IllegalArgumentException {
        return null;
    }

    @Override
    public JSONObject getInfoSlug(String slug) throws IllegalArgumentException {
        return null;
    }

    @Override
    public JSONObject getInfoSymbol(String symbol) throws IllegalArgumentException {
        return null;
    }

    @Override
    public JSONObject getAllInfo(String start, String limit, String sort) throws IllegalArgumentException {
        return null;
    }
}

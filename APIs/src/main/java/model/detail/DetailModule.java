package model.detail;

import model.auth.Auth;
import model.cryptocurrencies.Crypto;
import model.cryptocurrencies.CryptoModule;
import org.json.JSONArray;
import org.json.JSONObject;

public class DetailModule implements Detail {
    private Crypto crypto;

    public DetailModule(Auth auth) {
        this.crypto = new CryptoModule(auth);
    }

    @Override
    public String getInfoID(String id) throws IllegalArgumentException {
        try {

            if (crypto.getInfoID(id) == null) {
                return "invalid input";
            }
            JSONObject obj = crypto.getInfoID(id).getJSONObject("data");
            String str = "";

            for (Object key : obj.keySet()) {

                JSONObject k = obj.getJSONObject((String) key);

                for (Object oo : k.keySet()) {
                    if (oo.equals("tag-groups")) {
                        // I did not include this because I feel the output of this is useless to the users.
                        str+="tag-groups: not relevant/useful to readers\n";
                    } else if (oo.equals("tag-names") && !k.get((String) oo).toString().equals("null")) {
//                    System.out.println(k.get((String) oo).toString());
                        JSONArray tn = k.getJSONArray((String) oo);
                        str += oo + ":\n";
                        for (int j = 0; j < tn.length(); j++) {
                            str += "\t" + tn.get(j) + "\n";
                        }
                    } else if (oo.equals("tags") && !k.get((String) oo).toString().equals("null")) {
                        JSONArray tn = k.getJSONArray((String) oo);
                        str += oo + ":\n";
                        for (int j = 0; j < tn.length(); j++) {
                            str += "\t" + tn.get(j) + "\n";
                        }
                    } else if (oo.equals("urls")) {
                        JSONObject u = k.getJSONObject((String) oo);
                        str += oo + ":\n";
                        for (Object uo : u.keySet()) {
                            Object uvalue = u.get((String) uo);
                            str += "\t" + uo + ": " + uvalue + "\n";
                        }
                    } else if (oo.equals("platform") && !k.get((String) oo).toString().equals("null")) {
                        JSONObject temp = (JSONObject) k.get((String) oo);
                        str += "platform:\n";
                        for (Object p : temp.keySet()) {
                            Object pvalue = temp.get((String) p);
                            str += "\t" + p + ": " + pvalue + "\n";
                        }
                    } else {
                        Object value = k.get((String) oo);
                        str += oo + ": " + value + "\n";
                    }
                }
            }
            return str;
        }catch(Exception e){
            return "invalid input";
        }

    }

    @Override
    public String getInfoSlug(String slug) throws IllegalArgumentException {
        try {
            if (crypto.getInfoSlug(slug) == null) {
                return "invalid input";
            }
            JSONObject obj = crypto.getInfoSlug(slug).getJSONObject("data");
            String str = "";

            for (Object key : obj.keySet()) {

                JSONObject k = obj.getJSONObject((String) key);

                for (Object oo : k.keySet()) {
                    if (oo.equals("tag-groups")) {
                        // I did not include this because I feel the output of this is useless to the users.
                        str+="tag-groups: not relevant/useful to readers\n";
                    } else if (oo.equals("tag-names") && !k.get((String) oo).toString().equals("null")) {
//                    System.out.println(k.get((String) oo).toString());
                        JSONArray tn = k.getJSONArray((String) oo);
                        str += oo + ":\n";
                        for (int j = 0; j < tn.length(); j++) {
                            str += "\t" + tn.get(j) + "\n";
                        }
                    } else if (oo.equals("tags") && !k.get((String) oo).toString().equals("null")) {
                        JSONArray tn = k.getJSONArray((String) oo);
                        str += oo + ":\n";
                        for (int j = 0; j < tn.length(); j++) {
                            str += "\t" + tn.get(j) + "\n";
                        }
                    } else if (oo.equals("urls")) {
                        JSONObject u = k.getJSONObject((String) oo);
                        str += oo + ":\n";
                        for (Object uo : u.keySet()) {
                            Object uvalue = u.get((String) uo);
                            str += "\t" + uo + ": " + uvalue + "\n";
                        }
                    } else if (oo.equals("platform") && !k.get((String) oo).toString().equals("null")) {
                        JSONObject temp = (JSONObject) k.get((String) oo);
                        str += "platform:\n";
                        for (Object p : temp.keySet()) {
                            Object pvalue = temp.get((String) p);
                            str += "\t" + p + ": " + pvalue + "\n";
                        }
                    } else {
                        Object value = k.get((String) oo);
                        str += oo + ": " + value + "\n";
                    }
                }
            }
            return str;
        }catch (Exception e){
            return "invalid input";
        }

    }

    @Override
    public String getInfoSymbol(String symbol) throws IllegalArgumentException {
        try {
            if (crypto.getInfoSymbol(symbol) == null) {
                return "invalid input";
            }
            JSONObject obj = crypto.getInfoSymbol(symbol).getJSONObject("data");
            String str = "";

            for (Object key : obj.keySet()) {

                JSONObject k = obj.getJSONObject((String) key);

                for (Object oo : k.keySet()) {
                    if (oo.equals("tag-groups")) {
                        // I did not include this because I feel the output of this is useless to the users.
                        str+="tag-groups: not relevant/useful to readers\n";
                    } else if (oo.equals("tag-names") && !k.get((String) oo).toString().equals("null")) {
//                    System.out.println(k.get((String) oo).toString());
                        JSONArray tn = k.getJSONArray((String) oo);
                        str += oo + ":\n";
                        for (int j = 0; j < tn.length(); j++) {
                            str += "\t" + tn.get(j) + "\n";
                        }
                    } else if (oo.equals("tags") && !k.get((String) oo).toString().equals("null")) {
                        JSONArray tn = k.getJSONArray((String) oo);
                        str += oo + ":\n";
                        for (int j = 0; j < tn.length(); j++) {
                            str += "\t" + tn.get(j) + "\n";
                        }
                    } else if (oo.equals("urls")) {
                        JSONObject u = k.getJSONObject((String) oo);
                        str += oo + ":\n";
                        for (Object uo : u.keySet()) {
                            Object uvalue = u.get((String) uo);
                            str += "\t" + uo + ": " + uvalue + "\n";
                        }
                    } else if (oo.equals("platform") && !k.get((String) oo).toString().equals("null")) {
                        JSONObject temp = (JSONObject) k.get((String) oo);
                        str += "platform:\n";
                        for (Object p : temp.keySet()) {
                            Object pvalue = temp.get((String) p);
                            str += "\t" + p + ": " + pvalue + "\n";
                        }
                    } else {
                        Object value = k.get((String) oo);
                        str += oo + ": " + value + "\n";
                    }
                }
            }
            return str;
        }catch(Exception e){
            return "invalid input";
        }

    }

    @Override
    public String getAllInfo(String start, String limit, String sort) throws IllegalArgumentException {
        try {
            if (crypto.getAllInfo(start, limit, sort) == null) {
                return "invalid input";
            }

            String str = "";
            JSONArray array = crypto.getAllInfo(start, limit, sort).getJSONArray("data");

            for (int i = 0; i < array.length(); i++) {

                JSONObject obj = (JSONObject) array.get(i);
                for (Object key : obj.keySet()) {

                    if (!key.equals("platform")) {
                        Object value = obj.get((String) key);
                        str += "" + key + ": " + value + "\n";

                    } else {
                        if (!obj.get((String) key).toString().equals("null")) {
                            JSONObject temp = (JSONObject) obj.get((String) key);
                            str += "platform:\n";
                            for (Object p : temp.keySet()) {
                                Object pvalue = temp.get((String) p);
                                str += "\t" + p + ": " + pvalue + "\n";
                            }
                        } else {
                            str += key + ": " + obj.get((String) key) + "\n";
                        }
                    }
                }
                str += "\n";

            }
            return str;
        }catch (Exception e){
            return "invalid input";
        }

    }
}


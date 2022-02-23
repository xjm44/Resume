package model.conversion;


public class ConverseDummy implements Converse {


    @Override
    public String converseSymbol(String cur1, String cur2, String amount, String fee) throws IllegalArgumentException {
        return "symbol: BTC\n" +
                "amount: 1\n" +
                "last_updated: 2021-05-26T01:52:02.000Z\n" +
                "quote:\n" +
                "\tUSD:\n" +
                "\t\tlast_updated: 2021-05-26T01:52:02.000Z\n" +
                "\t\tprice: 38716.56276045297\n" +
                "name: Bitcoin\n" +
                "id: 1";
    }

    @Override
    public String converseID(String cur1, String cur2, String amount, String fee) throws IllegalArgumentException {
        return "symbol: BTC\n" +
                "amount: 1\n" +
                "last_updated: 2021-05-26T01:53:02.000Z\n" +
                "quote:\n" +
                "\t2:\n" +
                "\t\tlast_updated: 2021-05-26T01:53:02.000Z\n" +
                "\t\tprice: 208.35492208130833\n" +
                "name: Bitcoin\n" +
                "id: 1";
    }
}

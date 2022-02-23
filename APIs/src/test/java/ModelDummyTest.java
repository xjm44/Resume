
import model.auth.Auth;
import model.auth.AuthDummy;
import model.auth.AuthModule;
import model.conversion.Converse;
import model.conversion.ConverseDummy;
import model.conversion.ConverseModule;
import model.cryptocurrencies.Crypto;
import model.cryptocurrencies.CryptoModule;
import model.detail.Detail;
import model.detail.DetailDummy;
import model.detail.DetailModule;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


public class ModelDummyTest {

    private Auth auth;
    private Converse con;
    //    private Crypto crypto;
    private Detail detail;

    @Before
    public void init(){

        auth = new AuthDummy();
        auth.login("hi");
        con = new ConverseDummy();
        detail = new DetailDummy();

    }

    @Test
    public void testDetail(){

        String temp = detail.getAllInfo("1","2","id");
        String temp1 = detail.getInfoID("1");
        String temp2 = detail.getInfoSlug("bitcoin");
        String temp3 = detail.getInfoSymbol("BTC");

        String check = "symbol: BTC\n" +
                "is_active: 1\n" +
                "last_historical_data: 2021-05-26T06:49:03.000Z\n" +
                "name: Bitcoin\n" +
                "rank: 1\n" +
                "id: 1\n" +
                "slug: bitcoin\n" +
                "platform: null\n" +
                "first_historical_data: 2013-04-28T18:47:21.000Z\n";
        String check1 ="symbol: VTA\n" +
                "twitter_username: \n" +
                "is_hidden: 0\n" +
                "description: Virtacoin (VTA) is a cryptocurrency . Users are able to generate VTA through the process of mining. Virtacoin has a current supply of 12,666,916,683.2 with 5,201,310,113.21 in circulation. The last known price of Virtacoin is 0.00000007 USD and is up 0.00 over the last 24 hours. More information can be found at https://www.virtacoin.world/.\n" +
                "tag-names:\n" +
                "\tMineable\n";

        assertTrue(temp.contains(check));
        assertTrue(temp1.contains(check1));
        assertTrue(temp2.contains(check1));
        assertTrue(temp3.contains(check1));

    }
    @Test
    public void testConverse(){

        String temp = con.converseID("1","2","1","1");
        String check = "symbol: BTC\n" +
                "amount: 1\n" +
                "last_updated: 2021-05-26T01:53:02.000Z\n" +
                "quote:\n";
        assertTrue(temp.contains(check));

        String temp1 = con.converseSymbol("BTC","BTC","1","1");
        String check1 = "symbol: BTC\n" +
                "amount: 1\n" +
                "last_updated: 2021-05-26T01:52:02.000Z\n" +
                "quote:\n";
        assertTrue(temp1.contains(check1));

    }


}


import model.auth.Auth;
import model.auth.AuthModule;
import model.conversion.Converse;
import model.conversion.ConverseModule;
import model.cryptocurrencies.Crypto;
import model.cryptocurrencies.CryptoModule;
import model.detail.Detail;
import model.detail.DetailModule;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


public class ModelTest {

    private Auth auth;
    private Converse con;
//    private Crypto crypto;
    private Detail detail;

    @Before
    public void init(){

        auth = new AuthModule();
        assertTrue(auth.login("8b030e14-dd33-45c2-9246-8345115f6829"));
        auth.login("8b030e14-dd33-45c2-9246-8345115f6829");
        con = new ConverseModule(auth);
        detail = new DetailModule(auth);

    }

    @Test
    public void testDetail(){

        String temp = detail.getAllInfo("1","2","id");
        String temp1 = detail.getInfoID("1");
        String temp2 = detail.getInfoSlug("bitcoin");
        String temp3 = detail.getInfoSymbol("BTC");

        String check = "symbol: BTC\n" +
                "is_active: 1";
        String check1 ="symbol: BTC\n" +
                "tag-groups: not relevant/useful to readers\n" +
                "twitter_username: \n" +
                "is_hidden: 0";
//        System.out.println(temp1);
        assertTrue(temp.contains(check));
        assertTrue(temp1.contains(check1));
        assertTrue(temp2.contains(check1));
        assertTrue(temp3.contains(check1));

    }
    @Test
    public void testConverse(){

        String temp = con.converseID("1","2","1","10");
        String check = "symbol: BTC\n" +
                "amount: 1";
        assertTrue(temp.contains(check));

        String temp1 = con.converseSymbol("BTC","BTC","1","10");
        String check1 = "symbol: BTC\n" +
                "amount: 1";
        assertTrue(temp1.contains(check1));

    }


}

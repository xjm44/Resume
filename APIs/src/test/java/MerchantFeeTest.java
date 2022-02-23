import model.MerchantFee;
import model.MerchantFeeImpl;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
public class MerchantFeeTest {

    private MerchantFee mf;

    @Before
    public void init(){
        mf = new MerchantFeeImpl();

    }

    @Test
    public void testCurrency(){

        assertNotNull(mf.currency("10","100"));
        assertNull(mf.currency("a","100"));
    }

    @Test
    public void testCheckRange(){
        assertFalse(mf.checkRange("12.5"));
        assertTrue(mf.checkRange("0"));
        assertTrue(mf.checkRange("99"));
        assertFalse(mf.checkRange("-1"));
        assertFalse(mf.checkRange("100"));
        assertFalse(mf.checkRange("200"));
    }
}

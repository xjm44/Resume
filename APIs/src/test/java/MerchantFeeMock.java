import model.MerchantFee;
import org.junit.Before;
import org.junit.Test;


import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
public class MerchantFeeMock {

    private MerchantFee mf;

    @Before
    public void init(){
        mf = mock(MerchantFee.class);
    }

    @Test
    public void currencyTest(){
        when(mf.currency("hi","hi")).thenReturn("a");
        assertEquals("a",mf.currency("hi","hi"));
        verify(mf,times(1)).currency("hi","hi");
    }

    @Test
    public void checkRangeTest(){
        when(mf.checkRange("a")).thenReturn(true);
        assertTrue(mf.checkRange("a"));
        verify(mf,times(1)).checkRange("a");
    }
}

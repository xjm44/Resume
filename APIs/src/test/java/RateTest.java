import model.Rate;
import model.RateImpl;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
public class RateTest {
    private Rate rate;

    @Before
    public void init(){
        rate = new RateImpl();
    }
    @Test
    public void testRate(){
        assertNotNull(rate.rate("12"));
        assertNull(rate.rate("a"));
    }
}

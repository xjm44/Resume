import model.Rate;
import org.junit.Before;
import org.junit.Test;


import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
public class RateMock {

    private Rate rate;

    @Before
    public void init(){
        rate = mock(Rate.class);
    }
    @Test
    public void testRate(){
        when(rate.rate("a")).thenReturn((float) 0);
        assertEquals(0.0,rate.rate("a"), 1e-15);

    }
}

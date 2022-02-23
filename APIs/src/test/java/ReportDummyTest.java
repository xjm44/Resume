
import org.junit.Test;
import report.Report;
import report.ReportDummy;


import static org.junit.Assert.*;
public class ReportDummyTest {

    private Report rp;

    @Test
    public void testSend(){
        rp = new ReportDummy();

        assertTrue(rp.send("a"));


    }
    @Test
    public void testDummyVersion(){
        rp = new ReportDummy();

        assertTrue(rp.checkDummy());


    }
}


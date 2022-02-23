import org.junit.Before;
import org.junit.Test;
import report.Report;
import report.ReportImpl;

import static org.junit.Assert.*;
public class ReportTest {

    private Report report;

    @Test
    public void testSend(){
        report = new ReportImpl();
        assertTrue(report.send("hi"));

    }
    @Test
    public void testDummyVersion(){
        report = new ReportImpl();
        assertFalse(report.checkDummy());

    }

}

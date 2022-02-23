import org.junit.Before;
import org.junit.Test;
import report.Report;


import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
public class ReportMock {

    private Report report;

    @Test
    public void testReport(){
        report = mock(Report.class);
        when(report.send("a")).thenReturn(false);
        assertFalse(report.send("a"));
    }
    @Test
    public void dummyVersion(){
        report = mock(Report.class);
        when(report.checkDummy()).thenReturn(false);
        assertFalse(report.checkDummy());
    }
}

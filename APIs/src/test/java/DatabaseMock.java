import database.Database;
import org.junit.Before;
import org.junit.Test;


import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
public class DatabaseMock {

    private Database db;

    @Before
    public void init(){
        db = mock(Database.class);
    }

    @Test
    public void testSet(){
        when(db.insertData("a","b")).thenReturn(true);
        assertTrue(db.insertData("a","b"));
        verify(db,times(1)).insertData("a","b");
    }
    @Test
    public void testGet(){
        when(db.getDetail("a")).thenReturn("hi");
        assertEquals("hi",db.getDetail("a"));
        verify(db,times(1)).getDetail("a");
    }
}

import model.auth.Auth;

import model.conversion.Converse;
import model.cryptocurrencies.Crypto;

import model.detail.Detail;
import org.junit.Before;
import org.junit.Test;


import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

public class ModelMock {
    private Auth auth;
    private Converse converse;
    private Detail detail;
    private Crypto crypto;


    @Before
    public void init(){


        auth = mock(Auth.class);

        crypto = mock(Crypto.class);

        converse = mock(Converse.class);
        detail = mock(Detail.class);

    }

    @Test
    public void testAuth(){
        when(auth.login("aaa")).thenReturn(true);
        assertTrue(auth.login("aaa"));

        when(auth.checkVersion()).thenReturn(5);
        assertEquals(5,auth.checkVersion());

        verify(auth,times(1)).login("aaa");
        verify(auth,times(1)).checkVersion();

    }


    @Test
    public void testCrypto(){
        when(crypto.getAllInfo("a","a","a")).thenReturn(null);
        assertNull(crypto.getAllInfo("a", "a", "a"));

        verify(crypto,times(1)).getAllInfo("a","a","a");

        when(crypto.getInfoID("a")).thenReturn(null);
        assertNull(crypto.getInfoID("a"));
        when(crypto.getInfoSlug("a")).thenReturn(null);
        assertNull(crypto.getInfoSlug("a"));
        when(crypto.getInfoSymbol("a")).thenReturn(null);
        assertNull(crypto.getInfoSymbol("a"));
    }
    @Test
    public void testDetail(){
        when(detail.getAllInfo("a","a","a")).thenReturn("hi");
        assertEquals("hi",detail.getAllInfo("a","a","a"));
        verify(detail,times(1)).getAllInfo("a","a","a");

        when(detail.getInfoID("a")).thenReturn("hi");
        assertEquals("hi", detail.getInfoID("a"));
        when(detail.getInfoSlug("a")).thenReturn("hi");
        assertEquals("hi", detail.getInfoSlug("a"));
        when(detail.getInfoSymbol("a")).thenReturn("hi");
        assertEquals("hi", detail.getInfoSymbol("a"));


    }

    @Test
    public void testConverse(){

        when(converse.converseID("1","1","10","1")).thenReturn("hi");
        assertEquals("hi",converse.converseID("1","1","10","1"));

        when(converse.converseSymbol("a","a","10","1")).thenReturn("hi");
        assertEquals("hi",converse.converseSymbol("a","a","10","1"));

        verify(converse,times(1)).converseID("1","1","10","1");
        verify(converse,times(1)).converseSymbol("a","a","10","1");

    }





}

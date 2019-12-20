package com.example.m2app;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.test.mock.MockContext;

import androidx.test.platform.app.InstrumentationRegistry;

import com.example.data.MyResult;
import com.example.data.Requests;
import com.mapbox.mapboxsdk.Mapbox;

import org.junit.Test;
import org.junit.runner.Request;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.MockitoAnnotations;
import org.mockito.MockitoSession;
import org.mockito.junit.MockitoJUnitRunner;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;

import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

/*
import androidx.test.platform.app.InstrumentationRegistry;

*/

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
//@RunWith(RobolectricTestRunner.class)
@RunWith(PowerMockRunner.class)
public class ExampleUnitTest {
    @Test
    //@Config(sdk = Build.VERSION_CODES.O_MR1)
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }


    @Test
    //@Config(sdk = Build.VERSION_CODES.O_MR1)
    public void testMyResultClass() {
        MyResult myres = new MyResult(1, 2);
        assertEquals(1, myres.getFirst());
        assertEquals(2, myres.getSecond());
    }
    //@Test
    //@Config(sdk = Build.VERSION_CODES.O_MR1)
    //public void useAppContext() {
        // Context of the app under test.
      //  Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
     //   assertEquals("com.example.m2app", appContext.getPackageName());
    //}
    @Test
   // @Config(sdk = Build.VERSION_CODES.O_MR1)
    public void mobileProvidersCodeTest() {
        assertNotEquals(0, Requests.getMNCList(250).length);
    }

    @Test
    // @Config(sdk = Build.VERSION_CODES.O_MR1)
    public void mobileCountryCodeTest() {
        assertEquals(250, (int)Requests.getMCC("ru"));
    }
    @Test
    public void MobileCountryNameTest() throws IOException {
        String code = Requests.getCountryCode(55.751244, 37.618423);
        assertTrue(code
                .startsWith("{\"status\":\"ok\""));
        assertTrue(code.endsWith("}}"));
    }

    @Test
    public void StationsTest() throws IOException {
        String[] getStations = Requests.getStations(
                2224, 16, "ru");
        assertNotNull(getStations);
    }

    @Test
    @PrepareForTest({Mapbox.class})
    public void CellIdTest() {

        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(Mapbox.class);
        int type = TelephonyManager.PHONE_TYPE_GSM;

        TelephonyManager mockTelephonyManager = mock(TelephonyManager.class);
        GsmCellLocation location = mock(GsmCellLocation.class);

        when(location.getCid()).thenReturn(0);
        when(location.getLac()).thenReturn(0);
        when(mockTelephonyManager.getPhoneType()).thenReturn(type);
        when(mockTelephonyManager.getCellLocation()).thenReturn(location);

        MockContext mockContext =  mock(MockContext.class);
        when(mockContext.getSystemService(any(String.class))).thenReturn(mockTelephonyManager);

        when(Mapbox.getApplicationContext()).thenReturn(mockContext);

        MyResult res = Requests.getCellId();
        assertEquals(0, res.getFirst());
        assertEquals(0, res.getSecond());
    }
}
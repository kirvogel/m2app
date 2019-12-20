package com.example.m2app;

import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.test.mock.MockContext;

import com.example.data.JSONCreator;
import com.example.data.MobileCountryCodeMobileNetworkCode;
import com.example.data.MyResult;
import com.example.data.Requests;
import com.mapbox.mapboxsdk.Mapbox;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

    /*@Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class);
    @Rule
    public ActivityTestRule<MapActivity> ruleMap = new ActivityTestRule<>(MapActivity.class);*/

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
    /*@Test
    //@Config(sdk = Build.VERSION_CODES.O_MR1)
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.m2app", appContext.getPackageName());
    }*/
    @Test
   // @Config(sdk = Build.VERSION_CODES.O_MR1)
    public void mobileProvidersCodeTestException() {
        assertEquals(0, Requests.getMNCList(2500000).length);
    }

    public void mobileProvidersCodeTest() {
        assertNotEquals(0, Requests.getMNCList(250).length);
    }

    @Test
    // @Config(sdk = Build.VERSION_CODES.O_MR1)
    public void mobileCountryCodeTest() {
        assertEquals(250, (int)Requests.getMCC("ru"));
    }

    @Test
    // @Config(sdk = Build.VERSION_CODES.O_MR1)
    public void mobileCountryCodeTestException() {
        assertEquals(0, (int)Requests.getMCC(null));
        assertEquals(0, (int)Requests.getMCC("ruu"));
    }
    @Test
    public void MobileCountryNameTest() throws IOException {
        String code = Requests.getCountryCode(55.751244, 37.618423);
        assertTrue(code
                .startsWith("{\"status\":\"ok\""));
        assertTrue(code.endsWith("}}"));
    }

    @Test
    public void MobileCountryNameTestError() throws IOException {
        String code = Requests.getCountryCode(-1, -1);
        assertEquals("{\"status\":\"error\",\"message\":\"NO_MATCHES\"}", code);
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

    @Test
    @PrepareForTest({Requests.class, JSONCreator.class})
    public void MobileCountryCodeMobileNetworkCodeTest() throws IOException, JSONException {
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(Requests.class);
        PowerMockito.mockStatic(JSONCreator.class);

        MyResult location = mock(MyResult.class);
        when(location.getFirst()).thenReturn(0);
        when(location.getSecond()).thenReturn(0);

        JSONObject obj1 = mock(JSONObject.class);
        JSONObject obj2 = mock(JSONObject.class);
        JSONObject[] array = new JSONObject[]{obj1, obj2};

        when(Requests.getCellId()).thenReturn(location);
        when(Requests.getCountryCode(any(Double.class), any(Double.class))).thenReturn("ru");
        when(Requests.getMCC("ru")).thenReturn(250);
        when(Requests.getMNCList(any(Integer.class))).thenReturn(new int[]{1,2,3});
        when(Requests.getStations(any(Integer.class), any(Integer.class), any(String.class))).thenReturn(new String[]{});

        when(JSONCreator.createJSONObject(any(String.class))).thenReturn(obj1);
        when(JSONCreator.createArray(any(Integer.class))).thenReturn(array);

        assertEquals(2, MobileCountryCodeMobileNetworkCode.getStations(0, 0).length);
        assertEquals("", MobileCountryCodeMobileNetworkCode.getCoutryName(0 , 0));
    }
}
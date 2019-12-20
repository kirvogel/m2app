package com.example.m2app;

import com.example.data.MobileCountryCodeMobileNetworkCode;
import com.example.data.MyResult;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

//import androidx.test.platform.app.InstrumentationRegistry;
//

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
//@RunWith(RobolectricTestRunner.class)
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
    /*public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.m2app", appContext.getPackageName());
    }*/
    @Test
   // @Config(sdk = Build.VERSION_CODES.O_MR1)
    public void mobileProvidersCodeTest() {
        assertNotEquals(0, MobileCountryCodeMobileNetworkCode.getMNCList(250).length);
    }

    @Test
    // @Config(sdk = Build.VERSION_CODES.O_MR1)
    public void mobileCountryCodeTest() {
        assertEquals(250, (int)MobileCountryCodeMobileNetworkCode.getMCC("ru"));
    }
    @Test
    public void MobileCountryNameTest() throws IOException {
        String code = MobileCountryCodeMobileNetworkCode.getCountryCode(55.751244, 37.618423);
        assertTrue(code
                .startsWith("{\"status\":\"ok\""));
        assertTrue(code.endsWith("}}"));
    }
}
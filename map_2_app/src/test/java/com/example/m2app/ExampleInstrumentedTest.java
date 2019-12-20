package com.example.m2app;

import android.content.Context;
import android.os.Build;

import androidx.test.platform.app.InstrumentationRegistry;

import com.example.data.MobileCountryCodeMobileNetworkCode;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

@RunWith(RobolectricTestRunner.class)
public class ExampleInstrumentedTest {
    @Test
    @Config(sdk = Build.VERSION_CODES.O_MR1)
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.m2app", appContext.getPackageName());
    }
    @Test
    @Config(sdk = Build.VERSION_CODES.O_MR1)
    public void mobileCoutryCodeTest() {
        assertNotEquals(0, MobileCountryCodeMobileNetworkCode.getMNCList(250).length);
    }
}

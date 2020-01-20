package com.wishakhn.jangoassistant;

import android.content.Context;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;


@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
	@Test
	public void useAppContext() throws Exception {
		// Context of the app under test.
		Context appContext = InstrumentationRegistry.getTargetContext();

		assertEquals("com.wishakhn.jangoassistant", appContext.getPackageName());
	}
}

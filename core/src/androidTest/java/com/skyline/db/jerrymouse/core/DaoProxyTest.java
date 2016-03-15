package com.skyline.db.jerrymouse.core;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import junit.framework.TestCase;

import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Created by jairus on 15/12/21.
 */
public class DaoProxyTest extends TestCase {

	private static final String LOG_TAG = DaoProxyTest.class.getSimpleName();

	public static class TestClass {

		public int valInt;

		public int valInt2;

		public int valInt3;

		public String valStr;

	}

	public class BooleanTypeAdapter extends TypeAdapter<Integer> {
		@Override
		public void write(JsonWriter out, Integer value) throws IOException {
			Log.d(LOG_TAG, "value:" + value);
			if (value == null) {
				out.nullValue();
				return;
			}
			boolean valBoolean = value != 0 ? true : false;
			out.value(valBoolean);
		}

		@Override
		public Integer read(JsonReader in) throws IOException {
			Log.d(LOG_TAG, "in.peek():" + in.peek());
			if (in.peek() == JsonToken.NULL) {
				in.nextNull();
				return 0;
			} else if (in.peek() == JsonToken.NUMBER) {
				return in.nextInt();
			}

			boolean valInt = in.nextBoolean();
			return valInt ? 1 : 0;
		}
	}

	@Test
	public void test() {

		String json = "{\"valInt\":true,\"valInt2\":2,\"valInt3\":null,\"valStr\":\"this is valStr\"}";
		TestClass t = fromJson(json,
				new Type[]{int.class},
				new TypeAdapter[]{new BooleanTypeAdapter()},
				TestClass.class);
		Log.d(LOG_TAG, "TestClass, valInt: " + t.valInt + ", valInt2: " + t.valInt2 + ", valInt3: " + t.valInt3 + ", valStr: " + t.valStr);

	}

	public static <T> T fromJson(String json, Type[] types, TypeAdapter[] adapters, Class<T> classOfT) {
		T ret = null;
		try {
			GsonBuilder builder = new GsonBuilder();
			if (adapters != null && types != null && types.length == adapters.length) {
				for (int i = 0; i < types.length; i++) {
					builder.registerTypeAdapter(types[i], adapters[i]);
				}
			}
			Gson gson = builder.create();
			ret = gson.fromJson(json, classOfT);
		} catch (Exception e) {
			Log.d(LOG_TAG, "", e);
		}
		return ret;
	}

}

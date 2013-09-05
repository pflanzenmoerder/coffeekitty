package de.caffeine.kitty.web.tools;

import static org.mockito.Mockito.mock;

import java.lang.reflect.Field;

import org.springframework.util.ReflectionUtils;


public final class TestHelper {
	private TestHelper() {
	}
	
	public static <T> T setAndGetServiceMock(Object target, Class<T> clazz) 
			throws NoSuchFieldException {

		T mockService = mock(clazz);
		for(Field platformsField:target.getClass().getDeclaredFields()) {
			if(platformsField.getType().equals(clazz)) {
				ReflectionUtils.makeAccessible(platformsField);
				ReflectionUtils.setField(platformsField, target, mockService);
			}
		}
		return mockService;
	}
}

package com.github.ghkvud2.ft4j.util;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

import com.github.ghkvud2.ft4j.annotation.Order;
import com.github.ghkvud2.ft4j.exception.MissingOrderAnnotationException;

public class AnnotationUtils {

	public static List<Field> getDeclaredFieldsOrdering(Object obj) {

		return Arrays.stream(obj.getClass().getDeclaredFields())
				.filter(field -> !field.isSynthetic())
				.sorted(Comparator.comparingInt(field -> {
			
			if (!field.isAnnotationPresent(Order.class)) {
				throw new MissingOrderAnnotationException(
						String.format("Field '%s' is missing the Order annotation.", field.getName()));
			}
			Order annotation = field.getDeclaredAnnotation(Order.class);
			return annotation.value();
		})).collect(Collectors.toList());
	}
}

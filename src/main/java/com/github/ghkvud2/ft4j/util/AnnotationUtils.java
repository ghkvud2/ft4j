package com.github.ghkvud2.ft4j.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.github.ghkvud2.ft4j.annotation.*;
import com.github.ghkvud2.ft4j.exception.MissingOrderAnnotationException;
import com.github.ghkvud2.ft4j.exception.MissingRequiredAnnotationException;

@SuppressWarnings("deprecation")
public class AnnotationUtils {

	private static final Map<Class<? extends Annotation>, Function<Annotation, Integer>> orderExtractors = new HashMap<>();

	static {
		orderExtractors.put(DoubleValue.class, annotation -> ((DoubleValue) annotation).order());
		orderExtractors.put(FloatValue.class, annotation -> ((FloatValue) annotation).order());
		orderExtractors.put(IntValue.class, annotation -> ((IntValue) annotation).order());
		orderExtractors.put(ShortValue.class, annotation -> ((ShortValue) annotation).order());
		orderExtractors.put(LongValue.class, annotation -> ((LongValue) annotation).order());
		orderExtractors.put(StringValue.class, annotation -> ((StringValue) annotation).order());
		orderExtractors.put(ObjectValue.class, annotation -> ((ObjectValue) annotation).order());
	}

	@Deprecated
	public static List<Field> getDeclaredFieldsOrdering(Object obj) {

		return Arrays.stream(obj.getClass().getDeclaredFields()).filter(field -> !field.isSynthetic())
				.sorted(Comparator.comparingInt(field -> {

					if (!field.isAnnotationPresent(Order.class)) {
						throw new MissingOrderAnnotationException(
								String.format("Field '%s' is missing the Order annotation.", field.getName()));
					}
					Order annotation = field.getDeclaredAnnotation(Order.class);
					return annotation.value();
				})).collect(Collectors.toList());
	}

	public static List<Field> getDeclaredFieldsWithOrderProperty(Object obj) {

		return Arrays.stream(obj.getClass().getDeclaredFields())
				.filter(field -> !field.isSynthetic() && Arrays.stream(field.getAnnotations())
						.anyMatch(annotation -> orderExtractors.containsKey(annotation.annotationType())))
				.sorted(Comparator.comparingInt(AnnotationUtils::getOrder)).collect(Collectors.toList());
	}

	private static int getOrder(Field field) {
		List<Annotation> validAnnotations = Arrays.stream(field.getAnnotations())
				.filter(annotation -> orderExtractors.containsKey(annotation.annotationType()))
				.collect(Collectors.toList());

		if (validAnnotations.size() > 1) {
			throw new MissingRequiredAnnotationException(
					String.format("Field '%s' is missing the Order annotation.", field.getName()));
		}

		return validAnnotations.stream()
				.map(annotation -> orderExtractors.get(annotation.annotationType()).apply(annotation)).findFirst()
				.orElseThrow(() -> new IllegalStateException("Field does not have an order annotation"));
	}
}

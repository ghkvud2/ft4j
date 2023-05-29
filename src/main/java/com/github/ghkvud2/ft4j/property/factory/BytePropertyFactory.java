package com.github.ghkvud2.ft4j.property.factory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.function.BiFunction;

import com.github.ghkvud2.ft4j.annotation.*;
import com.github.ghkvud2.ft4j.exception.InvalidAnnotationConfigurationException;
import com.github.ghkvud2.ft4j.property.AbstractProperty;
import com.github.ghkvud2.ft4j.property.bytes.*;

public class BytePropertyFactory implements PropertyFactory {

	@Override
	public AbstractProperty<?> createProperty(Object obj, Field field) {
		return BytePropertyEnum.createProperty(obj, field);
	}

//	@Override
//	public AnnotationFieldProperty createProperty(Field field) {
//		return BytePropertyEnum.createProperty(field);
//	}

	enum BytePropertyEnum {

//		SHORT_VALUE(ShortValue.class, (object, field) -> new ShortProperty(object, field),
//				field -> new ShortProperty(field)),
//		INT_VALUE(IntValue.class, (object, field) -> new IntProperty(object, field), field -> new IntProperty(field)),
//		LONG_VALUE(LongValue.class, (object, field) -> new LongProperty(object, field),
//				field -> new LongProperty(field)),
//		FLOAT_VALUE(FloatValue.class, (object, field) -> new FloatProperty(object, field),
//				field -> new FloatProperty(field)),
//		DOUBLE_VALUE(DoubleValue.class, (object, field) -> new DoubleProperty(object, field),
//				field -> new DoubleProperty(field)),
//		MESSAGE(Message.class, (object, field) -> new MessageProperty(object, field),
//				field -> new MessageProperty(field));
		
//		private BytePropertyEnum(Class<? extends Annotation> annotationType,
//				BiFunction<Object, Field, AbstractProperty<?>> biFunction,
//				Function<Field, AbstractProperty<?>> function) {
//			this.annotationType = annotationType;
//			this.biFunction = biFunction;
//			this.function = function;
//		}
		
//		public Function<Field, AbstractProperty<?>> getFunction() {
//			return function;
//		}
		
		SHORT_VALUE(ShortValue.class, (object, field) -> new ShortProperty(object, field)),
		INT_VALUE(IntValue.class, (object, field) -> new IntProperty(object, field)),
		LONG_VALUE(LongValue.class, (object, field) -> new LongProperty(object, field)),
		FLOAT_VALUE(FloatValue.class, (object, field) -> new FloatProperty(object, field)),
		DOUBLE_VALUE(DoubleValue.class, (object, field) -> new DoubleProperty(object, field)),
		MESSAGE(Message.class, (object, field) -> new MessageProperty(object, field));

		private final Class<? extends Annotation> annotationType;
		private final BiFunction<Object, Field, AbstractProperty<?>> biFunction;
//		private final Function<Field, AbstractProperty<?>> function;

		private BytePropertyEnum(Class<? extends Annotation> annotationType,
				BiFunction<Object, Field, AbstractProperty<?>> biFunction) {
			this.annotationType = annotationType;
			this.biFunction = biFunction;
		}

		public Class<? extends Annotation> getAnnotationType() {
			return annotationType;
		}

		public BiFunction<Object, Field, AbstractProperty<?>> getBiFunction() {
			return biFunction;
		}

		public static AbstractProperty<?> createProperty(Object obj, Field field) {

			return Arrays.stream(values()).filter(e -> field.isAnnotationPresent(e.getAnnotationType())).findFirst()
					.orElseThrow(() -> new InvalidAnnotationConfigurationException(
							"Field must have accurate annotation, but not both."))
					.getBiFunction().apply(obj, field);
		}

//		public static AbstractProperty<?> createProperty(Field field) {
//
//			return Arrays.stream(values()).filter(e -> field.isAnnotationPresent(e.getAnnotationType())).findFirst()
//					.orElseThrow(() -> new InvalidAnnotationConfigurationException(
//							"Field must have accurate annotation, but not both."))
//					.getFunction().apply(field);
//		}
	}

}

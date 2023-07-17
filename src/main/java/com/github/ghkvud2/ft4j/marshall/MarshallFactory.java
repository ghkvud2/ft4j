package com.github.ghkvud2.ft4j.marshall;

import java.util.ArrayList;
import java.util.List;

import com.github.ghkvud2.ft4j.constant.*;
import com.github.ghkvud2.ft4j.converter.*;
import com.github.ghkvud2.ft4j.property.factory.BytePropertyFactory;
import com.github.ghkvud2.ft4j.property.factory.PropertyFactory;
import com.github.ghkvud2.ft4j.validator.*;
import com.github.ghkvud2.ft4j.validator.bytes.*;

public class MarshallFactory {
	private PropertyFactory propertyFactory;
	private CharsetConverter converter;
	private Marshaller marshaller;
	private ValidatorManager validatorManager;

	private PropertyFactoryType propertyFactoryType = PropertyFactoryType.BYTE;
	private MarshallerType marshallerType = MarshallerType.BYTE;
	private MarshallManagerType marshallManagerType = MarshallManagerType.DEFAULT;
	private ValidationManagerType validationManagerType = ValidationManagerType.BYTE;

	private List<PropertyValidator> validators = new ArrayList<>();

	public static MarshallFactory builder() {
		return new MarshallFactory();
	}

	public MarshallFactory() {
		registerDefaultValidators();
	}

	private void registerDefaultValidators() {
		validators.add(new ByteDecimalPropertyValidator());
		validators.add(new ByteShortPropertyValidator());
		validators.add(new ByteIntPropertyValidator());
		validators.add(new ByteLongPropertyValidator());
		validators.add(new ByteStringPropertyValidator());
	}

	public MarshallFactory manager(MarshallManagerType marshallerType) {
		this.marshallManagerType = marshallerType;
		return this;
	}

	public MarshallFactory mashaller(MarshallerType marshallType) {
		this.marshallerType = marshallType;
		return this;
	}

	public MarshallFactory propertyFactory(PropertyFactoryType propertyFactoryType) {
		this.propertyFactoryType = propertyFactoryType;
		return this;
	}

	public MarshallFactory validationManager(ValidationManagerType validationManagerType) {
		this.validationManagerType = validationManagerType;
		return this;
	}

	public MarshallFactory converter(ConverterType converterType) {
		switch (converterType) {
		case EUC_KR:
			this.converter = new EucKrCharsetConverter();
			break;
		case UTF_8:
			this.converter = new Utf8CharsetConverter();
			break;
		default:
			throw new IllegalArgumentException("Invalid ConverterType.");
		}
		return this;
	}

	public MarshallFactory addValidators(List<PropertyValidator> validatorList) {
		this.validators.addAll(validatorList);
		return this;
	}

	public MarshallManager build() {

		if (converter == null) {
			throw new IllegalStateException("All dependencies must be set before building a Marshaller.");
		}

		switch (propertyFactoryType) {
		case BYTE:
			propertyFactory = new BytePropertyFactory();
			break;
		default:
			throw new IllegalArgumentException("Invalid PropertyFactoryType.");
		}

		switch (validationManagerType) {
		case BYTE:
			validatorManager = new ByteValidatorManager(converter, validators);
			break;
		default:
			throw new IllegalArgumentException("Invalid ValidationManagerType.");
		}

		switch (marshallerType) {
		case BYTE:
			marshaller = new ByteBasedMarshaller(converter);
			break;
		default:
			throw new IllegalArgumentException("Invalid MarshallerType.");
		}

		MarshallManager manager;
		switch (marshallManagerType) {
		case DEFAULT:
			manager = new DefaultMarshallManager(propertyFactory, marshaller, validatorManager);
			break;
		default:
			throw new IllegalArgumentException("Invalid MarshallManagerType.");
		}

		return manager;
	}

}

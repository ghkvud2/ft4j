package com.github.ghkvud2.ft4j.unmarshall;

import java.util.ArrayList;
import java.util.List;

import com.github.ghkvud2.ft4j.constant.*;
import com.github.ghkvud2.ft4j.converter.*;
import com.github.ghkvud2.ft4j.property.factory.BytePropertyFactory;
import com.github.ghkvud2.ft4j.property.factory.PropertyFactory;
import com.github.ghkvud2.ft4j.validator.PropertyValidator;
import com.github.ghkvud2.ft4j.validator.bytes.*;

public class UnMarshallFactory {

	private PropertyFactory propertyFactory;
	private CharsetConverter converter;
	private UnMarshaller unMarshaller;
//	private ValidatorManager validatorManager;

	private PropertyFactoryType propertyFactoryType = PropertyFactoryType.BYTE;
	private UnMarshallerType unMarshallerType = UnMarshallerType.BYTE;
	private UnMarshallManagerType unMarshallManagerType = UnMarshallManagerType.DEFAULT;
//	private ValidationManagerType validationManagerType = ValidationManagerType.BYTE;

	private List<PropertyValidator> validators = new ArrayList<>();

	public static UnMarshallFactory builder() {
		return new UnMarshallFactory();
	}

	public UnMarshallFactory() {
		registerDefaultValidators();
	}

	private void registerDefaultValidators() {
		validators.add(new ByteDecimalPropertyValidator());
		validators.add(new ByteIntPropertyValidator());
		validators.add(new ByteMessagePropertyValidator());
	}

	public UnMarshallFactory manager(UnMarshallManagerType unMarshallManagerType) {
		this.unMarshallManagerType = unMarshallManagerType;
		return this;
	}

	public UnMarshallFactory mashaller(UnMarshallerType unMarshallerType) {
		this.unMarshallerType = unMarshallerType;
		return this;
	}

	public UnMarshallFactory propertyFactory(PropertyFactoryType propertyFactoryType) {
		this.propertyFactoryType = propertyFactoryType;
		return this;
	}

//	public UnMarshallFactory validationManager(ValidationManagerType validationManagerType) {
//		this.validationManagerType = validationManagerType;
//		return this;
//	}

	public UnMarshallFactory converter(ConverterType converterType) {
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

	public UnMarshallFactory addValidators(List<PropertyValidator> validatorList) {
		this.validators.addAll(validatorList);
		return this;
	}

	public UnMarshallManager build() {

		if (converter == null) {
			throw new IllegalStateException("All dependencies must be set before building a UnMarshaller.");
		}

		switch (propertyFactoryType) {
		case BYTE:
			propertyFactory = new BytePropertyFactory();
			break;
		default:
			throw new IllegalArgumentException("Invalid PropertyFactoryType.");
		}

//		switch (validationManagerType) {
//		case BYTE:
//			validatorManager = new ByteValidatorManager(converter, validators);
//			break;
//		default:
//			throw new IllegalArgumentException("Invalid ValidationManagerType.");
//		}

		switch (unMarshallerType) {
		case BYTE:
			unMarshaller = new ByteBasedUnMarshaller(converter);
			break;
		default:
			throw new IllegalArgumentException("Invalid UnMarshallerType.");
		}

		UnMarshallManager manager;
		switch (unMarshallManagerType) {
		case DEFAULT:
			manager = new DefaultUnMarshallManager(propertyFactory, unMarshaller);
			break;
		default:
			throw new IllegalArgumentException("Invalid UnMarshallManagerType.");
		}

		return manager;
	}

}

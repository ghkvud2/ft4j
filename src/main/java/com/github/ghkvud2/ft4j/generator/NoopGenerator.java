package com.github.ghkvud2.ft4j.generator;

public class NoopGenerator implements Generator {

	@Override
	public String generate() {
		throw new UnsupportedOperationException("NoopGenerator method should not be called.");
	}

}

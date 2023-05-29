package com.github.ghkvud2.ft4j.annotation.constant;

public enum PaddingByte {
	ZERO((byte) '0'), SPACE((byte) ' '), NULL((byte) '\0');

	private final byte value;

	PaddingByte(byte value) {
		this.value = value;
	}

	public byte getValue() {
		return value;
	}
}

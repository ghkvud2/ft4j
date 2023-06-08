package com.github.ghkvud2.ft4j.unmarshall;

public interface UnMarshallManager {
	<T> T unmarshall(byte[] bytes, Class<T> clazz);
}

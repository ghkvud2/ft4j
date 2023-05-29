package com.github.ghkvud2.ft4j.unmarshall;

public interface UnMarshallManager {
	<T> T unmarshall(String input, Class<T> clazz);
}

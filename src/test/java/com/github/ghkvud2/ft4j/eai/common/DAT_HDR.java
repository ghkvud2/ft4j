package com.github.ghkvud2.ft4j.eai.common;

import com.github.ghkvud2.ft4j.annotation.*;

public class DAT_HDR {

	@Order(1)
	@Message(length = 3)
	private String DAT_KDCD;

	@Order(2)
	@LongValue(length = 7)
	private long DAT_LEN;

	public DAT_HDR() {
		super();
	}

}

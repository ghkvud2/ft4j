package com.github.ghkvud2.ft4j.nested.domain;

import com.github.ghkvud2.ft4j.annotation.Message;
import com.github.ghkvud2.ft4j.annotation.Order;

public class Address {

	@Order(1)
	@Message(length = 10)
	private String addr1;

	@Order(2)
	@Message(length = 10)
	private String addr2;

	@Order(3)
	@Message(length = 10)
	private String addr3;

	public Address(String addr1, String addr2, String addr3) {
		this.addr1 = addr1;
		this.addr2 = addr2;
		this.addr3 = addr3;
	}

	public Address() {
	}

	public String getAddr1() {
		return addr1;
	}

	public String getAddr2() {
		return addr2;
	}

	public String getAddr3() {
		return addr3;
	}

}

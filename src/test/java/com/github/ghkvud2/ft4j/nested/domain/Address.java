package com.github.ghkvud2.ft4j.nested.domain;

import com.github.ghkvud2.ft4j.annotation.StringValue;

public class Address {

	@StringValue(order = 1, length = 10)
	private String addr1;

	@StringValue(order = 2, length = 10)
	private String addr2;

	@StringValue(order = 3, length = 10)
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

	@Override
	public String toString() {
		return "Address [addr1=" + addr1 + ", addr2=" + addr2 + ", addr3=" + addr3 + "]";
	}

}

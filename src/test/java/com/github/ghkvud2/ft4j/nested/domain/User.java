package com.github.ghkvud2.ft4j.nested.domain;

import com.github.ghkvud2.ft4j.annotation.*;

public class User {

	@Order(1)
	@Message(length = 10)
	private String name;

	@Order(2)
	@IntValue(length = 10)
	private int age;

	@Order(3)
	@Message(length = 20)
	private String email;

	@Order(4)
	private Address address;

	public User(String name, int age, String email, Address address) {
		this.name = name;
		this.age = age;
		this.email = email;
		this.address = address;
	}

	public User() {
		super();
	}

	public String getName() {
		return name;
	}

	public int getAge() {
		return age;
	}

	public String getEmail() {
		return email;
	}

	public Address getAddress() {
		return address;
	}

}

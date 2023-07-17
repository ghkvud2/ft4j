package com.github.ghkvud2.ft4j.nested.domain;

import com.github.ghkvud2.ft4j.annotation.*;

public class User {

	@StringValue(order = 1, length = 10)
	private String name;

	@IntValue(order = 2, length = 10)
	private int age;

	@StringValue(order = 3, length = 20)
	private String email;

	@ObjectValue(order = 4)
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

	@Override
	public String toString() {
		return "User [name=" + name + ", age=" + age + ", email=" + email + ", address=" + address + "]";
	}

}

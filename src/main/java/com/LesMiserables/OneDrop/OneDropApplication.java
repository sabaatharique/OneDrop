package com.LesMiserables.OneDrop;

import com.LesMiserables.OneDrop.authentication.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OneDropApplication {
	private static User user = new User();

	public static void main(String[] args) {
		SpringApplication.run(com.LesMiserables.OneDrop.OneDropApplication.class, args);
		user.setPhone("1234567890");
		System.out.println(user.getPhone());
	}
}

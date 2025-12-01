package com.register;
import java.util.Scanner;
public class App {
	 public static void main(String[] args) {
	        UserRepository userRepository = new UserRepository();
	        userRepository.createTableIfNotExists();

	        Scanner scanner = new Scanner(System.in);

	        System.out.println("Welcome to the CLI Registration App");
	        System.out.print("Enter username: ");
	        String username = scanner.nextLine();

	        System.out.print("Enter email: ");
	        String email = scanner.nextLine();

	        System.out.print("Enter password: ");
	        String password = scanner.nextLine();

	        User newUser = new User(username, email, password);
	        userRepository.save(newUser);
	        scanner.close();
	    }
}

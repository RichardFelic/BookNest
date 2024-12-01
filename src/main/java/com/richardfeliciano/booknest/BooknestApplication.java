package com.richardfeliciano.booknest;

import com.richardfeliciano.booknest.repository.AuthorRepository;
import com.richardfeliciano.booknest.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.richardfeliciano.booknest.client.Client;
@SpringBootApplication
@ComponentScan(basePackages = "com.richardfeliciano.booknest")
public class BooknestApplication implements CommandLineRunner {

	@Autowired
	private BookRepository 	bookRepository;

	@Autowired
	private AuthorRepository authorRepository;

	public static void main(String[] args) {
		SpringApplication.run(BooknestApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Client client = new Client(bookRepository, authorRepository);
		client.start();
	}
}

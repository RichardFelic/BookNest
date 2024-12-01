package com.richardfeliciano.booknest.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

import com.richardfeliciano.booknest.repository.*;
import com.richardfeliciano.booknest.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.richardfeliciano.booknest.data.GutendexResponse;
import com.richardfeliciano.booknest.model.*;

@Component
public class Client {

    // global variables
    private Scanner scanner = new Scanner(System.in);
    private APIConsumer apiConsumer = new APIConsumer();
    private JsonMapper jsonMapper = new JsonMapper();
    private static final String API_URL = "https://gutendex.com/books/";

    // repositories
    private BookRepository  bookRepository;
    private AuthorRepository authorRepository;

    // variables to store search results
    private List<Book> books = new ArrayList<>();

    // constructor
    @Autowired
    public Client(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    // menu

    public void start() {
        var option = -1;
        while (option != 0) {
            var menu = """
                    +----- BookNest  -----+
                    1. Search books by title
                    2. Search authors by name
                    3. List Top 10 books by downloads
                    4. List books searched by title
                    5. List authors searched by name
                    0. Exit""";
            System.out.println(menu);
            option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1 -> searchBookByTitle();
                case 2 -> searchAuthorByName();
                case 3 -> listTop10BooksByDownloads();
                case 4 -> listBooksSearchedByTitle();
                case 5 -> listAuthorsSearchedByName();
                case 0 -> System.out.println("Goodbye!");
                default -> System.out.println("Invalid option");
            }
        }
    }

    // search book by title
    private void searchBookByTitle() {
        System.out.print("Enter book title: ");
        var title = scanner.nextLine().trim().toLowerCase();

        // Busca primero en la base de datos
        var booksFromDatabase = bookRepository.findByTitleContainingIgnoreCase(title);
        if (!booksFromDatabase.isEmpty()) {
            System.out.println("Books found in the database: " + booksFromDatabase);
            return; // Termina el proceso si se encuentra en la base de datos
        }

        // Si no está en la base de datos, busca en la API
        String json = apiConsumer.consumeString(API_URL + "?search=" + title.replace(" ", "%20"));
        var response = jsonMapper.getData(json, GutendexResponse.class);

        // Extrae el libro si se encuentra en la respuesta
        Optional<Book> bookFromAPI = response.results().stream()
                .findFirst()
                .map(b -> new Book(
                        b.title(),
                        b.downloadCount(),
                        b.authors().stream()
                                .map(a -> new Author(a.name(), a.birthYear(), a.deathYear()))
                                .collect(Collectors.toList()),
                        b.genres(),
                        b.languages()
                ));

        if (bookFromAPI.isPresent()) {
            var bookFound = bookFromAPI.get();

            // Verifica si el libro ya existe en la base de datos antes de guardar
            var existingBook = bookRepository.findByTitle(bookFound.getTitle().toLowerCase());
            if (existingBook.isPresent()) {
                System.out.println("Book already exists in the database: " + existingBook.get());
            } else {
                bookRepository.save(bookFound);
                System.out.println("Book found in the API and saved to the database: " + bookFound);
            }
        } else {
            System.out.println("Book not found in the API or the database.");
        }
    }

    // search author by name
    private void searchAuthorByName() {
        System.out.print("Enter author name: ");
        var name = scanner.nextLine().trim().toLowerCase();

        // Busca primero en la base de datos
        var author = authorRepository.findByNameSQL(name);
        if (author.isPresent()) {
            System.out.println("Author found in the database: " + author.get());
            return; // Si se encuentra, termina el proceso
        }

        // Si no está en la base de datos, busca en la API
        String json = apiConsumer.consumeString(API_URL + "?search=" + name.replace(" ", "%20"));
        var response = jsonMapper.getData(json, GutendexResponse.class);

        // Extrae el autor si se encuentra en la respuesta
        var authorFound = response.results().stream()
                .findFirst()
                .map(a -> new Author(
                        a.authors().get(0).name(),
                        a.authors().get(0).birthYear(),
                        a.authors().get(0).deathYear()))
                .orElse(null);

        if (authorFound != null) {
            // Verifica si el autor ya existe en la base de datos antes de guardar
            var existingAuthor = authorRepository.findByName(authorFound.getName().toLowerCase());
            if (existingAuthor.isPresent()) {
                System.out.println("Author already exists in the database: " + existingAuthor.get());
            } else {
                // Guarda el autor en la base de datos
                authorRepository.save(authorFound);
                System.out.println("Author found in the API and saved to the database: " + authorFound);
            }
        } else {
            System.out.println("Author not found in the API or the database.");
        }
    }



    // list top 10 books by downloads
    private void listTop10BooksByDownloads() {
        System.out.println("Top 10 books by downloads: " + bookRepository.findTop10ByOrderByDownloadCountDesc());
    }

    // list books searched by title
    private void listBooksSearchedByTitle() {
        System.out.println("Books searched by title: ");
        books = bookRepository.findAll();
        books.forEach(System.out::println);
    }

    // list authors searched by name
    private void listAuthorsSearchedByName() {
        System.out.println("Authors searched by name: ");
        List<Author> authors = authorRepository.findAll();
        authors.forEach(System.out::println);
    }

    //

}
package org.fiap.library;

import com.github.javafaker.Faker;
import org.fiap.library.dao.AuthorDao;
import org.fiap.library.dao.BookDao;
import org.fiap.library.dao.PublisherDao;
import org.fiap.library.model.Author;
import org.fiap.library.model.Book;
import org.fiap.library.model.Publisher;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class Main {
    public static AuthorDao authorDao = new AuthorDao();
    public static BookDao bookDao = new BookDao();
    public static PublisherDao publisherDao = new PublisherDao();
    public static Set<Book> books = new HashSet<>();
    public static Set<Publisher> publishers = new HashSet<>();
    public static Set<Author> authors = new HashSet<>();

    public static void main(String[] args) {
        seedData();
        List<Publisher> pubDois = publishers.stream().toList().subList(1, 2);
        Publisher pub = publishers.stream().findAny().orElse(null);
        Author nonRegisteredAuthor = getAuthor(Optional.of(new HashSet<>(pubDois)), Optional.empty());
        authorDao.save(nonRegisteredAuthor);
        pub.getAuthors().add(nonRegisteredAuthor);
        publisherDao.save(pub);
        bookDao.save(createBooks(Optional.of(nonRegisteredAuthor)));
    }

    public static void seedData() {
        int i = 0;
        while (i < 11) {
            Author author = getAuthor(Optional.of(new LinkedHashSet<>(publishers)),
                    Optional.of(new LinkedHashSet<>(books)));
            authors.add(author);
            Book book = createBooks(
                    Optional.of(author));
            books.add(book);
            bookDao.save(book);

            Publisher publisher = getPublisher(Optional.of(authors), Optional.of(books));
            publisherDao.save(publisher);
            publishers.add(publisher);
            i++;
        }
    }

    public static Book createBooks(Optional<Author> author) {
        Author a = author.orElseGet(Author::new);
        Faker faker = new Faker();
        return new Book(
                faker.book().title(),
                faker.date().past(1, TimeUnit.DAYS),
                a);
    }

    @SuppressWarnings({"OptionalUsedAsFieldOrParameterType"})
    private static Author getAuthor(Optional<Set<Publisher>> publishers, Optional<Set<Book>> books) {
        Faker faker = new Faker();
        Set<Publisher> authorList = publishers.orElseGet(HashSet::new);
        Set<Book> bookList = books.orElseGet(HashSet::new);
        return new Author(
                faker.name().name(),
                faker.date().past(1, TimeUnit.DAYS),
                bookList,
                authorList);
    }

    private static Publisher getPublisher(Optional<Set<Author>> authors, Optional<Set<Book>> books) {
        Faker faker = new Faker();
        Set<Author> authorList = authors.orElseGet(HashSet::new);
        Set<Book> bookList = books.orElseGet(HashSet::new);
        return new Publisher(
                faker.company().name(),
                faker.number().digits(14),
                authorList, bookList);
    }
}
package io.medveckis.dataload;

import io.medveckis.model.Author;
import io.medveckis.model.Book;
import io.medveckis.model.Category;
import io.medveckis.repository.AuthorRepository;
import io.medveckis.repository.BookRepository;
import io.medveckis.repository.CategoryRepository;
import io.micronaut.discovery.event.ServiceReadyEvent;
import io.micronaut.runtime.event.annotation.EventListener;
import jakarta.inject.Singleton;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

@Singleton
public class DataLoadRunner {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;

    public DataLoadRunner(BookRepository bookRepository, AuthorRepository authorRepository, CategoryRepository categoryRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.categoryRepository = categoryRepository;
    }

    @EventListener
    @Transactional
    public void loadData(ServiceReadyEvent event) {
        // create categories
        IntStream.range(0, 5).forEach(idx -> categoryRepository.save(createCategory(idx)));
        // create authors
        IntStream.range(0, 5).forEach(idx -> authorRepository.save(createAuthor(idx)));
        // creating books
        List<Category> categories = StreamSupport.stream(categoryRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
        List<Author> authors = StreamSupport.stream(authorRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
        IntStream.range(0, 10).forEach(idx -> bookRepository.save(createBook(idx, authors, categories)));
    }

    private Category createCategory(int idx) {
        return new Category("Category_" + idx);
    }

    private Author createAuthor(int idx) {
        Random rng = new Random();
        String name = "name_" + idx;
        Date bornOn = new Date(rng.nextInt());
        return new Author(name, bornOn);
    }

    private Book createBook(int idx, List<Author> authors, List<Category> categories) {
        String name = "name_" + idx;
        int qty = idx + 10;
        double feeCounter = 0;
        Collections.shuffle(authors);
        Collections.shuffle(categories);
        return new Book(name, qty, ++feeCounter, authors.stream().limit(2).collect(Collectors.toList()),
                categories.stream().limit(3).collect(Collectors.toList()));
    }
}

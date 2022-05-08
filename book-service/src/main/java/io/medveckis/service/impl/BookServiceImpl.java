package io.medveckis.service.impl;

import io.medveckis.model.Book;
import io.medveckis.repository.BookRepository;
import io.medveckis.service.BookService;
import jakarta.inject.Singleton;

import java.util.List;

@Singleton
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }


    @Override
    public List<Book> getAllBooksByCategories(List<Integer> categoryIds) {
        return bookRepository.findAllBooksByCategories(categoryIds);
    }

    @Override
    public Book getBookById(Integer bookId) {
        return bookRepository.findById(bookId).orElseThrow();
    }
}

package io.medveckis.service;

import io.medveckis.model.Book;

import java.util.List;

public interface BookService {
    List<Book> getAllBooksByCategories(List<Integer> categoryIds);
    Book getBookById(Integer bookId);
}
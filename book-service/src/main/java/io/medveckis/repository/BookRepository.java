package io.medveckis.repository;

import io.medveckis.model.Book;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;

@Repository
public interface BookRepository extends CrudRepository<Book, Integer> {

    @Query(value = "SELECT DISTINCT b FROM Book b JOIN b.categories c WHERE c.id IN :categoryIds ORDER BY b.id ASC")
    List<Book> findAllBooksByCategories(List<Integer> categoryIds);
}

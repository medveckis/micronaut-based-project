package io.medveckis.repository;


import io.medveckis.model.Author;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface AuthorRepository extends CrudRepository<Author, Integer> {
}

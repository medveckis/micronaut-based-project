package io.medveckis.repository;

import io.medveckis.model.Category;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface CategoryRepository extends CrudRepository<Category, Integer> {
}

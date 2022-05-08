package io.medveckis.repository;

import io.medveckis.model.BookRecord;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.model.Sort;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;

@Repository
public interface BookRecordRepository extends CrudRepository<BookRecord, Integer> {
    List<BookRecord> listByUserId(Integer userId, Sort sort);
}

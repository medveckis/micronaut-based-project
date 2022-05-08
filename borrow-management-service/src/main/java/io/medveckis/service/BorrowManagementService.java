package io.medveckis.service;

import io.medveckis.web.dto.BookRecordData;
import io.reactivex.rxjava3.core.Single;

import java.util.List;

public interface BorrowManagementService {
    Single<Integer> createRecord(BookRecordData bookRecordData);
    Single<List<BookRecordData>> getAllRecordsByUserId(Integer userId);
//    Integer save(ZipContext zipContext);
}

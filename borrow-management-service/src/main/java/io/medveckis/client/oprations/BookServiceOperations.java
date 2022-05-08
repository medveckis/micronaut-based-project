package io.medveckis.client.oprations;

import io.medveckis.client.response.BookResponse;
import io.reactivex.rxjava3.core.Single;

public interface BookServiceOperations {
    Single<BookResponse> getBookById(Integer bookId);
}

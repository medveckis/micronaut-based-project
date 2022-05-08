package io.medveckis.client.fallback;

import io.medveckis.client.BookServiceClient;
import io.medveckis.client.response.BookResponse;
import io.micronaut.retry.annotation.Fallback;
import io.reactivex.rxjava3.core.Single;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Fallback
public class BookServiceClientFallback implements BookServiceClient {
    Logger LOG = LoggerFactory.getLogger(BookServiceClientFallback.class);

    @Override
    public Single<BookResponse> getBookById(Integer bookId) {
        LOG.error("Error during call to book-service (getBookById)");
        return Single.just(new BookResponse(bookId, -1., "Unknown name", -1));
    }
}

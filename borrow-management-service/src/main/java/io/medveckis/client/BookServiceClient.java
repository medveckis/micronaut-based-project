package io.medveckis.client;

import io.medveckis.client.oprations.BookServiceOperations;
import io.medveckis.client.response.BookResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.retry.annotation.CircuitBreaker;
import io.reactivex.rxjava3.core.Single;

@Client(id = "book-service", path = "/books")
@CircuitBreaker(attempts = "5", reset = "5s")
public interface BookServiceClient extends BookServiceOperations {

    @Override
    @Get(value = "/{bookId}")
    @Produces(MediaType.APPLICATION_JSON)
    Single<BookResponse> getBookById(@PathVariable(value = "bookId") Integer bookId);
}

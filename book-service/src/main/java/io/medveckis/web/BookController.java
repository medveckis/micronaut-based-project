package io.medveckis.web;

import io.medveckis.service.BookService;
import io.medveckis.web.dto.BookData;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
import org.reactivestreams.Publisher;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller(value = "/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @Get
    @Produces(MediaType.APPLICATION_JSON)
    public Publisher<BookData> getAllBooksByCategoriesAndType(@QueryValue(value = "type", defaultValue = "ALL") String type,
                                                              @QueryValue(value = "categories") List<String> categories) {

        return Flowable.fromStream(bookService.getAllBooksByCategories(categories
                        .stream()
                        .map(Integer::parseInt)
                        .collect(Collectors.toList())
                )
                .stream()
                .map(book -> new BookData(book.getId(), book.getName(), book.getQuantity(), book.getBookFee())));
    }

    @Get(value = "/{bookId}")
    public Single<BookData> getBookById(@PathVariable(value = "bookId") Integer bookId) {
        return Single.just(Stream.of(bookService.getBookById(bookId))
                .map(book -> new BookData(book.getId(), book.getName(), book.getQuantity(), book.getBookFee()))
                .findFirst()
                .orElseThrow());
    }
}

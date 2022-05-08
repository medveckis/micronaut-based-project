package io.medveckis.web;

import io.medveckis.service.BorrowManagementService;
import io.medveckis.web.dto.BookData;
import io.medveckis.web.dto.BookRecordData;
import io.medveckis.web.dto.UserData;
import io.medveckis.web.form.BookRecordForm;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.http.server.util.HttpHostResolver;
import io.reactivex.rxjava3.core.Single;

import java.net.URI;
import java.util.List;

@Controller(value = "/borrow-management")
public class BorrowManagementController {

    private final BorrowManagementService borrowManagementService;
    private final HttpHostResolver httpHostResolver;

    public BorrowManagementController(BorrowManagementService borrowManagementService, HttpHostResolver httpHostResolver) {
        this.borrowManagementService = borrowManagementService;
        this.httpHostResolver = httpHostResolver;
    }

    @Get(value = "/records")
    public Single<HttpResponse<List<BookRecordData>>> getAllRecordsByUser(@QueryValue(value = "userId") Integer userId) {
        return borrowManagementService.getAllRecordsByUserId(userId).map(HttpResponse::ok);
    }

    @Post(value = "/records")
    public Single<HttpResponse<?>> createRecord(HttpRequest<?> request, @Body BookRecordForm bookRecordForm) {
        return borrowManagementService.createRecord(convert(bookRecordForm)).map(id -> HttpResponse.created(URI.create(httpHostResolver.resolve(request) + request.getPath()
                + "/" + id)));
    }

    private BookRecordData convert(BookRecordForm bookRecordForm) {
        BookRecordData bookRecordData = new BookRecordData();
        UserData userData = new UserData();
        userData.setId(bookRecordForm.getUserId());
        bookRecordData.setUser(userData);
        BookData bookData = new BookData();
        bookData.setId(bookRecordForm.getBookId());
        bookRecordData.setBook(bookData);
        return bookRecordData;
    }
}

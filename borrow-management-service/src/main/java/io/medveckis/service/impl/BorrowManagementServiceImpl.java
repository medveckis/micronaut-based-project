package io.medveckis.service.impl;

import io.medveckis.client.oprations.BookServiceOperations;
import io.medveckis.client.oprations.UserServiceOperations;
import io.medveckis.client.response.BookResponse;
import io.medveckis.client.response.UserResponse;
import io.medveckis.model.BookRecord;
import io.medveckis.repository.BookRecordRepository;
import io.medveckis.service.BorrowManagementService;
import io.medveckis.web.dto.BookData;
import io.medveckis.web.dto.BookRecordData;
import io.medveckis.web.dto.UserData;
import io.micronaut.data.model.Sort;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import jakarta.inject.Singleton;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Singleton
public class BorrowManagementServiceImpl implements BorrowManagementService {

    private final BookRecordRepository bookRecordRepository;
    private final BookServiceOperations bookServiceOperations;
    private final UserServiceOperations userServiceOperations;

    public BorrowManagementServiceImpl(BookRecordRepository bookRecordRepository, BookServiceOperations bookServiceClient, UserServiceOperations userServiceClient) {
        this.bookRecordRepository = bookRecordRepository;
        this.bookServiceOperations = bookServiceClient;
        this.userServiceOperations = userServiceClient;
    }

    @Override
    public Single<Integer> createRecord(BookRecordData bookRecordData) {
        return Observable.zip(
                userServiceOperations.getUserById(bookRecordData.getUser().getId()).toObservable(),
                bookServiceOperations.getBookById(bookRecordData.getBook().getId()).toObservable(),
                (UserResponse ur, BookResponse br) -> new ZipContext(bookRecordData, ur, br)
        ).map(zc -> bookRecordRepository.save(convert(zc.getBookRecordData(), zc.getBookResponse().getBookFee(), zc.getUserResponse().getLoyaltyLevel())).getId())
                .single(Integer.MIN_VALUE);
    }


    @Override
    public Single<List<BookRecordData>> getAllRecordsByUserId(Integer userId) {
        List<BookRecord> bookRecords = new ArrayList<>(bookRecordRepository.listByUserId(userId, Sort.of(Sort.Order.asc("id"))));
        return Observable.fromStream(bookRecords.stream())
                .flatMap(br -> Observable.zip(
                        userServiceOperations.getUserById(userId).toObservable(),
                        bookServiceOperations.getBookById(br.getBookId()).toObservable(),
                        (uRes, bRes) -> convertToData(br, uRes, bRes)))
                .toList();
    }

    private BookRecord convert(BookRecordData bookRecordData, Double bookFee, Integer loyaltyLevel) {
        BookRecord bookRecord = new BookRecord();
        bookRecord.setBookId(bookRecordData.getBook().getId());
        bookRecord.setUserId(bookRecordData.getUser().getId());
        bookRecord.setExpirationDate(DateTime.now().plusMonths(3).toDate());
        bookRecord.setFee(loyaltyLevel == 0 ? bookFee : bookFee - (bookFee * 0.1 * loyaltyLevel));
        return bookRecord;
    }

    private BookRecordData convertToData(BookRecord bookRecord, UserResponse userResponse, BookResponse bookResponse) {
        BookRecordData bookRecordData = new BookRecordData();

        UserData userData = new UserData();
        if (Objects.nonNull(userResponse)) {
            userData.setId(userResponse.getUserId());
            userData.setFirstName(userResponse.getFirstName());
            userData.setLastName(userResponse.getLastName());
            userData.setEmail(userResponse.getEmail());
            userData.setAge(userResponse.getAge());
        }
        bookRecordData.setUser(userData);

        BookData bookData = new BookData();
        if (Objects.nonNull(userResponse)) {
            bookData.setId(bookResponse.getId());
            bookData.setName(bookResponse.getName());
        }
        bookRecordData.setBook(bookData);

        bookRecordData.setExpirationDate(bookRecord.getExpirationDate());
        bookRecordData.setFee(bookRecord.getFee());
        bookRecordData.setCompleted(bookRecord.getCompleted());

        return bookRecordData;
    }

    private static class ZipContext {
        private BookRecordData bookRecordData;
        private UserResponse userResponse;
        private BookResponse bookResponse;

        public ZipContext(BookRecordData bookRecordData, UserResponse userResponse, BookResponse bookResponse) {
            this.bookRecordData = bookRecordData;
            this.userResponse = userResponse;
            this.bookResponse = bookResponse;
        }

        public BookRecordData getBookRecordData() {
            return bookRecordData;
        }

        public void setBookRecordData(BookRecordData bookRecordData) {
            this.bookRecordData = bookRecordData;
        }

        public UserResponse getUserResponse() {
            return userResponse;
        }

        public void setUserResponse(UserResponse userResponse) {
            this.userResponse = userResponse;
        }

        public BookResponse getBookResponse() {
            return bookResponse;
        }

        public void setBookResponse(BookResponse bookResponse) {
            this.bookResponse = bookResponse;
        }
    }
}

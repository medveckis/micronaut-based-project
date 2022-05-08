package io.medveckis.client;

import io.medveckis.client.oprations.UserServiceOperations;
import io.medveckis.client.response.UserResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.retry.annotation.CircuitBreaker;
import io.reactivex.rxjava3.core.Single;

@Client(id = "user-service", path = "/users")
@CircuitBreaker(attempts = "5", reset = "5s")
public interface UserServiceClient extends UserServiceOperations {

    @Override
    @Get(value = "/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    Single<UserResponse> getUserById(@PathVariable(value = "userId") Integer userId);
}

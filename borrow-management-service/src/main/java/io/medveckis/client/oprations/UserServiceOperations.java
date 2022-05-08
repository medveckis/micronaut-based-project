package io.medveckis.client.oprations;

import io.medveckis.client.response.UserResponse;
import io.reactivex.rxjava3.core.Single;

public interface UserServiceOperations {
    Single<UserResponse> getUserById(Integer userId);
}

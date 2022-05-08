package io.medveckis.client.fallback;

import io.medveckis.client.UserServiceClient;
import io.medveckis.client.response.UserResponse;
import io.micronaut.retry.annotation.Fallback;
import io.reactivex.rxjava3.core.Single;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Fallback
public class UserServiceClientFallback implements UserServiceClient {
    Logger LOG = LoggerFactory.getLogger(UserServiceClientFallback.class);


    @Override
    public Single<UserResponse> getUserById(Integer userId) {
        LOG.error("Error during call to user-service (getUserById)");
        return Single.just(new UserResponse(userId, "Unknown", "Unknown", -1, "Unknown", 0, "Unknown"));
    }
}

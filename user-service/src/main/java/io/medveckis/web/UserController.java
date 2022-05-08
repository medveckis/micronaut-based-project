package io.medveckis.web;

import io.medveckis.model.Role;
import io.medveckis.service.UserService;
import io.medveckis.web.dto.UserData;
import io.medveckis.web.form.UserForm;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.http.server.util.HttpHostResolver;
import io.reactivex.rxjava3.core.Single;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

@Controller(value = "/users")
public class UserController {
    private final UserService userService;
    private final HttpHostResolver httpHostResolver;


    public UserController(UserService userService, HttpHostResolver httpHostResolver) {
        this.userService = userService;
        this.httpHostResolver = httpHostResolver;
    }

    @Post
    public Single<HttpResponse<?>> createUser(HttpRequest<?> request, @Body UserForm userForm) {
        return Single.just(HttpResponse.created(URI.create(httpHostResolver.resolve(request) + request.getPath()
                + "/" + userService.createUser(convert(userForm)))));
    }

    @Get(value = "/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Single<UserData> getUserById(@PathVariable(value = "userId") Integer userId) {
        return Single.just(Optional.ofNullable(userService.getUserById(userId))
                .map(user -> new UserData(user.getId(), user.getFirstName(), user.getLastName(), user.getAge(), user.getEmail(), user.getLoyaltyLevel(), user.getRole()))
                .orElse(new UserData()));
    }

    private UserData convert(UserForm userForm) {
        UserData userData = new UserData();
        userData.setFirstName(userForm.getFirstName());
        userData.setLastName(userForm.getLastName());
        userData.setEmail(userForm.getEmail());
        userData.setAge(userForm.getAge());
        userData.setRole(Role.CUSTOMER);
        return userData;
    }
}
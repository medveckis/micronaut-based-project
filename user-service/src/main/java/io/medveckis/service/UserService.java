package io.medveckis.service;

import io.medveckis.model.User;
import io.medveckis.web.dto.UserData;

public interface UserService {
    Integer createUser(UserData userData);
    User getUserById(Integer id);
}


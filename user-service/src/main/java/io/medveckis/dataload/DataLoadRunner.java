package io.medveckis.dataload;

import io.medveckis.model.Role;
import io.medveckis.model.User;
import io.medveckis.repository.UserRepository;
import io.micronaut.discovery.event.ServiceReadyEvent;
import io.micronaut.runtime.event.annotation.EventListener;
import jakarta.inject.Singleton;

import javax.transaction.Transactional;
import java.util.stream.IntStream;

@Singleton
public class DataLoadRunner {
    private final UserRepository userRepository;

    public DataLoadRunner(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @EventListener
    @Transactional
    public void loadData(ServiceReadyEvent event) {
        // create users
        IntStream.range(0, 4).forEach(idx -> userRepository.save(createTestUser(idx)));
    }

    private User createTestUser(int idx) {
        String firstName = "firstName_" + idx;
        String lastName = "lastName_" + idx;
        int age = idx + 1;
        String email = "example" + idx + "@mail.com";
        return new User(firstName, lastName, age, email, 2, age == 4 ? Role.ADMIN : Role.CUSTOMER);
    }
}


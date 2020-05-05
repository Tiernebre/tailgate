package com.tiernebre.tailgate.user;


import com.tiernebre.tailgate.jooq.tables.records.UsersRecord;
import com.tiernebre.tailgate.test.DatabaseIntegrationTestSuite;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceImplIntegrationTests extends DatabaseIntegrationTestSuite {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRecordPool userRecordPool;

    @Nested
    @DisplayName("createOne")
    public class CreateOneTests {
        @Test
        @DisplayName("fully persists a user with correct information")
        public void testCreateOneFullyPersists() throws InvalidUserException, UserAlreadyExistsException {
            CreateUserRequest createUserRequest = UserFactory.generateOneCreateUserRequest();
            UserDTO createdUser = userService.createOne(createUserRequest);
            UsersRecord userFound = userRecordPool.findOneByIdAndEmail(createdUser.getId(), createdUser.getEmail());
            assertAll(
                () -> assertNotEquals(createUserRequest.getPassword(), userFound.getPassword()),
                () -> assertEquals(createUserRequest.getEmail(), userFound.getEmail())
            );
        }
    }

    @Nested
    @DisplayName("findOneByEmailAndPassword")
    public class FindOneByEmailAndPasswordTests {
        @Test
        @DisplayName("fully retrieves a user by email and password that is legitimate")
        public void testCreateOneFullyPersists() throws InvalidUserException, UserAlreadyExistsException {
            CreateUserRequest createUserRequest = UserFactory.generateOneCreateUserRequest();
            UserDTO createdUser = userService.createOne(createUserRequest);
            UserDTO foundUser = userService.findOneByEmailAndPassword(createUserRequest.getEmail(), createUserRequest.getPassword()).orElse(null);
            assertNotNull(foundUser);
            assertAll(
                    () -> assertEquals(createdUser.getId(), foundUser.getId()),
                    () -> assertEquals(createUserRequest.getEmail(), foundUser.getEmail())
            );
        }

        @Test
        @DisplayName("returns empty if a user email is correct but the password is not")
        public void testCreateOneEnforcesPasswordMatching() throws InvalidUserException, UserAlreadyExistsException {
            CreateUserRequest createUserRequest = UserFactory.generateOneCreateUserRequest();
            userService.createOne(createUserRequest);
            Optional<UserDTO> foundUser = userService.findOneByEmailAndPassword(createUserRequest.getEmail(), createUserRequest.getPassword() + UUID.randomUUID().toString());
            assertTrue(foundUser.isEmpty());
        }
    }
}

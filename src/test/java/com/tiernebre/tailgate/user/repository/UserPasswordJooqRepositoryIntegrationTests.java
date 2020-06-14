package com.tiernebre.tailgate.user.repository;

import com.tiernebre.tailgate.jooq.tables.records.UsersRecord;
import com.tiernebre.tailgate.test.DatabaseIntegrationTestSuite;
import com.tiernebre.tailgate.token.password_reset.PasswordResetTokenRecordPool;
import com.tiernebre.tailgate.user.UserRecordPool;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class UserPasswordJooqRepositoryIntegrationTests extends DatabaseIntegrationTestSuite {
    @Autowired
    private UserPasswordJooqRepository userPasswordJooqRepository;

    @Autowired
    private UserRecordPool userRecordPool;

    @Autowired
    private PasswordResetTokenRecordPool passwordResetTokenRecordPool;

    @Nested
    @DisplayName("updateOneWithEmailAndNonExpiredResetToken")
    public class UpdateOneWithEmailAndNonExpiredResetTokenTests {
        @Test
        @DisplayName("updates a password if the email and reset token provided exist.")
        void updatesAPasswordIfTheEmailAndResetTokenProvidedExist() {
            UsersRecord user = userRecordPool.createAndSaveOne();
            String resetToken = passwordResetTokenRecordPool.createAndSaveOneForUser(user).getToken();
            String password = UUID.randomUUID().toString();
            userPasswordJooqRepository.updateOneWithEmailAndNonExpiredResetToken(password, user.getEmail(), resetToken);
            UsersRecord updatedUser = userRecordPool.findOneByIdAndEmail(user.getId(), user.getEmail());
            assertEquals(password, updatedUser.getPassword());
        }

        @Test
        @DisplayName("does not update a password if the email does not exist, but the token does")
        void doesNotUpdateAPasswordIfTheEmailDoesNotExistButTheTokenDoes() {
            UsersRecord originalUser = userRecordPool.createAndSaveOne();
            String resetToken = passwordResetTokenRecordPool.createAndSaveOneForUser(originalUser).getToken();
            String password = UUID.randomUUID().toString();
            userPasswordJooqRepository.updateOneWithEmailAndNonExpiredResetToken(password, UUID.randomUUID().toString(), resetToken);
            UsersRecord updatedUser = userRecordPool.findOneByIdAndEmail(originalUser.getId(), originalUser.getEmail());
            assertEquals(originalUser.getPassword(), updatedUser.getPassword());
        }

        @Test
        @DisplayName("does not update a password if the reset token does not exist, but the email does")
        void doesNotUpdateAPasswordIfTheResetTokenDoesNotExistButTheEmailDoes() {
            UsersRecord originalUser = userRecordPool.createAndSaveOne();
            String password = UUID.randomUUID().toString();
            userPasswordJooqRepository.updateOneWithEmailAndNonExpiredResetToken(password, originalUser.getEmail(), UUID.randomUUID().toString());
            UsersRecord updatedUser = userRecordPool.findOneByIdAndEmail(originalUser.getId(), originalUser.getEmail());
            assertEquals(originalUser.getPassword(), updatedUser.getPassword());
        }

        @Test
        @DisplayName("does not update a password if the reset token and email do not exist")
        void doesNotUpdateAPasswordIfTheResetTokenAndEmailDoNotExist() {
            UsersRecord originalUser = userRecordPool.createAndSaveOne();
            String password = UUID.randomUUID().toString();
            userPasswordJooqRepository.updateOneWithEmailAndNonExpiredResetToken(password, UUID.randomUUID().toString(), UUID.randomUUID().toString());
            UsersRecord updatedUser = userRecordPool.findOneByIdAndEmail(originalUser.getId(), originalUser.getEmail());
            assertEquals(originalUser.getPassword(), updatedUser.getPassword());
        }
    }
}

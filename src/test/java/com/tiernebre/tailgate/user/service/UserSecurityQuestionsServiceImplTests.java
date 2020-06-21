package com.tiernebre.tailgate.user.service;

import com.tiernebre.tailgate.user.exception.InvalidSecurityQuestionAnswerException;
import com.tiernebre.tailgate.user.repository.UserSecurityQuestionsRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserSecurityQuestionsServiceImplTests {
    @InjectMocks
    private UserSecurityQuestionsServiceImpl userSecurityQuestionsService;

    @Mock
    private UserSecurityQuestionsRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Nested
    @DisplayName("validateAnswersForUserWithEmailAndResetToken")
    class ValidateAnswersForUserWithEmailAndResetTokenTests {
        @Test
        @DisplayName("does not throw an invalid error if the provided answers match the found ones")
        void doesNotThrowAnInvalidErrorIfTheProvidedAnswersMatchTheFoundOnes() {
            String resetToken = UUID.randomUUID().toString();
            String email = UUID.randomUUID().toString();
            Map<Long, String> foundAnswers = new HashMap<>();
            Map<Long, String> providedAnswers = new HashMap<>();
            for (long i = 0; i < 2; i++) {
                String foundAnswer = UUID.randomUUID().toString();
                String providedAnswer = UUID.randomUUID().toString();
                foundAnswers.put(i, foundAnswer);
                providedAnswers.put(i, providedAnswer);
                when(passwordEncoder.matches(eq(providedAnswer), eq(foundAnswer))).thenReturn(true);
            }
            when(repository.getAnswersForEmailAndResetToken(eq(email), eq(resetToken))).thenReturn(foundAnswers);
            assertDoesNotThrow(() -> userSecurityQuestionsService.validateAnswersForUserWithEmailAndResetToken(
                    email,
                    resetToken,
                    providedAnswers
            ));
        }

        @Test
        @DisplayName("throws an invalid error if all provided answers do not match the found ones")
        void throwsAnInvalidErrorIfAllProvidedAnswersDoNotMatchTheFoundOnes() {
            String resetToken = UUID.randomUUID().toString();
            String email = UUID.randomUUID().toString();
            Map<Long, String> foundAnswers = new HashMap<>();
            Map<Long, String> providedAnswers = new HashMap<>();
            for (long i = 0; i < 2; i++) {
                String foundAnswer = UUID.randomUUID().toString();
                String providedAnswer = UUID.randomUUID().toString();
                foundAnswers.put(i, foundAnswer);
                providedAnswers.put(i, providedAnswer);
            }
            when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);
            when(repository.getAnswersForEmailAndResetToken(eq(email), eq(resetToken))).thenReturn(foundAnswers);
            assertThrows(InvalidSecurityQuestionAnswerException.class, () ->
                    userSecurityQuestionsService.validateAnswersForUserWithEmailAndResetToken(
                            email,
                            resetToken,
                            providedAnswers
                    )
            );
        }

        @Test
        @DisplayName("throws an invalid error if some provided answers do not match the found ones")
        void throwsAnInvalidErrorIfSomeProvidedAnswersDoNotMatchTheFoundOnes() {
            String resetToken = UUID.randomUUID().toString();
            String email = UUID.randomUUID().toString();
            Map<Long, String> foundAnswers = new HashMap<>();
            Map<Long, String> providedAnswers = new HashMap<>();
            for (long i = 0; i < 2; i++) {
                String foundAnswer = UUID.randomUUID().toString();
                String providedAnswer = UUID.randomUUID().toString();
                foundAnswers.put(i, foundAnswer);
                providedAnswers.put(i, providedAnswer);
                boolean matches = i % 2 == 0;
                when(passwordEncoder.matches(anyString(), anyString())).thenReturn(matches);
            }
            when(repository.getAnswersForEmailAndResetToken(eq(email), eq(resetToken))).thenReturn(foundAnswers);
            assertThrows(InvalidSecurityQuestionAnswerException.class, () ->
                    userSecurityQuestionsService.validateAnswersForUserWithEmailAndResetToken(
                            email,
                            resetToken,
                            providedAnswers
                    )
            );
        }

        @Test
        @DisplayName("ignores extra provided answers that do not match the found answers")
        void ignoresExtraProvidedAnswersThatDoNotMatchTheFoundAnswers() {
            String resetToken = UUID.randomUUID().toString();
            String email = UUID.randomUUID().toString();
            Map<Long, String> foundAnswers = new HashMap<>();
            Map<Long, String> providedAnswers = new HashMap<>();
            for (long i = 0; i < 2; i++) {
                String foundAnswer = UUID.randomUUID().toString();
                String providedAnswer = UUID.randomUUID().toString();
                foundAnswers.put(i, foundAnswer);
                providedAnswers.put(i, providedAnswer);
                when(passwordEncoder.matches(providedAnswer, foundAnswer)).thenReturn(true);
            }
            providedAnswers.put(Integer.toUnsignedLong(providedAnswers.size()), UUID.randomUUID().toString());
            when(repository.getAnswersForEmailAndResetToken(eq(email), eq(resetToken))).thenReturn(foundAnswers);
            assertDoesNotThrow(() ->
                    userSecurityQuestionsService.validateAnswersForUserWithEmailAndResetToken(
                            email,
                            resetToken,
                            providedAnswers
                    )
            );
            verify(passwordEncoder, times(foundAnswers.size())).matches(anyString(), anyString());
        }

        @Test
        @DisplayName("throws an invalid error if a provided answer was missing for an assigned security question")
        void throwsAnInvalidErrorIfAProvidedAnswerWasMissingForAnAssignedSecurityQuestion() {
            String resetToken = UUID.randomUUID().toString();
            String email = UUID.randomUUID().toString();
            Map<Long, String> foundAnswers = new HashMap<>();
            Map<Long, String> providedAnswers = new HashMap<>();
            for (long i = 0; i < 2; i++) {
                String foundAnswer = UUID.randomUUID().toString();
                foundAnswers.put(i, foundAnswer);
                if (i % 2 == 0) {
                    String providedAnswer = UUID.randomUUID().toString();
                    providedAnswers.put(i, providedAnswer);
                    when(passwordEncoder.matches(providedAnswer, foundAnswer)).thenReturn(true);
                }
            }
            when(repository.getAnswersForEmailAndResetToken(eq(email), eq(resetToken))).thenReturn(foundAnswers);
            assertThrows(InvalidSecurityQuestionAnswerException.class, () ->
                    userSecurityQuestionsService.validateAnswersForUserWithEmailAndResetToken(
                            email,
                            resetToken,
                            providedAnswers
                    )
            );
        }

        @ParameterizedTest(name = "throws an invalid error if a provided answer was {0}")
        @EmptySource
        @NullSource
        @ValueSource(strings = " ")
        void throwsAnInvalidErrorIfAProvidedAnswerWas(String providedAnswer) {
            String resetToken = UUID.randomUUID().toString();
            String email = UUID.randomUUID().toString();
            Map<Long, String> foundAnswers = new HashMap<>();
            Map<Long, String> providedAnswers = new HashMap<>();
            for (long i = 0; i < 2; i++) {
                String foundAnswer = UUID.randomUUID().toString();
                foundAnswers.put(i, foundAnswer);
                providedAnswers.put(i, providedAnswer);
            }
            when(repository.getAnswersForEmailAndResetToken(eq(email), eq(resetToken))).thenReturn(foundAnswers);
            assertThrows(InvalidSecurityQuestionAnswerException.class, () ->
                    userSecurityQuestionsService.validateAnswersForUserWithEmailAndResetToken(
                            email,
                            resetToken,
                            providedAnswers
                    )
            );
        }
    }
}

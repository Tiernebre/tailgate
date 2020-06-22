package com.tiernebre.tailgate.user;

import com.tiernebre.tailgate.test.WebControllerIntegrationTestSuite;
import com.tiernebre.tailgate.test.WithMockCustomUser;
import com.tiernebre.tailgate.user.dto.CreateUserRequest;
import com.tiernebre.tailgate.user.dto.UserDto;
import com.tiernebre.tailgate.user.exception.UserNotFoundForConfirmationException;
import com.tiernebre.tailgate.user.service.UserConfirmationService;
import com.tiernebre.tailgate.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserRestfulController.class)
public class UserRestfulControllerIntegrationTests extends WebControllerIntegrationTestSuite {
    @MockBean
    private UserService userService;

    @MockBean
    private UserConfirmationService userConfirmationService;

    @DisplayName("POST /users")
    @Nested
    public class PostUsersTest {
        @Test
        @DisplayName("returns with 201 CREATED status if successful")
        void testThatItReturnsWith201CreatedStatusIfSuccessful() throws Exception {
            CreateUserRequest createUserRequest = UserFactory.generateOneCreateUserRequest();
            when(userService.createOne(eq(createUserRequest))).thenReturn(UserFactory.generateOneDto());
            mockMvc.perform(
                    post("/users")
                            .content(objectMapper.writeValueAsString(createUserRequest))
                            .contentType(MediaType.APPLICATION_JSON)
            )
                    .andExpect(status().isCreated());
        }

        @Test
        @DisplayName("returns with the created user along with links in the response body")
        void testThatItReturnsWithCreatedUserIfSuccessful() throws Exception {
            CreateUserRequest createUserRequest = UserFactory.generateOneCreateUserRequest();
            UserDto expectedUser = UserFactory.generateOneDto();
            when(userService.createOne(eq(createUserRequest))).thenReturn(expectedUser);
            mockMvc.perform(
                    post("/users")
                            .content(objectMapper.writeValueAsString(createUserRequest))
                            .contentType(MediaType.APPLICATION_JSON)
            )
                    .andExpect(jsonPath("$.id").exists())
                    .andExpect(jsonPath("$.id").value(expectedUser.getId()))
                    .andExpect(jsonPath("$.email").exists())
                    .andExpect(jsonPath("$.email").value(expectedUser.getEmail()));
        }
    }

    @Nested
    @DisplayName("PATCH /users/confirmation/{confirmationToken}")
    public class PatchUsersConfirmationTests {
        @Test
        @DisplayName("returns with 204 NO CONTENT status if successful")
        void returnsWith204NoContentStatusIfSuccessful() throws Exception {
            String confirmationToken = UUID.randomUUID().toString();
            doNothing().when(userService).confirmOne(eq(confirmationToken));
            mockMvc.perform(
                    patch("/users/confirmation/" + confirmationToken)
            ).andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("returns with 404 NOT FOUND status if user was not confirmed")
        void returnsWith404NotFoundStatusIfUserWasNotConfirmed() throws Exception {
            String confirmationToken = UUID.randomUUID().toString();
            doThrow(new UserNotFoundForConfirmationException()).when(userService).confirmOne(eq(confirmationToken));
            mockMvc.perform(
                    patch("/users/confirmation/" + confirmationToken)
            ).andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("POST /users/me/confirmation-token")
    public class PostMyConfirmationTokenTests {
        @Test
        @DisplayName("returns with 204 NO CONTENT status if successful")
        @WithMockCustomUser
        void returnsWith204NoContentStatusIfSuccessful() throws Exception {
            doNothing().when(userConfirmationService).sendOne(UserFactory.CUSTOM_AUTHENTICATED_USER);
            mockMvc.perform(
                    post("/users/me/confirmation-token")
            ).andExpect(status().isNoContent());
        }
    }
}

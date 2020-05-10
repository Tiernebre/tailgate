package com.tiernebre.tailgate.user;

import com.tiernebre.tailgate.test.WebControllerIntegrationTestSuite;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserRestfulController.class)
public class UserRestfulControllerIntegrationTests extends WebControllerIntegrationTestSuite {
    @MockBean
    private UserService userService;

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
}

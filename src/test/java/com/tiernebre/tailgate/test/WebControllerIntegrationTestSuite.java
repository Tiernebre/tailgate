package com.tiernebre.tailgate.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tiernebre.tailgate.token.access.AccessTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc()
@MockBeans({
        @MockBean(AccessTokenProvider.class)
})
public abstract class WebControllerIntegrationTestSuite {
    protected final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    protected MockMvc mockMvc;
}

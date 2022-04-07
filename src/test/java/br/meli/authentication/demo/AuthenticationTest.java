package br.meli.authentication.demo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
class AuthenticationTest {

	private static String AUTH_ROUTE = "/api/v1/auth";

	@Autowired
	private MockMvc mockMvc;

	@Test
	void itShouldLoginWithRightCredentials() throws Exception {
		String payload = "{ \"username\": \"wevuser\", \"password\": \"123456\" }";

		mockMvc.perform(post(AUTH_ROUTE)
				.contentType(MediaType.APPLICATION_JSON)
				.content(payload))
				.andExpect(status().isOk())
				.andExpect(jsonPath("acess_token").exists());
	}

	@Test
	void itShouldNotLoginWithWrongCredentials() throws Exception {
		String payload = "{ \"username\": \"wevuser\", \"password\": \"123457\" }";

		mockMvc.perform(post(AUTH_ROUTE)
						.contentType(MediaType.APPLICATION_JSON)
						.content(payload))
				.andExpect(status().isUnauthorized());
	}
}

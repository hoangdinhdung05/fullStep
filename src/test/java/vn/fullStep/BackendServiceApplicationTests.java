package vn.fullStep;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import vn.fullStep.controller.AuthenticationController;
import vn.fullStep.controller.EmailController;
import vn.fullStep.controller.UserController;

@SpringBootTest
class BackendServiceApplicationTests {

	@InjectMocks
	private AuthenticationController authenticationController;

	@InjectMocks
	private UserController userController;

	@InjectMocks
	private EmailController emailController;

	@Test
	void contextLoads() {
		//check if the controllers are loaded correctly
		Assertions.assertNotNull(authenticationController);
		Assertions.assertNotNull(userController);
		Assertions.assertNotNull(emailController);
	}

}

package org.cateproject.web.auth;

import static org.junit.Assert.assertEquals;

import org.cateproject.web.auth.LoginController;
import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

public class LoginControllerTest {
	
	private LoginController loginController;

	@Before
	public void setUp() throws Exception {
		loginController = new LoginController();
	}

	@Test
	public void testCreateForm() {
		Model uiModel = new ExtendedModelMap();
		assertEquals("createForm should return 'login'","login", loginController.createForm(uiModel));
	}

}

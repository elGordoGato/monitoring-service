package org.ylab.servlets;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.ylab.entity.User;
import org.ylab.user.UserService;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Tests for Login Servlet functionality")
@ExtendWith(MockitoExtension.class)
class LoginServletTest {
    @Mock
    private UserService userService;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private HttpSession session;
    @InjectMocks
    private LoginServlet servlet;
    private User user;
    private final String email = "test@example.com";
    private final String password = "secret";

    @BeforeEach
    void setUp() {
        user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setFirstName("Test User");
        user.setLastName("Kamenev");
    }

    @Test
    public void testDoPostWithValidCredentials() {
        when(request.getParameter("email")).thenReturn(email);
        when(request.getParameter("password")).thenReturn(password);
        when(request.getSession()).thenReturn(session);
        when(userService.authenticate(email, password)).thenReturn(user);

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);

        verify(session).setAttribute("user", user);
    }
}
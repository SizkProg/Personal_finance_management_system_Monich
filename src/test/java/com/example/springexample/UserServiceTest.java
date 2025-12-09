package com.example.springexample;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.example.springexample.Exceptions.PasswordIsNotCorrect;
import com.example.springexample.Exceptions.UserNotFoundException;
import com.example.springexample.model.User;
import com.example.springexample.service.UserService;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {
    
    private UserService userService;
    
    @BeforeEach
    public void setUp() {
        userService = new UserService();
    }
    
    @Test
    public void testRegisterUser() {
        // Проверка успешной регистрации
        assertDoesNotThrow(() -> userService.registerUser("testuser", "password123"));
        
        // Проверка, что пользователь действительно зарегистрирован
        assertThrows(PasswordIsNotCorrect.class, () -> {
            userService.authenticate("testuser", "wrongpassword");
        });
    }
    
    @Test
    public void testRegisterDuplicateUser() {
        // Регистрируем пользователя
        assertDoesNotThrow(() -> userService.registerUser("testuser", "password123"));
        
        // Пытаемся зарегистрировать того же пользователя еще раз
        assertThrows(IllegalArgumentException.class, () -> {
            userService.registerUser("testuser", "password456");
        });
    }
    
    @Test
    public void testAuthenticateUser() throws UserNotFoundException, PasswordIsNotCorrect {
        // Регистрируем пользователя
        userService.registerUser("testuser", "password123");
        
        // Проверка успешной аутентификации
        User user = userService.authenticate("testuser", "password123");
        assertNotNull(user);
        assertEquals("testuser", user.getUsername());
    }
    
    @Test
    public void testAuthenticateNonExistentUser() {
        assertThrows(UserNotFoundException.class, () -> {
            userService.authenticate("nonexistent", "password123");
        });
    }
    
    @Test
    public void testAuthenticateWithWrongPassword() {
        // Регистрируем пользователя
        userService.registerUser("testuser", "password123");
        
        // Пытаемся аутентифицироваться с неправильным паролем
        assertThrows(PasswordIsNotCorrect.class, () -> {
            userService.authenticate("testuser", "wrongpassword");
        });
    }
    
    @Test
    public void testGetCurrentUser() throws UserNotFoundException, PasswordIsNotCorrect {
        // Регистрируем и аутентифицируем пользователя
        userService.registerUser("testuser", "password123");
        User user = userService.authenticate("testuser", "password123");
        
        // Проверяем, что текущий пользователь установлен правильно
        User currentUser = userService.getCurrentUser();
        assertNotNull(currentUser);
        assertEquals(user, currentUser);
    }
    
    @Test
    public void testLogout() throws UserNotFoundException, PasswordIsNotCorrect {
        // Регистрируем и аутентифицируем пользователя
        userService.registerUser("testuser", "password123");
        userService.authenticate("testuser", "password123");
        
        // Проверяем, что пользователь залогинен
        assertTrue(userService.isLoggedIn());
        
        // Выполняем выход
        userService.logout();
        
        // Проверяем, что пользователь больше не залогинен
        assertFalse(userService.isLoggedIn());
        assertNull(userService.getCurrentUser());
    }
}
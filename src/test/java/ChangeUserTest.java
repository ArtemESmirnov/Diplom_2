import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pojos.*;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;
import static requestgenerators.ChangeUserRequestGenerator.changeUserRequest;
import static requestgenerators.CreateUserRequestGenerator.createUserRequest;
import static requestgenerators.DeleteUserRequestGenerator.deleteUserRequest;
import static requestgenerators.LoginUserRequestGenerator.loginUserRequest;

public class ChangeUserTest {
    final static String AUTH_REGISTER_API_PATH = "/api/auth/register";
    final static String AUTH_USER_API_PATH = "/api/auth/user";
    final static String AUTH_LOGIN_API_PATH = "/api/auth/login";
    final static String CHANGE_WITHOUT_LOGIN_MESSAGE = "You should be authorised";
    final static String EXISTING_EMAIL_MESSAGE = "User with such email already exists";
    final String name = "uniqueName";
    final String email = "uniqueEmail@yandex.ru";
    final String password = "uniquePassword";
    final String newName = "newName";
    final String newEmail = "newEmail@ya.ru";
    final String newPassword = "newPassword";
    Response response;
    String token;

    @Before
    public void createUser(){
        WholeUserBody userBody = new WholeUserBody(email, password, name);

        createUserRequest(userBody, AUTH_REGISTER_API_PATH).statusCode();
    }

    @Test
    public void changeAllUserInfoWithLoginShouldBePossibleStatusCode(){
        EmailPasswordUserBody emailPasswordUserBody = new EmailPasswordUserBody(email, password);
        Response changeResponse;
        WholeUserBody newUserBody = new WholeUserBody(newEmail, newPassword, newName);

        response = loginUserRequest(emailPasswordUserBody, AUTH_LOGIN_API_PATH);
        token = response.path("accessToken")
                .toString().replaceAll("Bearer ", "");
        changeResponse = changeUserRequest(newUserBody, AUTH_USER_API_PATH, token);
        assertEquals(SC_OK, changeResponse.statusCode());
    }

    @Test
    public void changeAllUserInfoWithLoginShouldBePossibleBody(){
        EmailPasswordUserBody emailPasswordUserBody = new EmailPasswordUserBody(email, password);
        Response changeResponse;
        WholeUserBody newUserBody = new WholeUserBody(newEmail, newPassword, newName);

        response = loginUserRequest(emailPasswordUserBody, AUTH_LOGIN_API_PATH);
        token = response.path("accessToken")
                .toString().replaceAll("Bearer ", "");
        changeResponse = changeUserRequest(newUserBody, AUTH_USER_API_PATH, token);
        assertTrue(changeResponse.path("success"));
        assertEquals(newEmail.toLowerCase(), changeResponse.path("user.email"));
        assertEquals(newName, changeResponse.path("user.name"));
    }

    @Test
    public void changeEmailWithLoginShouldBePossibleStatusCode(){
        EmailPasswordUserBody emailPasswordUserBody = new EmailPasswordUserBody(email, password);
        Response changeResponse;
        ChangeUserEmailBody newUserBody = new ChangeUserEmailBody(newEmail);

        response = loginUserRequest(emailPasswordUserBody, AUTH_LOGIN_API_PATH);
        token = response.path("accessToken")
                .toString().replaceAll("Bearer ", "");
        changeResponse = changeUserRequest(newUserBody, AUTH_USER_API_PATH, token);
        assertEquals(SC_OK, changeResponse.statusCode());
    }

    @Test
    public void changeEmailWithLoginShouldBePossibleBody(){
        EmailPasswordUserBody emailPasswordUserBody = new EmailPasswordUserBody(email, password);
        Response changeResponse;
        ChangeUserEmailBody newUserBody = new ChangeUserEmailBody(newEmail);

        response = loginUserRequest(emailPasswordUserBody, AUTH_LOGIN_API_PATH);
        token = response.path("accessToken")
                .toString().replaceAll("Bearer ", "");
        changeResponse = changeUserRequest(newUserBody, AUTH_USER_API_PATH, token);
        assertTrue(changeResponse.path("success"));
        assertEquals(newEmail.toLowerCase(), changeResponse.path("user.email"));
        assertEquals(name, changeResponse.path("user.name"));
    }

    @Test
    public void changeNameWithLoginShouldBePossibleStatusCode(){
        EmailPasswordUserBody emailPasswordUserBody = new EmailPasswordUserBody(email, password);
        Response changeResponse;
        ChangeUserNameBody newUserBody = new ChangeUserNameBody(newName);

        response = loginUserRequest(emailPasswordUserBody, AUTH_LOGIN_API_PATH);
        token = response.path("accessToken")
                .toString().replaceAll("Bearer ", "");
        changeResponse = changeUserRequest(newUserBody, AUTH_USER_API_PATH, token);
        assertEquals(SC_OK, changeResponse.statusCode());
    }

    @Test
    public void changeNameWithLoginShouldBePossibleBody(){
        EmailPasswordUserBody emailPasswordUserBody = new EmailPasswordUserBody(email, password);
        Response changeResponse;
        ChangeUserNameBody newUserBody = new ChangeUserNameBody(newName);

        response = loginUserRequest(emailPasswordUserBody, AUTH_LOGIN_API_PATH);
        token = response.path("accessToken")
                .toString().replaceAll("Bearer ", "");
        changeResponse = changeUserRequest(newUserBody, AUTH_USER_API_PATH, token);
        assertTrue(changeResponse.path("success"));
        assertEquals(email.toLowerCase(), changeResponse.path("user.email"));
        assertEquals(newName, changeResponse.path("user.name"));
    }

    @Test
    public void changePasswordWithLoginShouldBePossibleStatusCode(){
        EmailPasswordUserBody emailPasswordUserBody = new EmailPasswordUserBody(email, password);
        Response changeResponse;
        ChangeUserPasswordBody newUserBody = new ChangeUserPasswordBody(newPassword);

        response = loginUserRequest(emailPasswordUserBody, AUTH_LOGIN_API_PATH);
        token = response.path("accessToken")
                .toString().replaceAll("Bearer ", "");
        changeResponse = changeUserRequest(newUserBody, AUTH_USER_API_PATH, token);
        assertEquals(SC_OK, changeResponse.statusCode());
    }

    @Test
    public void changePasswordWithLoginShouldBePossibleBody(){
        EmailPasswordUserBody emailPasswordUserBody = new EmailPasswordUserBody(email, password);
        Response changeResponse;
        ChangeUserPasswordBody newUserBody = new ChangeUserPasswordBody(newPassword);

        response = loginUserRequest(emailPasswordUserBody, AUTH_LOGIN_API_PATH);
        token = response.path("accessToken")
                .toString().replaceAll("Bearer ", "");
        changeResponse = changeUserRequest(newUserBody, AUTH_USER_API_PATH, token);
        assertTrue(changeResponse.path("success"));
        assertEquals(email.toLowerCase(), changeResponse.path("user.email"));
        assertEquals(name, changeResponse.path("user.name"));
    }

    @Test
    public void changeAllUserInfoWithoutLoginShouldFailStatusCode(){
        EmailPasswordUserBody emailPasswordUserBody = new EmailPasswordUserBody(email, password);
        Response changeResponse;
        WholeUserBody newUserBody = new WholeUserBody(newEmail, newPassword, newName);

        changeResponse = changeUserRequest(newUserBody, AUTH_USER_API_PATH, "");
        assertEquals(SC_UNAUTHORIZED, changeResponse.statusCode());

        response = loginUserRequest(emailPasswordUserBody, AUTH_LOGIN_API_PATH);
        token = response.path("accessToken")
                .toString().replaceAll("Bearer ", "");
    }

    @Test
    public void changeAllUserInfoWithoutLoginShouldFailBody(){
        EmailPasswordUserBody emailPasswordUserBody = new EmailPasswordUserBody(email, password);
        Response changeResponse;
        WholeUserBody newUserBody = new WholeUserBody(newEmail, newPassword, newName);

        changeResponse = changeUserRequest(newUserBody, AUTH_USER_API_PATH, "");
        assertFalse(changeResponse.path("success"));
        assertEquals(CHANGE_WITHOUT_LOGIN_MESSAGE, changeResponse.path("message"));

        response = loginUserRequest(emailPasswordUserBody, AUTH_LOGIN_API_PATH);
        token = response.path("accessToken")
                .toString().replaceAll("Bearer ", "");
    }

    @Test
    public void changeEmailWithoutLoginShouldFailStatusCode(){
        EmailPasswordUserBody emailPasswordUserBody = new EmailPasswordUserBody(email, password);
        Response changeResponse;
        ChangeUserEmailBody newUserBody = new ChangeUserEmailBody(newEmail);

        changeResponse = changeUserRequest(newUserBody, AUTH_USER_API_PATH, "");
        assertEquals(SC_UNAUTHORIZED, changeResponse.statusCode());

        response = loginUserRequest(emailPasswordUserBody, AUTH_LOGIN_API_PATH);
        token = response.path("accessToken")
                .toString().replaceAll("Bearer ", "");
    }

    @Test
    public void changeEmailWithoutLoginShouldFailBody(){
        EmailPasswordUserBody emailPasswordUserBody = new EmailPasswordUserBody(email, password);
        Response changeResponse;
        ChangeUserEmailBody newUserBody = new ChangeUserEmailBody(newEmail);

        changeResponse = changeUserRequest(newUserBody, AUTH_USER_API_PATH, "");
        assertFalse(changeResponse.path("success"));
        assertEquals(CHANGE_WITHOUT_LOGIN_MESSAGE, changeResponse.path("message"));

        response = loginUserRequest(emailPasswordUserBody, AUTH_LOGIN_API_PATH);
        token = response.path("accessToken")
                .toString().replaceAll("Bearer ", "");
    }

    @Test
    public void changeNameWithoutLoginShouldFailStatusCode(){
        EmailPasswordUserBody emailPasswordUserBody = new EmailPasswordUserBody(email, password);
        Response changeResponse;
        ChangeUserNameBody newUserBody = new ChangeUserNameBody(newName);

        changeResponse = changeUserRequest(newUserBody, AUTH_USER_API_PATH, "");
        assertEquals(SC_UNAUTHORIZED, changeResponse.statusCode());

        response = loginUserRequest(emailPasswordUserBody, AUTH_LOGIN_API_PATH);
        token = response.path("accessToken")
                .toString().replaceAll("Bearer ", "");
    }

    @Test
    public void changeNameWithoutLoginShouldFailBody(){
        EmailPasswordUserBody emailPasswordUserBody = new EmailPasswordUserBody(email, password);
        Response changeResponse;
        ChangeUserNameBody newUserBody = new ChangeUserNameBody(newName);

        changeResponse = changeUserRequest(newUserBody, AUTH_USER_API_PATH, "");
        assertFalse(changeResponse.path("success"));
        assertEquals(CHANGE_WITHOUT_LOGIN_MESSAGE, changeResponse.path("message"));

        response = loginUserRequest(emailPasswordUserBody, AUTH_LOGIN_API_PATH);
        token = response.path("accessToken")
                .toString().replaceAll("Bearer ", "");
    }

    @Test
    public void changePasswordWithoutLoginShouldFailStatusCode(){
        EmailPasswordUserBody emailPasswordUserBody = new EmailPasswordUserBody(email, password);
        Response changeResponse;
        ChangeUserPasswordBody newUserBody = new ChangeUserPasswordBody(newPassword);

        changeResponse = changeUserRequest(newUserBody, AUTH_USER_API_PATH, "");
        assertEquals(SC_UNAUTHORIZED, changeResponse.statusCode());

        response = loginUserRequest(emailPasswordUserBody, AUTH_LOGIN_API_PATH);
        token = response.path("accessToken")
                .toString().replaceAll("Bearer ", "");
    }

    @Test
    public void changePasswordWithoutLoginShouldFailBody(){
        EmailPasswordUserBody emailPasswordUserBody = new EmailPasswordUserBody(email, password);
        Response changeResponse;
        ChangeUserPasswordBody newUserBody = new ChangeUserPasswordBody(newPassword);

        changeResponse = changeUserRequest(newUserBody, AUTH_USER_API_PATH, "");
        assertFalse(changeResponse.path("success"));
        assertEquals(CHANGE_WITHOUT_LOGIN_MESSAGE, changeResponse.path("message"));

        response = loginUserRequest(emailPasswordUserBody, AUTH_LOGIN_API_PATH);
        token = response.path("accessToken")
                .toString().replaceAll("Bearer ", "");
    }

    @Test
    public void changeAllUserInfoWithExistingEmailShouldFailStatusCode(){
        WholeUserBody secondUser = new WholeUserBody(newEmail, password, name);
        createUserRequest(secondUser, AUTH_REGISTER_API_PATH);
        EmailPasswordUserBody emailPasswordUserBody = new EmailPasswordUserBody(email, password);
        Response changeResponse;
        WholeUserBody newUserBody = new WholeUserBody(newEmail, newPassword, newName);

        response = loginUserRequest(emailPasswordUserBody, AUTH_LOGIN_API_PATH);
        token = response.path("accessToken")
                .toString().replaceAll("Bearer ", "");

        changeResponse = changeUserRequest(newUserBody, AUTH_USER_API_PATH, token);
        assertEquals(SC_FORBIDDEN, changeResponse.statusCode());

        EmailPasswordUserBody secondUserLoginBody = new EmailPasswordUserBody(newEmail, password);
        deleteUserRequest(loginUserRequest(secondUserLoginBody, AUTH_LOGIN_API_PATH)
                .path("accessToken").toString().replaceAll("Bearer ", ""), AUTH_USER_API_PATH);
    }

    @Test
    public void changeAllUserInfoWithExistingEmailShouldFailBody(){
        WholeUserBody secondUser = new WholeUserBody(newEmail, password, name);
        createUserRequest(secondUser, AUTH_REGISTER_API_PATH);
        EmailPasswordUserBody emailPasswordUserBody = new EmailPasswordUserBody(email, password);
        Response changeResponse;
        WholeUserBody newUserBody = new WholeUserBody(newEmail, newPassword, newName);

        response = loginUserRequest(emailPasswordUserBody, AUTH_LOGIN_API_PATH);
        token = response.path("accessToken")
                .toString().replaceAll("Bearer ", "");

        changeResponse = changeUserRequest(newUserBody, AUTH_USER_API_PATH, token);
        assertEquals(EXISTING_EMAIL_MESSAGE, changeResponse.path("message"));

        EmailPasswordUserBody secondUserLoginBody = new EmailPasswordUserBody(newEmail, password);
        deleteUserRequest(loginUserRequest(secondUserLoginBody, AUTH_LOGIN_API_PATH)
                .path("accessToken").toString().replaceAll("Bearer ", ""), AUTH_USER_API_PATH);
    }

    @Test
    public void changeUserEmailWithExistingEmailShouldFailStatusCode(){
        WholeUserBody secondUser = new WholeUserBody(newEmail, password, name);
        createUserRequest(secondUser, AUTH_REGISTER_API_PATH);
        EmailPasswordUserBody emailPasswordUserBody = new EmailPasswordUserBody(email, password);
        Response changeResponse;
        ChangeUserEmailBody newUserBody = new ChangeUserEmailBody(newEmail);

        response = loginUserRequest(emailPasswordUserBody, AUTH_LOGIN_API_PATH);
        token = response.path("accessToken")
                .toString().replaceAll("Bearer ", "");

        changeResponse = changeUserRequest(newUserBody, AUTH_USER_API_PATH, token);
        assertEquals(SC_FORBIDDEN, changeResponse.statusCode());

        EmailPasswordUserBody secondUserLoginBody = new EmailPasswordUserBody(newEmail, password);
        deleteUserRequest(loginUserRequest(secondUserLoginBody, AUTH_LOGIN_API_PATH)
                .path("accessToken").toString().replaceAll("Bearer ", ""), AUTH_USER_API_PATH);
    }

    @Test
    public void changeUserEmailWithExistingEmailShouldFailBody(){
        WholeUserBody secondUser = new WholeUserBody(newEmail, password, name);
        createUserRequest(secondUser, AUTH_REGISTER_API_PATH);
        EmailPasswordUserBody emailPasswordUserBody = new EmailPasswordUserBody(email, password);
        Response changeResponse;
        ChangeUserEmailBody newUserBody = new ChangeUserEmailBody(newEmail);

        response = loginUserRequest(emailPasswordUserBody, AUTH_LOGIN_API_PATH);
        token = response.path("accessToken")
                .toString().replaceAll("Bearer ", "");

        changeResponse = changeUserRequest(newUserBody, AUTH_USER_API_PATH, token);
        assertEquals(EXISTING_EMAIL_MESSAGE, changeResponse.path("message"));

        EmailPasswordUserBody secondUserLoginBody = new EmailPasswordUserBody(newEmail, password);
        deleteUserRequest(loginUserRequest(secondUserLoginBody, AUTH_LOGIN_API_PATH)
                .path("accessToken").toString().replaceAll("Bearer ", ""), AUTH_USER_API_PATH);
    }

    @After
    public void deleteUser(){
        assertEquals(SC_ACCEPTED, deleteUserRequest(token, AUTH_USER_API_PATH).statusCode());
    }
}

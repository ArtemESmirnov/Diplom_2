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
    private final static String AUTH_REGISTER_API_PATH = "/api/auth/register";
    private final static String AUTH_USER_API_PATH = "/api/auth/user";
    private final static String AUTH_LOGIN_API_PATH = "/api/auth/login";
    private final static String CHANGE_WITHOUT_LOGIN_MESSAGE = "You should be authorised";
    private final static String EXISTING_EMAIL_MESSAGE = "User with such email already exists";
    private final String NAME = "uniqueName";
    private final String EMAIL = "uniqueEmail@yandex.ru";
    private final String PASSWORD = "uniquePassword";
    private final String NEW_NAME = "newName";
    private final String NEW_EMAIL = "newEmail@ya.ru";
    private final String NEW_PASSWORD = "newPassword";
    private String token;

    private String loginGetToken(){
        EmailPasswordUserBody emailPasswordUserBody = new EmailPasswordUserBody(EMAIL, PASSWORD);

        return loginUserRequest(emailPasswordUserBody, AUTH_LOGIN_API_PATH)
                .path("accessToken").toString();
    }

    @Before
    public void createUser(){
        WholeUserBody userBody = new WholeUserBody(EMAIL, PASSWORD, NAME);

        createUserRequest(userBody, AUTH_REGISTER_API_PATH).statusCode();
    }

    @Test
    public void changeAllUserInfoWithLoginShouldBePossibleStatusCode(){
        Response changeResponse;
        WholeUserBody newUserBody = new WholeUserBody(NEW_EMAIL, NEW_PASSWORD, NEW_NAME);

        token = loginGetToken();
        changeResponse = changeUserRequest(newUserBody, AUTH_USER_API_PATH, token);
        assertEquals(SC_OK, changeResponse.statusCode());
    }

    @Test
    public void changeAllUserInfoWithLoginShouldBePossibleBody(){
        Response changeResponse;
        WholeUserBody newUserBody = new WholeUserBody(NEW_EMAIL, NEW_PASSWORD, NEW_NAME);

        token = loginGetToken();
        changeResponse = changeUserRequest(newUserBody, AUTH_USER_API_PATH, token);
        assertTrue(changeResponse.path("success"));
        assertEquals(NEW_EMAIL.toLowerCase(), changeResponse.path("user.email"));
        assertEquals(NEW_NAME, changeResponse.path("user.name"));
    }

    @Test
    public void changeEmailWithLoginShouldBePossibleStatusCode(){
        Response changeResponse;
        ChangeUserEmailBody newUserBody = new ChangeUserEmailBody(NEW_EMAIL);

        token = loginGetToken();
        changeResponse = changeUserRequest(newUserBody, AUTH_USER_API_PATH, token);
        assertEquals(SC_OK, changeResponse.statusCode());
    }

    @Test
    public void changeEmailWithLoginShouldBePossibleBody(){
        Response changeResponse;
        ChangeUserEmailBody newUserBody = new ChangeUserEmailBody(NEW_EMAIL);

        token = loginGetToken();
        changeResponse = changeUserRequest(newUserBody, AUTH_USER_API_PATH, token);
        assertTrue(changeResponse.path("success"));
        assertEquals(NEW_EMAIL.toLowerCase(), changeResponse.path("user.email"));
        assertEquals(NAME, changeResponse.path("user.name"));
    }

    @Test
    public void changeNameWithLoginShouldBePossibleStatusCode(){
        Response changeResponse;
        ChangeUserNameBody newUserBody = new ChangeUserNameBody(NEW_NAME);

        token = loginGetToken();
        changeResponse = changeUserRequest(newUserBody, AUTH_USER_API_PATH, token);
        assertEquals(SC_OK, changeResponse.statusCode());
    }

    @Test
    public void changeNameWithLoginShouldBePossibleBody(){
        Response changeResponse;
        ChangeUserNameBody newUserBody = new ChangeUserNameBody(NEW_NAME);

        token = loginGetToken();
        changeResponse = changeUserRequest(newUserBody, AUTH_USER_API_PATH, token);
        assertTrue(changeResponse.path("success"));
        assertEquals(EMAIL.toLowerCase(), changeResponse.path("user.email"));
        assertEquals(NEW_NAME, changeResponse.path("user.name"));
    }

    @Test
    public void changePasswordWithLoginShouldBePossibleStatusCode(){
        Response changeResponse;
        ChangeUserPasswordBody newUserBody = new ChangeUserPasswordBody(NEW_PASSWORD);

        token = loginGetToken();
        changeResponse = changeUserRequest(newUserBody, AUTH_USER_API_PATH, token);
        assertEquals(SC_OK, changeResponse.statusCode());
    }

    @Test
    public void changePasswordWithLoginShouldBePossibleBody(){
        Response changeResponse;
        ChangeUserPasswordBody newUserBody = new ChangeUserPasswordBody(NEW_PASSWORD);

        token = loginGetToken();
        changeResponse = changeUserRequest(newUserBody, AUTH_USER_API_PATH, token);
        assertTrue(changeResponse.path("success"));
        assertEquals(EMAIL.toLowerCase(), changeResponse.path("user.email"));
        assertEquals(NAME, changeResponse.path("user.name"));
    }

    @Test
    public void changeAllUserInfoWithoutLoginShouldFailStatusCode(){
        Response changeResponse;
        WholeUserBody newUserBody = new WholeUserBody(NEW_EMAIL, NEW_PASSWORD, NEW_NAME);

        changeResponse = changeUserRequest(newUserBody, AUTH_USER_API_PATH, "");
        assertEquals(SC_UNAUTHORIZED, changeResponse.statusCode());

        token = loginGetToken();
    }

    @Test
    public void changeAllUserInfoWithoutLoginShouldFailBody(){
        Response changeResponse;
        WholeUserBody newUserBody = new WholeUserBody(NEW_EMAIL, NEW_PASSWORD, NEW_NAME);

        changeResponse = changeUserRequest(newUserBody, AUTH_USER_API_PATH, "");
        assertFalse(changeResponse.path("success"));
        assertEquals(CHANGE_WITHOUT_LOGIN_MESSAGE, changeResponse.path("message"));

        token = loginGetToken();
    }

    @Test
    public void changeEmailWithoutLoginShouldFailStatusCode(){
        Response changeResponse;
        ChangeUserEmailBody newUserBody = new ChangeUserEmailBody(NEW_EMAIL);

        changeResponse = changeUserRequest(newUserBody, AUTH_USER_API_PATH, "");
        assertEquals(SC_UNAUTHORIZED, changeResponse.statusCode());

        token = loginGetToken();
    }

    @Test
    public void changeEmailWithoutLoginShouldFailBody(){
        Response changeResponse;
        ChangeUserEmailBody newUserBody = new ChangeUserEmailBody(NEW_EMAIL);

        changeResponse = changeUserRequest(newUserBody, AUTH_USER_API_PATH, "");
        assertFalse(changeResponse.path("success"));
        assertEquals(CHANGE_WITHOUT_LOGIN_MESSAGE, changeResponse.path("message"));

        token = loginGetToken();
    }

    @Test
    public void changeNameWithoutLoginShouldFailStatusCode(){
        Response changeResponse;
        ChangeUserNameBody newUserBody = new ChangeUserNameBody(NEW_NAME);

        changeResponse = changeUserRequest(newUserBody, AUTH_USER_API_PATH, "");
        assertEquals(SC_UNAUTHORIZED, changeResponse.statusCode());

        token = loginGetToken();
    }

    @Test
    public void changeNameWithoutLoginShouldFailBody(){
        Response changeResponse;
        ChangeUserNameBody newUserBody = new ChangeUserNameBody(NEW_NAME);

        changeResponse = changeUserRequest(newUserBody, AUTH_USER_API_PATH, "");
        assertFalse(changeResponse.path("success"));
        assertEquals(CHANGE_WITHOUT_LOGIN_MESSAGE, changeResponse.path("message"));

        token = loginGetToken();
    }

    @Test
    public void changePasswordWithoutLoginShouldFailStatusCode(){
        Response changeResponse;
        ChangeUserPasswordBody newUserBody = new ChangeUserPasswordBody(NEW_PASSWORD);

        changeResponse = changeUserRequest(newUserBody, AUTH_USER_API_PATH, "");
        assertEquals(SC_UNAUTHORIZED, changeResponse.statusCode());

        token = loginGetToken();
    }

    @Test
    public void changePasswordWithoutLoginShouldFailBody(){
        Response changeResponse;
        ChangeUserPasswordBody newUserBody = new ChangeUserPasswordBody(NEW_PASSWORD);

        changeResponse = changeUserRequest(newUserBody, AUTH_USER_API_PATH, "");
        assertFalse(changeResponse.path("success"));
        assertEquals(CHANGE_WITHOUT_LOGIN_MESSAGE, changeResponse.path("message"));

        token = loginGetToken();
    }

    @Test
    public void changeAllUserInfoWithExistingEmailShouldFailStatusCode(){
        WholeUserBody secondUser = new WholeUserBody(NEW_EMAIL, PASSWORD, NAME);
        createUserRequest(secondUser, AUTH_REGISTER_API_PATH);
        Response changeResponse;
        WholeUserBody newUserBody = new WholeUserBody(NEW_EMAIL, NEW_PASSWORD, NEW_NAME);

        token = loginGetToken();

        changeResponse = changeUserRequest(newUserBody, AUTH_USER_API_PATH, token);
        assertEquals(SC_FORBIDDEN, changeResponse.statusCode());

        EmailPasswordUserBody secondUserLoginBody = new EmailPasswordUserBody(NEW_EMAIL, PASSWORD);
        deleteUserRequest(loginUserRequest(secondUserLoginBody, AUTH_LOGIN_API_PATH)
                .path("accessToken").toString(), AUTH_USER_API_PATH);
    }

    @Test
    public void changeAllUserInfoWithExistingEmailShouldFailBody(){
        WholeUserBody secondUser = new WholeUserBody(NEW_EMAIL, PASSWORD, NAME);
        createUserRequest(secondUser, AUTH_REGISTER_API_PATH);
        Response changeResponse;
        WholeUserBody newUserBody = new WholeUserBody(NEW_EMAIL, NEW_PASSWORD, NEW_NAME);

        token = loginGetToken();

        changeResponse = changeUserRequest(newUserBody, AUTH_USER_API_PATH, token);
        assertEquals(EXISTING_EMAIL_MESSAGE, changeResponse.path("message"));

        EmailPasswordUserBody secondUserLoginBody = new EmailPasswordUserBody(NEW_EMAIL, PASSWORD);
        deleteUserRequest(loginUserRequest(secondUserLoginBody, AUTH_LOGIN_API_PATH)
                .path("accessToken").toString(), AUTH_USER_API_PATH);
    }

    @Test
    public void changeUserEmailWithExistingEmailShouldFailStatusCode(){
        WholeUserBody secondUser = new WholeUserBody(NEW_EMAIL, PASSWORD, NAME);
        createUserRequest(secondUser, AUTH_REGISTER_API_PATH);
        Response changeResponse;
        ChangeUserEmailBody newUserBody = new ChangeUserEmailBody(NEW_EMAIL);

        token = loginGetToken();

        changeResponse = changeUserRequest(newUserBody, AUTH_USER_API_PATH, token);
        assertEquals(SC_FORBIDDEN, changeResponse.statusCode());

        EmailPasswordUserBody secondUserLoginBody = new EmailPasswordUserBody(NEW_EMAIL, PASSWORD);
        deleteUserRequest(loginUserRequest(secondUserLoginBody, AUTH_LOGIN_API_PATH)
                .path("accessToken").toString(), AUTH_USER_API_PATH);
    }

    @Test
    public void changeUserEmailWithExistingEmailShouldFailBody(){
        WholeUserBody secondUser = new WholeUserBody(NEW_EMAIL, PASSWORD, NAME);
        createUserRequest(secondUser, AUTH_REGISTER_API_PATH);
        Response changeResponse;
        ChangeUserEmailBody newUserBody = new ChangeUserEmailBody(NEW_EMAIL);

        token = loginGetToken();

        changeResponse = changeUserRequest(newUserBody, AUTH_USER_API_PATH, token);
        assertEquals(EXISTING_EMAIL_MESSAGE, changeResponse.path("message"));

        EmailPasswordUserBody secondUserLoginBody = new EmailPasswordUserBody(NEW_EMAIL, PASSWORD);
        deleteUserRequest(loginUserRequest(secondUserLoginBody, AUTH_LOGIN_API_PATH)
                .path("accessToken").toString(), AUTH_USER_API_PATH);
    }

    @After
    public void deleteUser(){
        assertEquals(SC_ACCEPTED, deleteUserRequest(token, AUTH_USER_API_PATH).statusCode());
    }
}

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
    final static String authRegisterApiPath = "/api/auth/register";
    final static String authUserApiPath = "/api/auth/user";
    final static String authLoginApiPath = "/api/auth/login";
    final String CHANGE_WITHOUT_LOGIN_MESSAGE = "You should be authorised";
    final String EXISTING_EMAIL_MESSAGE = "User with such email already exists";
    String name = "uniqueName";
    String email = "uniqueEmail@yandex.ru";
    String password = "uniquePassword";
    String newName = "newName";
    String newEmail = "newEmail@ya.ru";
    String newPassword = "newPassword";
    Response response;
    String token;

    @Before
    public void createUser(){
        WholeUserBody userBody = new WholeUserBody(email, password, name);

        createUserRequest(userBody, authRegisterApiPath).statusCode();
    }

    @Test
    public void changeAllUserInfoWithLoginShouldBePossibleStatusCode(){
        EmailPasswordUserBody emailPasswordUserBody = new EmailPasswordUserBody(email, password);
        Response changeResponse;
        WholeUserBody newUserBody = new WholeUserBody(newEmail, newPassword, newName);

        response = loginUserRequest(emailPasswordUserBody, authLoginApiPath);
        token = response.path("accessToken")
                .toString().replaceAll("Bearer ", "");
        changeResponse = changeUserRequest(newUserBody, authUserApiPath, token);
        assertEquals(SC_OK, changeResponse.statusCode());
    }

    @Test
    public void changeAllUserInfoWithLoginShouldBePossibleBody(){
        EmailPasswordUserBody emailPasswordUserBody = new EmailPasswordUserBody(email, password);
        Response changeResponse;
        WholeUserBody newUserBody = new WholeUserBody(newEmail, newPassword, newName);

        response = loginUserRequest(emailPasswordUserBody, authLoginApiPath);
        token = response.path("accessToken")
                .toString().replaceAll("Bearer ", "");
        changeResponse = changeUserRequest(newUserBody, authUserApiPath, token);
        assertTrue(changeResponse.path("success"));
        assertEquals(newEmail.toLowerCase(), changeResponse.path("user.email"));
        assertEquals(newName, changeResponse.path("user.name"));
    }

    @Test
    public void changeEmailWithLoginShouldBePossibleStatusCode(){
        EmailPasswordUserBody emailPasswordUserBody = new EmailPasswordUserBody(email, password);
        Response changeResponse;
        ChangeUserEmailBody newUserBody = new ChangeUserEmailBody(newEmail);

        response = loginUserRequest(emailPasswordUserBody, authLoginApiPath);
        token = response.path("accessToken")
                .toString().replaceAll("Bearer ", "");
        changeResponse = changeUserRequest(newUserBody, authUserApiPath, token);
        assertEquals(SC_OK, changeResponse.statusCode());
    }

    @Test
    public void changeEmailWithLoginShouldBePossibleBody(){
        EmailPasswordUserBody emailPasswordUserBody = new EmailPasswordUserBody(email, password);
        Response changeResponse;
        ChangeUserEmailBody newUserBody = new ChangeUserEmailBody(newEmail);

        response = loginUserRequest(emailPasswordUserBody, authLoginApiPath);
        token = response.path("accessToken")
                .toString().replaceAll("Bearer ", "");
        changeResponse = changeUserRequest(newUserBody, authUserApiPath, token);
        assertTrue(changeResponse.path("success"));
        assertEquals(newEmail.toLowerCase(), changeResponse.path("user.email"));
        assertEquals(name, changeResponse.path("user.name"));
    }

    @Test
    public void changeNameWithLoginShouldBePossibleStatusCode(){
        EmailPasswordUserBody emailPasswordUserBody = new EmailPasswordUserBody(email, password);
        Response changeResponse;
        ChangeUserNameBody newUserBody = new ChangeUserNameBody(newName);

        response = loginUserRequest(emailPasswordUserBody, authLoginApiPath);
        token = response.path("accessToken")
                .toString().replaceAll("Bearer ", "");
        changeResponse = changeUserRequest(newUserBody, authUserApiPath, token);
        assertEquals(SC_OK, changeResponse.statusCode());
    }

    @Test
    public void changeNameWithLoginShouldBePossibleBody(){
        EmailPasswordUserBody emailPasswordUserBody = new EmailPasswordUserBody(email, password);
        Response changeResponse;
        ChangeUserNameBody newUserBody = new ChangeUserNameBody(newName);

        response = loginUserRequest(emailPasswordUserBody, authLoginApiPath);
        token = response.path("accessToken")
                .toString().replaceAll("Bearer ", "");
        changeResponse = changeUserRequest(newUserBody, authUserApiPath, token);
        assertTrue(changeResponse.path("success"));
        assertEquals(email.toLowerCase(), changeResponse.path("user.email"));
        assertEquals(newName, changeResponse.path("user.name"));
    }

    @Test
    public void changePasswordWithLoginShouldBePossibleStatusCode(){
        EmailPasswordUserBody emailPasswordUserBody = new EmailPasswordUserBody(email, password);
        Response changeResponse;
        ChangeUserPasswordBody newUserBody = new ChangeUserPasswordBody(newPassword);

        response = loginUserRequest(emailPasswordUserBody, authLoginApiPath);
        token = response.path("accessToken")
                .toString().replaceAll("Bearer ", "");
        changeResponse = changeUserRequest(newUserBody, authUserApiPath, token);
        assertEquals(SC_OK, changeResponse.statusCode());
    }

    @Test
    public void changePasswordWithLoginShouldBePossibleBody(){
        EmailPasswordUserBody emailPasswordUserBody = new EmailPasswordUserBody(email, password);
        Response changeResponse;
        ChangeUserPasswordBody newUserBody = new ChangeUserPasswordBody(newPassword);

        response = loginUserRequest(emailPasswordUserBody, authLoginApiPath);
        token = response.path("accessToken")
                .toString().replaceAll("Bearer ", "");
        changeResponse = changeUserRequest(newUserBody, authUserApiPath, token);
        assertTrue(changeResponse.path("success"));
        assertEquals(email.toLowerCase(), changeResponse.path("user.email"));
        assertEquals(name, changeResponse.path("user.name"));
    }

    @Test
    public void changeAllUserInfoWithoutLoginShouldFailStatusCode(){
        EmailPasswordUserBody emailPasswordUserBody = new EmailPasswordUserBody(email, password);
        Response changeResponse;
        WholeUserBody newUserBody = new WholeUserBody(newEmail, newPassword, newName);

        changeResponse = changeUserRequest(newUserBody, authUserApiPath, "");
        assertEquals(SC_UNAUTHORIZED, changeResponse.statusCode());

        response = loginUserRequest(emailPasswordUserBody, authLoginApiPath);
        token = response.path("accessToken")
                .toString().replaceAll("Bearer ", "");
    }

    @Test
    public void changeAllUserInfoWithoutLoginShouldFailBody(){
        EmailPasswordUserBody emailPasswordUserBody = new EmailPasswordUserBody(email, password);
        Response changeResponse;
        WholeUserBody newUserBody = new WholeUserBody(newEmail, newPassword, newName);

        changeResponse = changeUserRequest(newUserBody, authUserApiPath, "");
        assertFalse(changeResponse.path("success"));
        assertEquals(CHANGE_WITHOUT_LOGIN_MESSAGE, changeResponse.path("message"));

        response = loginUserRequest(emailPasswordUserBody, authLoginApiPath);
        token = response.path("accessToken")
                .toString().replaceAll("Bearer ", "");
    }

    @Test
    public void changeEmailWithoutLoginShouldFailStatusCode(){
        EmailPasswordUserBody emailPasswordUserBody = new EmailPasswordUserBody(email, password);
        Response changeResponse;
        ChangeUserEmailBody newUserBody = new ChangeUserEmailBody(newEmail);

        changeResponse = changeUserRequest(newUserBody, authUserApiPath, "");
        assertEquals(SC_UNAUTHORIZED, changeResponse.statusCode());

        response = loginUserRequest(emailPasswordUserBody, authLoginApiPath);
        token = response.path("accessToken")
                .toString().replaceAll("Bearer ", "");
    }

    @Test
    public void changeEmailWithoutLoginShouldFailBody(){
        EmailPasswordUserBody emailPasswordUserBody = new EmailPasswordUserBody(email, password);
        Response changeResponse;
        ChangeUserEmailBody newUserBody = new ChangeUserEmailBody(newEmail);

        changeResponse = changeUserRequest(newUserBody, authUserApiPath, "");
        assertFalse(changeResponse.path("success"));
        assertEquals(CHANGE_WITHOUT_LOGIN_MESSAGE, changeResponse.path("message"));

        response = loginUserRequest(emailPasswordUserBody, authLoginApiPath);
        token = response.path("accessToken")
                .toString().replaceAll("Bearer ", "");
    }

    @Test
    public void changeNameWithoutLoginShouldFailStatusCode(){
        EmailPasswordUserBody emailPasswordUserBody = new EmailPasswordUserBody(email, password);
        Response changeResponse;
        ChangeUserNameBody newUserBody = new ChangeUserNameBody(newName);

        changeResponse = changeUserRequest(newUserBody, authUserApiPath, "");
        assertEquals(SC_UNAUTHORIZED, changeResponse.statusCode());

        response = loginUserRequest(emailPasswordUserBody, authLoginApiPath);
        token = response.path("accessToken")
                .toString().replaceAll("Bearer ", "");
    }

    @Test
    public void changeNameWithoutLoginShouldFailBody(){
        EmailPasswordUserBody emailPasswordUserBody = new EmailPasswordUserBody(email, password);
        Response changeResponse;
        ChangeUserNameBody newUserBody = new ChangeUserNameBody(newName);

        changeResponse = changeUserRequest(newUserBody, authUserApiPath, "");
        assertFalse(changeResponse.path("success"));
        assertEquals(CHANGE_WITHOUT_LOGIN_MESSAGE, changeResponse.path("message"));

        response = loginUserRequest(emailPasswordUserBody, authLoginApiPath);
        token = response.path("accessToken")
                .toString().replaceAll("Bearer ", "");
    }

    @Test
    public void changePasswordWithoutLoginShouldFailStatusCode(){
        EmailPasswordUserBody emailPasswordUserBody = new EmailPasswordUserBody(email, password);
        Response changeResponse;
        ChangeUserPasswordBody newUserBody = new ChangeUserPasswordBody(newPassword);

        changeResponse = changeUserRequest(newUserBody, authUserApiPath, "");
        assertEquals(SC_UNAUTHORIZED, changeResponse.statusCode());

        response = loginUserRequest(emailPasswordUserBody, authLoginApiPath);
        token = response.path("accessToken")
                .toString().replaceAll("Bearer ", "");
    }

    @Test
    public void changePasswordWithoutLoginShouldFailBody(){
        EmailPasswordUserBody emailPasswordUserBody = new EmailPasswordUserBody(email, password);
        Response changeResponse;
        ChangeUserPasswordBody newUserBody = new ChangeUserPasswordBody(newPassword);

        changeResponse = changeUserRequest(newUserBody, authUserApiPath, "");
        assertFalse(changeResponse.path("success"));
        assertEquals(CHANGE_WITHOUT_LOGIN_MESSAGE, changeResponse.path("message"));

        response = loginUserRequest(emailPasswordUserBody, authLoginApiPath);
        token = response.path("accessToken")
                .toString().replaceAll("Bearer ", "");
    }

    @Test
    public void changeAllUserInfoWithExistingEmailShouldFailStatusCode(){
        WholeUserBody secondUser = new WholeUserBody(newEmail, password, name);
        createUserRequest(secondUser, authRegisterApiPath);
        EmailPasswordUserBody emailPasswordUserBody = new EmailPasswordUserBody(email, password);
        Response changeResponse;
        WholeUserBody newUserBody = new WholeUserBody(newEmail, newPassword, newName);

        response = loginUserRequest(emailPasswordUserBody, authLoginApiPath);
        token = response.path("accessToken")
                .toString().replaceAll("Bearer ", "");

        changeResponse = changeUserRequest(newUserBody, authUserApiPath, token);
        assertEquals(SC_FORBIDDEN, changeResponse.statusCode());

        EmailPasswordUserBody secondUserLoginBody = new EmailPasswordUserBody(newEmail, password);
        deleteUserRequest(loginUserRequest(secondUserLoginBody, authLoginApiPath)
                .path("accessToken").toString().replaceAll("Bearer ", ""), authUserApiPath);
    }

    @Test
    public void changeAllUserInfoWithExistingEmailShouldFailBody(){
        WholeUserBody secondUser = new WholeUserBody(newEmail, password, name);
        createUserRequest(secondUser, authRegisterApiPath);
        EmailPasswordUserBody emailPasswordUserBody = new EmailPasswordUserBody(email, password);
        Response changeResponse;
        WholeUserBody newUserBody = new WholeUserBody(newEmail, newPassword, newName);

        response = loginUserRequest(emailPasswordUserBody, authLoginApiPath);
        token = response.path("accessToken")
                .toString().replaceAll("Bearer ", "");

        changeResponse = changeUserRequest(newUserBody, authUserApiPath, token);
        assertEquals(EXISTING_EMAIL_MESSAGE, changeResponse.path("message"));

        EmailPasswordUserBody secondUserLoginBody = new EmailPasswordUserBody(newEmail, password);
        deleteUserRequest(loginUserRequest(secondUserLoginBody, authLoginApiPath)
                .path("accessToken").toString().replaceAll("Bearer ", ""), authUserApiPath);
    }

    @Test
    public void changeUserEmailWithExistingEmailShouldFailStatusCode(){
        WholeUserBody secondUser = new WholeUserBody(newEmail, password, name);
        createUserRequest(secondUser, authRegisterApiPath);
        EmailPasswordUserBody emailPasswordUserBody = new EmailPasswordUserBody(email, password);
        Response changeResponse;
        ChangeUserEmailBody newUserBody = new ChangeUserEmailBody(newEmail);

        response = loginUserRequest(emailPasswordUserBody, authLoginApiPath);
        token = response.path("accessToken")
                .toString().replaceAll("Bearer ", "");

        changeResponse = changeUserRequest(newUserBody, authUserApiPath, token);
        assertEquals(SC_FORBIDDEN, changeResponse.statusCode());

        EmailPasswordUserBody secondUserLoginBody = new EmailPasswordUserBody(newEmail, password);
        deleteUserRequest(loginUserRequest(secondUserLoginBody, authLoginApiPath)
                .path("accessToken").toString().replaceAll("Bearer ", ""), authUserApiPath);
    }

    @Test
    public void changeUserEmailWithExistingEmailShouldFailBody(){
        WholeUserBody secondUser = new WholeUserBody(newEmail, password, name);
        createUserRequest(secondUser, authRegisterApiPath);
        EmailPasswordUserBody emailPasswordUserBody = new EmailPasswordUserBody(email, password);
        Response changeResponse;
        ChangeUserEmailBody newUserBody = new ChangeUserEmailBody(newEmail);

        response = loginUserRequest(emailPasswordUserBody, authLoginApiPath);
        token = response.path("accessToken")
                .toString().replaceAll("Bearer ", "");

        changeResponse = changeUserRequest(newUserBody, authUserApiPath, token);
        assertEquals(EXISTING_EMAIL_MESSAGE, changeResponse.path("message"));

        EmailPasswordUserBody secondUserLoginBody = new EmailPasswordUserBody(newEmail, password);
        deleteUserRequest(loginUserRequest(secondUserLoginBody, authLoginApiPath)
                .path("accessToken").toString().replaceAll("Bearer ", ""), authUserApiPath);
    }

    @After
    public void deleteUser(){
        assertEquals(SC_ACCEPTED, deleteUserRequest(token, authUserApiPath).statusCode());
    }
}

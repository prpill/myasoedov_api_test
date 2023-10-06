import API.UserData;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;


public class SignUpTest {
    Methods method = new Methods();
    DataUsed data = new DataUsed();
    @Test
    public void successSignUpTest(){
        String email = Email.generate();
        String password = data.PASSWORD;

        Response response = method.createAccount(email, password);
        response.prettyPrint();
        int statusCode = response.getStatusCode();
        UserData user = response.body().jsonPath().getObject("user", UserData.class);
        String accessToken = response.body().jsonPath().getString("accessToken");

        Assert.assertNotNull(accessToken);
        Assert.assertNotNull(user.getId());
        Assert.assertEquals(user.getEmail(), email);
        Assert.assertEquals(statusCode, 201);
    }
    @Test
    public void negativeSignUpWithInvalidEmailTest(){
        String invalidEmail = data.NO_VALID_EMAIL;
        String password = data.PASSWORD;

        Response response = method.createAccount(invalidEmail, password);
        response.prettyPrint();
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 400);
    }
    @Test
    public void negativeSignUpWithEmptyEmailTest(){
        String emptyEmail = "";
        String password = data.PASSWORD;

        Response response = method.createAccount(emptyEmail, password);
        response.prettyPrint();
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 400);
    }
    @Test
    public void negativeSignUpWithEmptyPasswordTest(){
        String email = Email.generate();
        String emptyPassword = "";

        Response response = method.createAccount(email, emptyPassword);
        response.prettyPrint();
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 400);
    }
}

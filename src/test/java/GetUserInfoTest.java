import API.UserData;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.Test;
public class GetUserInfoTest {
    Methods method = new Methods();
    DataUsed data = new DataUsed();
    RequestSpecification request = RestAssured.given();
    String path = EndPoints.USER_ACTION;
    @Epic(value = "Working with a User Entity")
    @Feature(value = "Successful retrieval of user data")
    @Test
    public void successGetUserInfoTest(){
        String email = Email.generate();
        String password = data.PASSWORD;
        UserData regUser = method.createAccount(email, password)
                .body().jsonPath().getObject("user", UserData.class);

        Response response = request
                .when()
                .get(path + regUser.getId())
                .then()
                .extract().response();

        response.prettyPrint();
        int statusCode = response.getStatusCode();
        UserData user = response.jsonPath().getObject("", UserData.class);
        Assert.assertEquals(statusCode, 200);
        Assert.assertEquals(user.getId(), regUser.getId());
        Assert.assertEquals(user.getEmail(), regUser.getEmail());
    }
    @Epic(value = "Working with a User Entity")
    @Feature(value = "Negative test - receiving user data with a non-existent post id")
    @Test
    public void negativeGetUserInfoWithInvalidUserIdTest(){
        int invalidUserId = -1;

        Response response = request
                .when()
                .get(path + invalidUserId)
                .then()
                .extract().response();

        response.prettyPrint();
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 200);
    }
}

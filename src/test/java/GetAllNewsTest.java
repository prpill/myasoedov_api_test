import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class GetAllNewsTest {
    String path = EndPoints.POST_ACTION;
    @Epic(value = "Working with posts")
    @Feature(value = "Successfully receiving all posts")
    @Test
    public void successGetAllNewsTest(){
        Response response = given()
                .when()
                .get(path)
                .then()
                .extract().response();
        response.prettyPrint();
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 200);
    }
}

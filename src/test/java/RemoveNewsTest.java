import API.PostData;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class RemoveNewsTest {
    Methods method = new Methods();
    DataUsed data = new DataUsed();
    String path = EndPoints.POST_ACTION;
    @Test
    public void successRemoveNewsTest(){
        String accessToken = method.createAccount(Email.generate(), data.PASSWORD)
                .body().jsonPath().getString("accessToken");
        PostData post = method.createPost(accessToken).jsonPath().getObject("", PostData.class);
        Response response = given()
                .auth().oauth2(accessToken)
                .when()
                .delete(path + post.getId())
                .then()
                .extract().response();
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 200);
        Assert.assertNotNull(method.searchPost(post.getId()));
    }
    @Test
    public void negativeRemoveNewsWithInvalidPostIdTest(){
        String accessToken = method.createAccount(Email.generate(), data.PASSWORD)
                .body().jsonPath().getString("accessToken");
        int invalidPostId = -1;
        Response response= given()
                .auth().oauth2(accessToken)
                .when()
                .delete(path + invalidPostId)
                .then()
                .extract().response();
        response.prettyPrint();
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 404);
    }
    @Test
    public void negativeRemoveNewsWithoutAccessTest(){
        String accessToken = method.createAccount(Email.generate(), data.PASSWORD)
                .body().jsonPath().getString("accessToken");

        String creatorToken = method.createAccount(Email.generate(), data.PASSWORD)
                .body().jsonPath().getString("accessToken");
        PostData creatorPost = method.createPost(creatorToken).jsonPath().getObject("", PostData.class);
        Response response = given()
                .auth().oauth2(accessToken)
                .when()
                .delete(path + creatorPost.getId())
                .then()
                .extract().response();
        response.prettyPrint();
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 403);
    }
    @Test
    public void negativeRemoveNewsWithoutAuthorizationTest(){
        String accessToken = method.createAccount(Email.generate(), data.PASSWORD)
                .body().jsonPath().getString("accessToken");
        PostData post = method.createPost(accessToken).jsonPath().getObject("", PostData.class);
        Response response = given()
                .when()
                .delete(path + post.getId())
                .then()
                .extract().response();
        response.prettyPrint();
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 401);
    }
}

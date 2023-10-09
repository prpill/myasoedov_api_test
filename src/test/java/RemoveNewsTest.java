import API.PostData;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;
public class RemoveNewsTest {
    Methods method = new Methods();
    DataUsed data = new DataUsed();
    String path = EndPoints.POST_ACTION;
    @Epic(value = "Working with posts")
    @Feature(value = "Successful post deletion")
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
    @Epic(value = "Working with posts")
    @Feature(value = "Negative test - deleting a post with a non-existent post id")
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
    @Epic(value = "Working with posts")
    @Feature(value = "Negative test - deleting a post under another user    ")
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
    @Epic(value = "Working with posts")
    @Feature(value = "Negative test - deleting a post without authorization")
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

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
public class RemoveCommentTest {
    Methods method = new Methods();
    DataUsed data = new DataUsed();
    String path = EndPoints.COMMENT_ACTION;
    @Epic(value = "Working with comments")
    @Feature(value = "Successful deletion of comment")
    @Test
    public void successRemoveCommentTest(){
        String accessToken = method.createAccount(Email.generate(), data.PASSWORD)
                .body().jsonPath().getString("accessToken");
        int postId = method.createPost(accessToken).jsonPath().getInt("id");
        int commentId = method.createComment(accessToken,postId)
                .jsonPath().getInt("id");
        Response response = given()
                .auth().oauth2(accessToken)
                .when()
                .delete(path + commentId)
                .then()
                .extract().response();
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 200);
    }
    @Epic(value = "Working with comments")
    @Feature(value = "Negative test - deleting a comment with a non-existent post id")
    @Test
    public void negativeRemoveCommentWithInvalidCommentIdTest(){
        String accessToken = method.createAccount(Email.generate(), data.PASSWORD)
                .body().jsonPath().getString("accessToken");
        int invalidCommentId = -1;
        Response response = given()
                .auth().oauth2(accessToken)
                .when()
                .delete(path + invalidCommentId)
                .then()
                .extract().response();
        response.prettyPrint();
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 404);
    }
    @Epic(value = "Working with comments")
    @Feature(value = "Negative test - deleting a comment under another user")
    @Test
    public void negativeRemoveCommentWithoutAccessTest(){
        String creatorToken = method.createAccount(Email.generate(), data.PASSWORD)
                .body().jsonPath().getString("accessToken");
        int postId = method.createPost(creatorToken).jsonPath().getInt("id");
        int commentId = method.createComment(creatorToken,postId)
                .jsonPath().getInt("id");
        String accessToken = method.createAccount(Email.generate(), data.PASSWORD)
                .body().jsonPath().getString("accessToken");
        Response response = given()
                .auth().oauth2(accessToken)
                .when()
                .delete(path + commentId)
                .then()
                .extract().response();
        response.prettyPrint();
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 403);
    }
    @Epic(value = "Working with comments")
    @Feature(value = "Negative test - deleting a comment without authorization")
    @Test
    public void negativeRemoveCommentWithoutAuthorizationTest(){
        String accessToken = method.createAccount(Email.generate(), data.PASSWORD)
                .body().jsonPath().getString("accessToken");
        int postId = method.createPost(accessToken).jsonPath().getInt("id");
        int commentId = method.createComment(accessToken,postId)
                .jsonPath().getInt("id");
        Response response = given()
                .when()
                .delete(path + commentId)
                .then()
                .extract().response();
        response.prettyPrint();
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 401);
    }
}

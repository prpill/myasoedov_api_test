import API.CommentData;
import DTO.CreateCommentDto;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
public class CreateCommentTest {
    Methods method = new Methods();
    DataUsed data = new DataUsed();
    String path = EndPoints.COMMENT_ACTION;
    @Epic(value = "Working with comments")
    @Feature(value = "Successful comment creation")
    @Test
    public void successCreateCommentTest(){
        Response regResponse = method.createAccount(Email.generate(), data.PASSWORD);
        int userId = regResponse.jsonPath().getInt("user.id");
        String accessToken = regResponse
                .body().jsonPath().getString("accessToken");
        int postId = method.createPost(accessToken).jsonPath().getInt("id");
        Response response = method.createComment(accessToken,postId);
        response.prettyPrint();
        CommentData comment = response.jsonPath().getObject("", CommentData.class);
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 201);
        Assert.assertNotNull(comment.getId());
        Assert.assertEquals(comment.getText(), data.COMMENT);
        Assert.assertEquals(comment.getPostId(), postId);
        Assert.assertEquals(comment.getAuthorId(), userId);
    }
    @Epic(value = "Working with comments")
    @Feature(value = "Negative test - creating a comment without content")
    @Test
    public void negativeCreateCommentWithEmptyTextFieldTest(){
        String accessToken = method.createAccount(Email.generate(), data.PASSWORD)
                .body().jsonPath().getString("accessToken");
        int postId = method.createPost(accessToken).jsonPath().getInt("id");
        String emptyText = "";
        CreateCommentDto commentDto = new CreateCommentDto(emptyText, postId);
        Response response = given()
                .auth().oauth2(accessToken)
                .contentType(ContentType.JSON)
                .body(commentDto)
                .when()
                .post(path)
                .then()
                .extract().response();
        response.prettyPrint();
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 400);
    }
    @Epic(value = "Working with comments")
    @Feature(value = "Negative test - creating a comment with a non-existent post id")
    @Test
    public void negativeCreateCommentWithInvalidPostIdTest(){
        String accessToken = method.createAccount(Email.generate(), data.PASSWORD)
                .body().jsonPath().getString("accessToken");
        int invalidPostId = -1;
        CreateCommentDto commentDto = new CreateCommentDto(data.COMMENT, invalidPostId);
        Response response = given()
                .auth().oauth2(accessToken)
                .contentType(ContentType.JSON)
                .body(commentDto)
                .when()
                .post(path)
                .then()
                .extract().response();
        response.prettyPrint();
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 404);
    }
    @Epic(value = "Working with comments")
    @Feature(value = "Negative test - creating a comment without authorization")
    @Test
    public void negativeCreateCommentWithoutAuthorizationTest(){
        Response regResponse = method.createAccount(Email.generate(), data.PASSWORD);
        String accessToken = regResponse
                .body().jsonPath().getString("accessToken");
        int postId = method.createPost(accessToken).jsonPath().getInt("id");
        CreateCommentDto commentDto = new CreateCommentDto(data.COMMENT, postId);
        Response response = given()
                .contentType(ContentType.JSON)
                .body(commentDto)
                .when()
                .post(path)
                .then()
                .extract().response();
        response.prettyPrint();
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 401);
    }
}

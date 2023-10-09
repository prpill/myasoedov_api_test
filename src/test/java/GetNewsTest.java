import API.PostData;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
public class GetNewsTest {
    Methods method = new Methods();
    DataUsed data = new DataUsed();
    @Epic(value = "Working with posts")
    @Feature(value = "Successfully obtaining a post")
    @Test
    public void successGetNewsTest(){
        Response regResponse = method.createAccount(Email.generate(), data.PASSWORD);
        String accessToken = regResponse
                .body().jsonPath().getString("accessToken");
        PostData post = method.createPost(accessToken).jsonPath().getObject("", PostData.class);
        Response response = method.searchPost(post.getId());
        response.prettyPrint();

        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 200);
        PostData foundPost = response.jsonPath().getObject("", PostData.class);
        Assert.assertEquals(foundPost.getId(), post.getId());
        Assert.assertEquals(foundPost.getTitle(), post.getTitle());
        Assert.assertEquals(foundPost.getText(), post.getText());
        Assert.assertEquals(foundPost.getCoverPath(), post.getCoverPath());
        Assert.assertEquals(foundPost.getAuthorId(), post.getAuthorId());
    }
    @Epic(value = "Working with posts")
    @Feature(value = "Negative test - receiving a post with a non-existent post id")
    @Test
    public void negativeGetNewsWithInvalidPostId(){
        int invalidPostId = -1;

        Response response = method.searchPost(invalidPostId);
        response.prettyPrint();

        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 404);
    }
}

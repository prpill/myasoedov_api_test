import API.PostData;
import API.UserData;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class GetNewsTest {
    Methods method = new Methods();
    DataUsed data = new DataUsed();
    String path = EndPoints.POST_ACTION;
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
    @Test
    public void negativeGetNewsWithInvalidPostId(){
        int invalidPostId = -1;

        Response response = method.searchPost(invalidPostId);
        response.prettyPrint();

        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 404);
    }
}

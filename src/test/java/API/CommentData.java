package API;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentData {
    public Integer id;
    public String text;
    public Integer authorId;
    public Integer postId;
}

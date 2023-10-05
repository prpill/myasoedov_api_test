package API;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostData {
    private Integer id;
    private String title;
    private String text;
    private String coverPath;
    private Integer authorId;
}

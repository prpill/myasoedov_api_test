package API;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserData {
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String avatarPath;
}

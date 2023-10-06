package API;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserData {
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String avatarPath;
}

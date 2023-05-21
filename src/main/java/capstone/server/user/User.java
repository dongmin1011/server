package capstone.server.user;




import lombok.Data;

@Data
public class User {
    private Long id;

    private String loginId;

    private String password;

    private String name;


}

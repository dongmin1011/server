package capstone.server.dto;

import capstone.server.entity.UserEntity;
import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String loginId;
    private String password;
    private String name;
    private String token;

    public static UserDTO toUserDTO(UserEntity userEntity){
        UserDTO userDTO = new UserDTO();
        userDTO.setId(userEntity.getId());
        userDTO.setLoginId(userEntity.getLoginId());
        userDTO.setPassword(userEntity.getPassword());
        userDTO.setName(userEntity.getName());

        return userDTO;
    }
}

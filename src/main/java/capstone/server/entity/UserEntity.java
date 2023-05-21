package capstone.server.entity;

import capstone.server.dto.UserDTO;
import capstone.server.service.UserService;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@Getter
@Table(name="user_table")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //auto increment
    private Long id;


    @Column(unique = true) //unique제약조건
    private String loginId;

    @Column
    private String password;

    @Column
    private String name;

    public static UserEntity toUserEntity(UserDTO userDTO){
        UserEntity userEntity = new UserEntity();
        userEntity.setLoginId(userDTO.getLoginId());
        userEntity.setPassword(userDTO.getPassword());
        userEntity.setName(userDTO.getName());

        return userEntity;
    }

    public static UserEntity toUpdateMemberEntity(UserDTO userDTO) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(userDTO.getId());
        userEntity.setLoginId(userDTO.getLoginId());
        userEntity.setPassword(userDTO.getPassword());
        userEntity.setName(userDTO.getName());
        return userEntity;
    }
}

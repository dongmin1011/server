package capstone.server.service;

//import capstone.server.User.UserRepository;
import capstone.server.dto.UserDTO;
import capstone.server.entity.UserEntity;
import capstone.server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    public void save(UserDTO userDTO) {
        UserEntity userEntity = UserEntity.toUserEntity(userDTO);

        userRepository.save(userEntity);
    }

    public UserDTO login(UserDTO userDTO) {

        Optional<UserEntity> byLoginId = userRepository.findByLoginId(userDTO.getLoginId());
        if(byLoginId.isPresent()){
            //조회 결과 있음
            UserEntity userEntity = byLoginId.get();
            if(userEntity.getPassword().equals(userDTO.getPassword())){
                //비밀번호 일치
                UserDTO dto = UserDTO.toUserDTO(userEntity);
                System.out.println("dto = " + dto);
                return dto;
            }else{
                //비밀번호 불일치
                System.out.println("userEntity = " + userEntity);
                return null;
            }
        }else {
            //조회결과없음
            return null;
        }

    }

    public UserDTO update(UserDTO userDTO) {
        UserEntity update = userRepository.save(UserEntity.toUpdateMemberEntity(userDTO));
        UserDTO dto = UserDTO.toUserDTO(update);
        return dto;
    }
}

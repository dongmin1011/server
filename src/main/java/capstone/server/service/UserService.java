package capstone.server.service;

//import capstone.server.User.UserRepository;
import capstone.server.JWTToken.JwtTokenProvider;
import capstone.server.dto.UserDTO;
//import capstone.server.dto.UserStoreDTO;
import capstone.server.entity.StoreEntity;
import capstone.server.entity.UserEntity;
import capstone.server.entity.UserStoreEntity;
import capstone.server.repository.StoreRepository;
import capstone.server.repository.UserRepository;
import capstone.server.repository.UserStoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserStoreRepository userStoreRepository;

    private final StoreRepository storeRepository;
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

                //사용자의 좋아요 목록을 저장
                List<UserStoreEntity> storeEntities = userStoreRepository.findAllByUserId(dto.getId());
//                List<UserStoreDTO> UserStoreDTOS = new ArrayList<>();
                if(storeEntities!=null){
                    Set<String> userStoreDTOS = new HashSet<>();
                    for(UserStoreEntity storeEntity:  storeEntities){
                        String temp ="";
                        temp = storeEntity.getStoreName();
                        userStoreDTOS.add(temp);
                    }
                    dto.setStoreDTOList(userStoreDTOS);
                }

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
        UserEntity userEntity = UserEntity.toUpdateMemberEntity(userDTO);
        // storeEntities 컬렉션 업데이트
        List<UserStoreEntity> newStoreEntities = new ArrayList<>();
        for (String storeEntity : userDTO.getStoreDTOList()) {
            System.out.println("storeEntity = " + storeEntity);
            // 기존 storeEntities에서 이미 제거된 엔티티는 다시 추가하지 않음
            UserStoreEntity userStoreEntity = new UserStoreEntity();
            userStoreEntity.setStoreName(storeEntity);
            userStoreEntity.setUser(userRepository.findById(userDTO.getId()).get());

//            if (!userEntity.getStoreEntities().contains(userStoreEntity)) {
                newStoreEntities.add(userStoreEntity);
//            }
        }
        userEntity.setStoreEntities(newStoreEntities);

        UserEntity update = userRepository.save(userEntity);
        UserDTO dto = UserDTO.toUserDTO(update);

        List<UserStoreEntity> storeEntities = userEntity.getStoreEntities();
        Set<String> temp = new HashSet<>();
        if(!storeEntities.isEmpty()){
            for(UserStoreEntity userStore : storeEntities){
                temp.add(userStore.getStoreName());
            }
        }
        dto.setStoreDTOList(temp);

        dto.setToken(jwtTokenProvider.createToken(dto.getLoginId(), dto.getPassword()));

        System.out.println("dto = " + dto);
        return dto;
    }

    public void saveFavorite(Long userId, String storeName){
        UserStoreEntity userStoreEntity = new UserStoreEntity();
        userStoreEntity.setUser(userRepository.findById(userId).get());
        userStoreEntity.setStoreName(storeName);

        StoreEntity storeEntity = storeRepository.findByName(storeName).get();


        if (storeEntity != null) {
            // StoreEntity가 존재할 경우에만 counter 증가
            storeEntity.setFavoriteCounter(storeEntity.getFavoriteCounter() + 1);
            storeRepository.save(storeEntity);

            userStoreRepository.save(userStoreEntity);
        }
//        userStoreRepository.save((userStoreEntity));

    }

    public Set<String> favoriteStoreSet(Long userId) {
        List<UserStoreEntity> userStoreEntities = userStoreRepository.findAllByUserId(userId);
        Set<String> storeSet = new HashSet<>();
        if (!userStoreEntities.isEmpty()) {
            for (UserStoreEntity userStore : userStoreEntities) {
                storeSet.add(userStore.getStoreName());
            }
        }
        return storeSet;
    }

    @Transactional
    public void deleteByStoreName(String storeName) {
        StoreEntity storeEntity = storeRepository.findByName(storeName).get();


        if (storeEntity != null) {
            // StoreEntity가 존재할 경우에만 counter 증가
            if(storeEntity.getFavoriteCounter() - 1<0){
                storeEntity.setFavoriteCounter(0);
            }else {
                storeEntity.setFavoriteCounter(storeEntity.getFavoriteCounter() - 1);
            }
            storeRepository.save(storeEntity);

            userStoreRepository.deleteByStoreName(storeName);
        }

    }

}

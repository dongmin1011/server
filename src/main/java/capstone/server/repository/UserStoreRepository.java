package capstone.server.repository;

import capstone.server.entity.UserStoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserStoreRepository extends JpaRepository<UserStoreEntity, Long> {
//    List<UserStoreEntity> findById(Long Id);
// 여러 개의 엔티티 조회
    List<UserStoreEntity> findAllByUserId(Long userId); // userId에 해당하는 모든 UserStoreEntity를 조회

    void deleteByStoreName(String storeName);
}

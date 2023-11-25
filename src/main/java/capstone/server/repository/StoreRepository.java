package capstone.server.repository;

//import capstone.server.entity.CommentEntity;
import capstone.server.entity.StoreEntity;
import capstone.server.entity.UserEntity;
import capstone.server.entity.UserStoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<StoreEntity, String> {
//    StoreEntity save(StoreEntity post);

    Optional<StoreEntity> findByName(String name);

//    List<StoreEntity> findAllByUserId(String name);
}


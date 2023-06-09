package capstone.server.repository;

import capstone.server.entity.Comment.NaverCommentEntity;
import capstone.server.entity.MenuEntity;
import capstone.server.entity.StoreEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NaverCommentRepository   extends JpaRepository<NaverCommentEntity, String> {
    @EntityGraph(attributePaths = "image")
    List<NaverCommentEntity> findByStoreNameIgnoreCase(String storeName);

    Optional<NaverCommentEntity> findByNickName(String name);
}

package capstone.server.repository;

import capstone.server.entity.Comment.ETParentCommentEntity;
import capstone.server.entity.Comment.NaverCommentEntity;
import capstone.server.entity.CommunityCommentEntity;
import capstone.server.entity.CommunityEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommunityCommentRepository   extends JpaRepository<CommunityEntity, String> {

    Optional<CommunityEntity> findById(Long id);
    List<CommunityEntity> findByStoreNameIgnoreCase(String storeName);

}

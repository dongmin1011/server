package capstone.server.repository.ETComment;

import capstone.server.entity.Comment.ETParentCommentEntity;
import capstone.server.entity.Comment.ETPostEntity;
import capstone.server.entity.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ETParentRepository   extends JpaRepository<ETParentCommentEntity, String> {
    Optional<ETParentCommentEntity> findById(Long id);
}

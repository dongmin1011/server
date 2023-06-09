package capstone.server.repository.ETComment;

import capstone.server.entity.Comment.ETChildCommentEntity;
import capstone.server.entity.Comment.ETPostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ETChildRepository   extends JpaRepository<ETChildCommentEntity, String> {
}

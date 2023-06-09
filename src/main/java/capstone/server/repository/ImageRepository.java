package capstone.server.repository;

import capstone.server.entity.Comment.ImageEntity;
import capstone.server.entity.MenuEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository  extends JpaRepository<ImageEntity, String> {
}

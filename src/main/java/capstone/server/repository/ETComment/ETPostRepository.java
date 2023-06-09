package capstone.server.repository.ETComment;

import capstone.server.entity.Comment.ETPostEntity;
import capstone.server.entity.Comment.NaverCommentEntity;
import capstone.server.entity.MenuEntity;
import capstone.server.entity.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ETPostRepository  extends JpaRepository<ETPostEntity, String> {
    Optional<ETPostEntity> findByNum(String num);
    ETPostEntity findByNumAndStore_Name(String num, String name);
    List<ETPostEntity> findByStoreNameIgnoreCase(String storeName);
}

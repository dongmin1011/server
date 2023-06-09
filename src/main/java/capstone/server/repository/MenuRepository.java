package capstone.server.repository;


import capstone.server.entity.MenuEntity;
import capstone.server.entity.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MenuRepository  extends JpaRepository<MenuEntity, String> {

    List<MenuEntity> findByStoreNameIgnoreCase(String name);
}

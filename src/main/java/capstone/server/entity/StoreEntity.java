package capstone.server.entity;

import capstone.server.dto.StoreDTO;
import capstone.server.dto.UserDTO;
import capstone.server.entity.Comment.ETPostEntity;
import capstone.server.entity.Comment.NaverCommentEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@Table(name="store")
public class StoreEntity {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY) //auto increment
//    private Long id;
//
//
//    @Column(length = 20, unique = true)
    @Id
    private String name;

    @Column(length = 20)
    private String PH;

    @Column(length = 30)
    private String address;

    @Column()
    private String businessTime;

    @Column
    private String score;

    @Column(columnDefinition = "int default 0") // 컬럼 기본값 설정
    private int favoriteCounter = 0; // 좋아요 수를 저장하는 변수

    @Column(columnDefinition = "int default 0") // 컬럼 기본값 설정
    private int viewsCounter = 0; // 조회 수를 저장하는 변수

    @Column(length = 300)
    private String URL;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuEntity> menus;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NaverCommentEntity> naverComment;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ETPostEntity> ETComment;
    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommunityEntity> communityComment;

    public static StoreEntity convertToEntity(StoreDTO storeDTO) {
        StoreEntity storeEntity = new StoreEntity();
        storeEntity.setName(storeDTO.getStoreName());
        storeEntity.setPH(storeDTO.getPh());
        storeEntity.setAddress(storeDTO.getAddress());
        storeEntity.setBusinessTime(storeDTO.getBusinessTime());
        storeEntity.setScore(storeDTO.getScore());
        // 필요한 다른 필드도 설정할 수 있습니다.
        storeEntity.setFavoriteCounter(0);
        storeEntity.setViewsCounter(0);
        storeEntity.setURL(storeDTO.getStorePhoto());
        return storeEntity;
    }
// 메뉴 추가 메서드
public void addMenu(MenuEntity menu) {
    if (menus == null) {
        menus = new ArrayList<>();
    }
    menus.add(menu);
    menu.setStore(this);
}

    // 메뉴 제거 메서드
    public void removeMenu(MenuEntity menu) {
        if (menus != null) {
            menus.remove(menu);
            menu.setStore(null);
        }
    }
    public void incrementViewsCounter() {
        this.viewsCounter++;
    }

}

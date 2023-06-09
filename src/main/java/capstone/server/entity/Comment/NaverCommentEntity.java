package capstone.server.entity.Comment;


import capstone.server.entity.MenuEntity;
import capstone.server.entity.StoreEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "navercomment")
public class NaverCommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20)
    private String nickName;

    @Column(length = 30)
    private String date;

//    @Column(length = 1000)
//    private String image_urls;

    @Column(length = 1000)
    private String review;

    @OneToMany(mappedBy = "name", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ImageEntity> image;

    // 댓글과 게시물의 다대일 관계 설정
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "name")
    private StoreEntity store;
}

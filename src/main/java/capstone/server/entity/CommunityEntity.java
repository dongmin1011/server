package capstone.server.entity;



import capstone.server.entity.Comment.NaverCommentEntity;
import capstone.server.entity.StoreEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "communityEntity")
public class CommunityEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20)
    private String nickName;

    @Column(length = 30)
    private String date;

    @Column
    private String title;
    @Column
    private String review;

    // 댓글과 게시물의 다대일 관계 설정
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "name")
    private StoreEntity store;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true , fetch = FetchType.EAGER)
    private List<CommunityCommentEntity> Comments;
}

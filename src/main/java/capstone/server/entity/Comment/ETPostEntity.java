package capstone.server.entity.Comment;

import capstone.server.entity.StoreEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "etPost")
public class ETPostEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20)
    private String num;

    @Column(length = 30)
    private String title;

    @Column (length = 5000)
    private String body;

    @OneToMany(mappedBy = "comments", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<ETParentCommentEntity> comments;


    // 댓글과 게시물의 다대일 관계 설정

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "name")
    private StoreEntity store;
}

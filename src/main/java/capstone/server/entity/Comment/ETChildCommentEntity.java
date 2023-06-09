package capstone.server.entity.Comment;

import capstone.server.entity.StoreEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "etCommentChild")
public class ETChildCommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500)
    private String text;

    @Column(length = 30)
    private String date;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent")
    private ETParentCommentEntity parent;
}

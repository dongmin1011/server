package capstone.server.entity.Comment;

import capstone.server.entity.StoreEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "etCommentParent")
public class ETParentCommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 30)
    private String date;

    @Column(length = 2000)
    private String text;


    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<ETChildCommentEntity> child;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "num")
    private ETPostEntity comments;
}

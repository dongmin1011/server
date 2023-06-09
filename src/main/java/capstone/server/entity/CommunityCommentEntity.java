package capstone.server.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "communityCommentEntity")
public class CommunityCommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20)
    private String nickName;

    @Column(length = 30)
    private String date;

    @Column
    private String review;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent")
    private CommunityEntity parent;
}

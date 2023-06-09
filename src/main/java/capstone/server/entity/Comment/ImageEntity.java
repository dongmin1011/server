package capstone.server.entity.Comment;

import capstone.server.entity.StoreEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "imageurl")
public class ImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;





    @Column(length = 1000)
    private String image_urls;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image")
    private NaverCommentEntity name;
}

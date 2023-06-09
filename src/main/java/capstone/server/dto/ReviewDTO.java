package capstone.server.dto;

import lombok.Data;

import java.util.List;

@Data
public class ReviewDTO{
    private List<NaverCommentDTO> reviews;
//    private List<ETPostDTO> reviews;
    private String storeName;
}

package capstone.server.dto;

import lombok.Data;

import java.util.List;

@Data
public class CommunityReview {
    private List<CommunityDTO> reviews;
    private CommunityDTO review;
    private String storeName;
}

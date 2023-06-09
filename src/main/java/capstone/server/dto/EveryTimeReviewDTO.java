package capstone.server.dto;

import lombok.Data;

import java.util.List;

@Data
public class EveryTimeReviewDTO {
    private List<ETPostDTO> reviews;
    private String storeName;
}

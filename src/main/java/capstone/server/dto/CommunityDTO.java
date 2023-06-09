package capstone.server.dto;

import lombok.Data;

import java.util.List;

@Data
public class CommunityDTO {
//    private String storeName;
    private Long id;
    private String title;
    private String date;
    private String name;
    private String review;

    private List<CommunityChildDTO> childDTOList;
}

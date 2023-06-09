package capstone.server.dto;

import lombok.Data;

import java.util.List;


@Data
public class NaverCommentDTO {
    private String nickName;


    private String date;
//    private String storeName;

    private String review;

    private List<String> image_urls;
}

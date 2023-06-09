package capstone.server.dto;

import lombok.Data;

import java.util.List;

@Data
public class ETPostDTO {
    private String num;
    private String review;
    private String title;

    List<ETCommentDTO> comments;
}

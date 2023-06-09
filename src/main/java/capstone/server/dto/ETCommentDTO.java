package capstone.server.dto;

import lombok.Data;

import java.util.List;

@Data
public class ETCommentDTO {
    private String date;
    private String division;
    private String text;
    private List<ETChildCommentDTO> child;
}

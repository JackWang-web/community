package life.majiang.community.dto;

import life.majiang.community.model.User;
import lombok.Data;

//这是传输层，将问题类与用户类建立联系
@Data
public class QuestionDTO {
    private Integer id;
    private String title;
    private String description;
    private String tag;
    private Long gmtCreate;
    private Long gmtModified;
    private Integer creator;
    private Integer viewCount;
    private Integer commentCount;
    private Integer likeCount;
    private User user;
}

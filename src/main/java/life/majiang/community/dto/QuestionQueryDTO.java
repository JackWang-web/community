package life.majiang.community.dto;

import lombok.Data;

/**
 * 问题搜索
 */
@Data
public class QuestionQueryDTO {
    private String search;
    private String tag;
    private Integer page;
    private Integer size;
}

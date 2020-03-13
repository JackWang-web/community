package life.majiang.community.dto;

import lombok.Data;

/**
 * 问题搜索
 */
@Data
public class QuestionQueryDTO {
    private String search;
    private Integer page;
    private Integer size;
}

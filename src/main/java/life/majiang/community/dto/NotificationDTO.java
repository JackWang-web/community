package life.majiang.community.dto;

import lombok.Data;

@Data
public class NotificationDTO {
    private Long id;
    private Long gmtCreate;
    private Integer status;
    private Long notifier;
    private String notifierName;
    private String outerTitle;
    private Long outerid;
    private String typeName;
    private Integer type;

    private int[] navigatepageNums;//导航栏：
    private Integer pageNum;//当前页码
    private Integer pageSize;//每页几条
    private Integer pages;//总页数：
    private Boolean hasPreviousPage;//有没有上一页
    private Boolean hasNextPage;//有没有下一页
}

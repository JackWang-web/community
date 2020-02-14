package life.majiang.community.dto;

import com.github.pagehelper.PageInfo;
import life.majiang.community.model.Question;
import life.majiang.community.model.User;
import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//这是传输层，将问题类与用户类建立联系
@Data
public class QuestionDTO<T> {
    private Long id;
    private String title;
    private String description;
    private String tag;
    private Long gmtCreate;
    private Long gmtModified;
    private Long creator;
    private Integer viewCount;
    private Integer commentCount;
    private Integer likeCount;
    private User user;


    private int[] navigatepageNums;//导航栏：
    private Integer pageNum;//当前页码
    private Integer pageSize;//每页几条
    private Integer pages;//总页数：
    private Boolean hasPreviousPage;//有没有上一页
    private Boolean hasNextPage;//有没有下一页




}

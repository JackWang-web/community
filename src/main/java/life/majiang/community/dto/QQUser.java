package life.majiang.community.dto;

import lombok.Data;

@Data
public class QQUser {
    private Long ret;//返回码
    private String msg;//错误信息
    private String nickname;//昵称
    private String gender;//性别
    private String figureurl_qq_2;//头像
}

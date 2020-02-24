package life.majiang.community.mapper;

import life.majiang.community.model.Comment;
import life.majiang.community.model.Question;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentExtMapper {
    int incCommentCount(Comment comment);
}
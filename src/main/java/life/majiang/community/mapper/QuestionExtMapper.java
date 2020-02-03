package life.majiang.community.mapper;

import life.majiang.community.model.Question;
import org.springframework.stereotype.Repository;
@Repository
public interface QuestionExtMapper {
    int incView(Question record);



}
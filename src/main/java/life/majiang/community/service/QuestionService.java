package life.majiang.community.service;

/*
用一个中间层来组装question与user
*/
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import life.majiang.community.dto.QuestionDTO;
import life.majiang.community.exception.CustomizeErrorCode;
import life.majiang.community.exception.CustomizeException;
import life.majiang.community.mapper.QuestionExtMapper;
import life.majiang.community.mapper.QuestionMapper;
import life.majiang.community.mapper.UserMapper;
import life.majiang.community.model.Question;
import life.majiang.community.model.QuestionExample;
import life.majiang.community.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class QuestionService {

    @Autowired(required=true)
    private QuestionMapper questionMapper;

    @Autowired(required=true)
    private UserMapper userMapper;

    @Autowired
    private QuestionExtMapper questionExtMapper;

    public List<QuestionDTO> list(Integer page, Integer size) {
        PageHelper.startPage(page,size);
        List<Question> questions = questionMapper.selectByExample(new QuestionExample());
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        PageInfo<Question> questionPageInfo = new PageInfo<>(questions,5);

        for (Question question : questions) {
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);

            questionDTO.setNavigatepageNums(questionPageInfo.getNavigatepageNums());
            questionDTO.setPageNum(questionPageInfo.getPageNum());
            questionDTO.setPageSize(questionPageInfo.getPageSize());
            questionDTO.setPages(questionPageInfo.getPages());
            questionDTO.setHasPreviousPage(questionPageInfo.isHasPreviousPage());
            questionDTO.setHasNextPage(questionPageInfo.isHasNextPage());
            questionDTOList.add(questionDTO);
            System.out.println(questionDTOList);

        }
/*
        for (Question question : questions) {
            User user = userMapper.findById(question.getCreator());
            *//*
            * 将question的所有属性放到QuestionDTO中最古老的方法
            * new QuestionDTO().setId(question.getId())
            * *//*
            //使用Spring内置的方法
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }*/


        return questionDTOList;
    }


    public List<QuestionDTO> listById(Integer userId, Integer page, Integer size) {
        PageHelper.startPage(page,size);
        QuestionExample example = new QuestionExample();
        example.createCriteria()
                .andIdEqualTo(userId);
        questionMapper.selectByExample(example);
        List<Question> questions = questionMapper.selectByExample(example);
        List<QuestionDTO> profileDTOList = new ArrayList<>();
        PageInfo<Question> profilePageInfo = new PageInfo<>(questions,5);

        for (Question question : questions) {
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);

            questionDTO.setNavigatepageNums(profilePageInfo.getNavigatepageNums());
            questionDTO.setPageNum(profilePageInfo.getPageNum());
            questionDTO.setPageSize(profilePageInfo.getPageSize());
            questionDTO.setPages(profilePageInfo.getPages());
            questionDTO.setHasPreviousPage(profilePageInfo.isHasPreviousPage());
            questionDTO.setHasNextPage(profilePageInfo.isHasNextPage());
            profileDTOList.add(questionDTO);
            System.out.println(profileDTOList);

        }


        return profileDTOList;
    }


    public QuestionDTO getById(Integer id) {
        Question question = questionMapper.selectByPrimaryKey(id);
        if (question == null) {
            throw  new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        User user = userMapper.selectByPrimaryKey(question.getCreator());
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question,questionDTO);
        questionDTO.setUser(user);

        return questionDTO;
    }

    public void createOrUpdate(Question question) {
        if (question.getId() == null) {
            //创建
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            questionMapper.insert(question);
        }else{
            //更新
            question.setGmtModified(System.currentTimeMillis());
            Question updateQuestion = new Question();
            updateQuestion.setGmtModified(System.currentTimeMillis());
            updateQuestion.setTitle(question.getTitle());
            updateQuestion.setDescription(question.getDescription());
            updateQuestion.setTag(question.getTag());

            QuestionExample example = new QuestionExample();
            example.createCriteria()
                    .andIdEqualTo(question.getId());
            int i = questionMapper.updateByExampleSelective(updateQuestion, example);
            if (i != 1) {
                throw  new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
        }
    }

    public void incView(Integer id) {
        Question question = new Question();
        question.setId(id);
        question.setViewCount(1);
        questionExtMapper.incView(question);
    }
}

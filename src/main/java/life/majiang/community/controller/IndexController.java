package life.majiang.community.controller;

import life.majiang.community.dto.QuestionDTO;
import life.majiang.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class IndexController {
    @Autowired(required=true)
    private QuestionService questionService;

    @GetMapping("/")
    public String index(
                        Model model,
                        @RequestParam(name = "page",defaultValue = "1") Integer page,
                        @RequestParam(name = "size",defaultValue = "5") Integer size){

        List<QuestionDTO> questionList = questionService.list(page, size);


        for (QuestionDTO questionDTO : questionList) {
            model.addAttribute("navigatepageNums",questionDTO.getNavigatepageNums());
            model.addAttribute("PageNum",questionDTO.getPageNum());
            model.addAttribute("hasPreviousPage",questionDTO.getHasPreviousPage());
            model.addAttribute("hasNextPage",questionDTO.getHasNextPage());
            model.addAttribute("pages",questionDTO.getPages());
        }
        model.addAttribute("questionList",questionList);
        return "index";
    }
}

package life.majiang.community.controller;

import life.majiang.community.dto.QuestionDTO;
import life.majiang.community.model.User;
import life.majiang.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class ProfileController {
    @Autowired(required=true)
    private QuestionService questionService;

    @GetMapping("/profile/{action}")
    public String profile(HttpServletRequest request,
                          Model model,
                          @PathVariable(name = "action")String action,
                          @RequestParam(name = "page",defaultValue = "1") Integer page,
                          @RequestParam(name = "size",defaultValue = "5") Integer size){

        User user = (User)request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }
        if ("questions".equals(action)){
            model.addAttribute("section","questions");
            model.addAttribute("sectionName","我的提问");
        }else if("replies".equals(action)){
            model.addAttribute("section","replies");
            model.addAttribute("sectionName","最新回复");
        }
        List<QuestionDTO> profileList =questionService.listById(user.getId(),page,size);
        for (QuestionDTO questionDTO : profileList) {
            model.addAttribute("navigatepageNums",questionDTO.getNavigatepageNums());
            model.addAttribute("PageNum",questionDTO.getPageNum());
            model.addAttribute("hasPreviousPage",questionDTO.getHasPreviousPage());
            model.addAttribute("hasNextPage",questionDTO.getHasNextPage());
            model.addAttribute("pages",questionDTO.getPages());
        }
        model.addAttribute("profileList",profileList);
        return "profile";
    }
}

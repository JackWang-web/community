package life.majiang.community.controller;

import life.majiang.community.dto.AccessTokenDTO;
import life.majiang.community.dto.GithubUser;
import life.majiang.community.dto.QQUser;
import life.majiang.community.model.User;
import life.majiang.community.provider.GithubProvider;
import life.majiang.community.provider.QQProvider;
import life.majiang.community.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
@Slf4j
public class AuthorizeController {

    @Autowired
    private GithubProvider githubProvider;

    @Autowired
    private QQProvider qqProvider;



    @Value("${github.client.id}")
    private String clientId;
    @Value("${github.client.secret}")
    private String secret;
    @Value("${github.redirect.uri}")
    private String redirectUri;

    @Autowired
    private UserService userService;

    private AccessTokenDTO accessTokenDTO = new AccessTokenDTO();

    //Github授权
    @GetMapping("/callback")
    public String callback(@RequestParam(name="code")String code,
                           @RequestParam(name="state")String state,
                            HttpServletResponse response){
        setAccessTokenDto(code, state, clientId, secret, redirectUri);
        //获取access_token
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        //根据accessToken获取用户信息
        GithubUser githubUser = githubProvider.getUser(accessToken);
        //判断用户信息是否为空
        if (githubUser != null && githubUser.getId() != null) {
            //随机生成令牌
            String token = UUID.randomUUID().toString();
            //设置用户信息
            setUserInfo(token, githubUser.getName(), githubUser.getAvatarUrl(), "Github-"+githubUser.getId(), githubUser.getBio());
            //将令牌添加到Cookie
            addCookieForToken(response, token);
            return "redirect:/";
        }else{
            log.error("GitHub callback get error,{}",githubUser);
            //登录失败
            return "redirect:/";
        }
    }
    //QQ回调地址
    @GetMapping("/callbackQQ")
    public String callbackQQ(@RequestParam(name="code")String code,
                           @RequestParam(name="state")String state,
                           HttpServletResponse response){
        String accessToken = qqProvider.getAccessToken(code);
        String openId = qqProvider.getOpenId(accessToken);
        QQUser qqUser = qqProvider.getQQUser(accessToken, openId);
        if (qqUser != null && qqUser.getRet() == 0) {
            String token = UUID.randomUUID().toString();
            setUserInfo(token, qqUser.getNickname(), qqUser.getFigureurl_qq_2(), "QQ-"+openId, null);
            addCookieForToken(response, token);
            //返回首页
            return "redirect:/";
        } else {
            log.error("QQ callback get error");
            //登录失败
            return "redirect:/";
        }

    }

    private void setAccessTokenDto(String code, String state, String client_id, String client_secret, String redirect_url) {
        accessTokenDTO.setClient_id(client_id);
        accessTokenDTO.setClient_secret(client_secret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_url(redirect_url);
        accessTokenDTO.setState(state);
    }

    //将token添加到cookie
    private void addCookieForToken(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie("token", token);
        cookie.setMaxAge(60 * 60 * 24 * 7);//7天有效期
        response.addCookie(cookie);
    }

    //设置用户信息
    private void setUserInfo(String token, String name, String avatarUrl, String accountId, String bio){
        User user = new User();
        user.setToken(token);
        user.setName(name);
        user.setAvatarUrl(avatarUrl);
        user.setAccountId(accountId);
        user.setGmtCreate(System.currentTimeMillis());
        user.setGmtModified(user.getGmtCreate());
        user.setBio(bio);
        userService.createOrUpdate(user);
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request,
                         HttpServletResponse response){
        request.getSession().removeAttribute("user");
        Cookie cookie = new Cookie("token", null);
        cookie.setMaxAge(0);//立马失效
        response.addCookie(cookie);
        return "redirect:/";
    }
}

package life.majiang.community.provider;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import life.majiang.community.dto.QQUser;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class QQProvider {
    @Value("${qq.client.id}")
    private String qqClientId;
    @Value("${qq.client.secret}")
    private String qqClientSecret;
    @Value("${qq.redirect.uri}")
    private String qqRedirectUri;

    private OkHttpClient client = new OkHttpClient();

    /**
     * 获取AccessToken
     */
    public String getAccessToken(String code) {
        String url = "https://graph.qq.com/oauth2.0/token?grant_type=authorization_code&client_id="+qqClientId+"&client_secret="+qqClientSecret+"&code="+code+"&redirect_uri="+qqRedirectUri;
        Request request = new Request.Builder()
                .url(url)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String str = response.body().string();
            String res = str.split("&")[0].split("=")[1];
            System.out.println(res);
            return res;
        } catch (Exception e) {
            log.error("获取access_token失败");
        }
        return null;
    }

    /**
     * 获取OpenId
     */
    public String getOpenId(String access_token) {
        String url = "https://graph.qq.com/oauth2.0/me?access_token=" + access_token;
        Request request = new Request.Builder()
                .url(url)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String str = response.body().string().split(" ")[1];
            return JSONObject.parseObject(str).getString("openid");
        } catch (Exception e) {
            log.error("获取OpenId失败");
        }
        return null;
    }

    /**
     * 根据access_token获取用户信息
     */
    public QQUser getQQUser(String access_token, String openId) {
        String url = "https://graph.qq.com/user/get_user_info?access_token=" + access_token + "&oauth_consumer_key=" + qqClientId + "&openid=" + openId;
        Request request = new Request.Builder()
                .url(url)
                .build();
        try (Response response = client.newCall(request).execute()) {
            //得到的是json字符串，因此需要转为QQUser对象
            String str = response.body().string();
            return JSON.parseObject(str, QQUser.class);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("获取QQUser错误");
        }
        return null;
    }
}


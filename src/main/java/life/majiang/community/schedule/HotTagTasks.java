package life.majiang.community.schedule;

import life.majiang.community.cache.HotTagCache;
import life.majiang.community.dto.QuestionDTO;
import life.majiang.community.service.QuestionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 热门标签定时器,此处是测试定时任务（每隔5秒打印日志）
 * 注意：需在main方法的那个类上添加@EnableScheduling注解
 */
@Component
@Slf4j
public class HotTagTasks {
    private List<QuestionDTO>  list = null;
    @Autowired
    private QuestionService questionService;

    @Autowired
    private HotTagCache hotTagCache;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    @Scheduled(fixedRate = 1000*60*60*4)
    //@Scheduled(cron = "0 0 1 * * *")//每天凌晨1点执行

    public void hotTagSchedule() {

        int offset = 0;
        int limit = 20;
        String search=null;
        log.info("hotTagSchedule start {}", dateFormat.format(new Date()));
        HashMap<String, Integer> priorities = new HashMap<>();
        while (offset ==0 || list.size()==limit){
            list = questionService.list(search, search, offset, limit);
            for (QuestionDTO questionDTO : list) {
                String tag1 = questionDTO.getTag();
                String[] tags = StringUtils.split(tag1, "，");
                for (String tag : tags) {
                    Integer priority = priorities.get(tag);
                    if (priority != null) {
                        priorities.put(tag,priority+5+questionDTO.getCommentCount());
                    }else{
                        priorities.put(tag,5+questionDTO.getCommentCount());
                    }
                }
                //log.info("list question :{}",questionDTO.getTag());
            }
            offset +=limit;
        }
        priorities.forEach(
                (k,v) -> {
                    System.out.print(k);
                    System.out.print(":");
                    System.out.print(v);
                    System.out.println();
                }
        );
        hotTagCache.updateTags(priorities);
        log.info("hotTagSchedule stop {}", dateFormat.format(new Date()));
    }
}

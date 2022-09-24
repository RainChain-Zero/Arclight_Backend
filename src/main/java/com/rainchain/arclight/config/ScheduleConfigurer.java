package com.rainchain.arclight.config;

import com.rainchain.arclight.mapper.ScheduleMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;

@Slf4j
@Configuration
@EnableScheduling
public class ScheduleConfigurer {
    @Autowired
    private ScheduleMapper cleanMapper;

    @Scheduled(cron = "0 0 4 * * ?")
    public void cleanGames() {
        long timeNow = new Date().getTime() / 1000;
        //满人且不收ob的团，过期7天后从即时团本中删除
        cleanMapper.cleanGames(timeNow - 7 * 24 * 60 * 60L);
        //清理过期且已被处理的的申请
        cleanMapper.cleanApplication(timeNow - 7 * 24 * 60 * 60L);
    }

}

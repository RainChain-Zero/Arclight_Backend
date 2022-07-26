package com.rainchain.arclight.config;

import com.rainchain.arclight.mapper.ScheduleMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@Configuration
@EnableScheduling
public class ScheduleConfigurer {
    @Autowired
    private ScheduleMapper cleanMapper;

    @Scheduled(cron = "0 0 4 * * ?")
    public void cleanGames() {
        //过期超过一天的团从即时团本中删除
        cleanMapper.cleanGames(new SimpleDateFormat("yyyy-MM-dd").format(new Date().getTime() - 3 * 24 * 60 * 60 * 1000L));
    }

}

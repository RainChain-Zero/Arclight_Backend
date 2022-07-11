package com.rainchain.arclight.config;

import com.rainchain.arclight.mapper.CleanMapper;
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
public class CleanConfigurer {
    @Autowired
    private CleanMapper cleanMapper;

    @Scheduled(cron = "0 0 4 * * ?")
    public void cleanGames(){
        cleanMapper.cleanGames(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
    }

}

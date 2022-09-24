package com.rainchain.arclight.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ScheduleMapper {
    void cleanGames(Long timeDes);

    void cleanApplication(Long timeDes);
}

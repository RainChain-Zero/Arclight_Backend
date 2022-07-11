package com.rainchain.arclight.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CleanMapper {
    void cleanGames(String timeNow);
}

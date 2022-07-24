package com.rainchain.arclight.mapper;

import com.rainchain.arclight.entity.Game;
import com.rainchain.arclight.entity.SearchCondition;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserMapper {
    List<Game> searchIdGame(Long id);

    List<Game> searchGames(SearchCondition searchCondition);
}

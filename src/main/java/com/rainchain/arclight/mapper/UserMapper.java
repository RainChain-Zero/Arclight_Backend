package com.rainchain.arclight.mapper;

import com.rainchain.arclight.component.SearchCondition;
import com.rainchain.arclight.entity.Game;
import com.rainchain.arclight.entity.JoinOrQuitInfoDB;
import com.rainchain.arclight.entity.ParticipatingGames;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserMapper {
    List<Game> searchIdGame(Long id);

    List<Game> searchGames(SearchCondition searchCondition);

    //加入或退出团
    void joinOrQuitGames(JoinOrQuitInfoDB joinOrQuitInfoDB);

    List<ParticipatingGames> getParticipatingGames(String qq);
}

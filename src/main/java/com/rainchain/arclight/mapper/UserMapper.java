package com.rainchain.arclight.mapper;

import com.rainchain.arclight.component.Player;
import com.rainchain.arclight.component.SearchCondition;
import com.rainchain.arclight.entity.Game;
import com.rainchain.arclight.entity.KpApproval;
import com.rainchain.arclight.entity.ParticipatingGames;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserMapper {
    Game searchIdGame(Long id);

    //通过id列表搜索团本
    List<Game> searchIdsGames(List<Long> ids);

    List<Game> searchGames(SearchCondition searchCondition);

    List<ParticipatingGames> getParticipatingGames(String qq);

    void joinGames(@Param("kpApproval") KpApproval kpApproval, @Param("title") String title);

    void quitGamesNow(@Param("id") Long id, @Param("players") List<Player> players);

    void quitGamesApplication(@Param("ids") List<Long> ids, @Param("qq") String qq);
}

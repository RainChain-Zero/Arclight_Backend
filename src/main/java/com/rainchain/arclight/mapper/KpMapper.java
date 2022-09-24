package com.rainchain.arclight.mapper;


import com.rainchain.arclight.component.Player;
import com.rainchain.arclight.entity.AuditResult;
import com.rainchain.arclight.entity.Game;
import com.rainchain.arclight.entity.KpApproval;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface KpMapper {
    int nameCheck(Game game);

    void addGame(Game game);

    void updateGame(Game game);

    int deleteGame(Long id, String qq);

    void addDeleteInfo(Long id, String info);

    void removePlayers(@Param("id") Long id, @Param("players") List<Player> players);

    List<KpApproval> getPlApplication(@Param("id") Long id, @Param("qqs") List<String> qqs);

    void acceptPlayers(@Param("id") Long id, @Param("qqs") List<String> qqs, @Param("playersDb") List<Player> playersDb,
                       @Param("timestamp") Long timestamp);

    void refusePlayers(@Param("id") Long id, @Param("qqs") List<String> qqs, @Param("timestamp") Long timestamp);

    void addIrregularGame(AuditResult auditResult);
}

package com.rainchain.arclight.mapper;


import com.rainchain.arclight.entity.AuditResult;
import com.rainchain.arclight.entity.Game;
import com.rainchain.arclight.entity.InviteOrRemoveInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface KpMapper {
    int nameCheck(Game game);

    void addGame(Game game);

    void updateGame(Game game);

    int deleteGame(Long id, String qq);

    void addDeleteInfo(Long id, String info);

    void inviteOrRemovePlayers(InviteOrRemoveInfo inviteOrRemoveInfo);

    void addIrregularGame(AuditResult auditResult);
}

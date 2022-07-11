package com.rainchain.arclight.mapper;


import com.rainchain.arclight.component.DeleteInfo;
import com.rainchain.arclight.entity.AuditResult;
import com.rainchain.arclight.entity.Game;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface KpMapper {
    int nameCheck(Game game);

    void addGame(Game game);

    void updateGame(Game game);

    int deleteGame(DeleteInfo deleteInfo);

    void addDeleteInfo(DeleteInfo deleteInfo);

    void addIrregularGame(AuditResult auditResult);
}

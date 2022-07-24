package com.rainchain.arclight.service;

import com.rainchain.arclight.component.DeleteInfo;
import com.rainchain.arclight.entity.AuditResult;
import com.rainchain.arclight.entity.Game;
import com.rainchain.arclight.exception.OperationFailException;
import com.rainchain.arclight.mapper.KpMapper;
import com.rainchain.arclight.mapper.UserMapper;
import com.rainchain.arclight.utils.TimeUtils;
import com.rainchain.arclight.utils.VerifyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class KpService {
    @Autowired
    private KpMapper kpMapper;
    @Autowired
    private UserMapper userMapper;

    public Long addGame(Game game) {
        //统一持续时间单位
        game.setLast_timeh(TimeUtils.convertToTimeH(game.getLast_time()));
        if (kpMapper.nameCheck(game) > 0) {
            throw new OperationFailException("你已经有同名的团了");
        }
        kpMapper.addGame(game);
        return game.getId();
    }

    public List<Game> updateGame(Game gameOld, Game gameNew) {

        kpMapper.updateGame(gameNew);

        List<Game> gameList = new ArrayList<>();
        gameList.add(gameOld);
        gameList.add(gameNew);
        return gameList;
    }

    public List<Boolean> deleteGame(DeleteInfo deleteInfo, String key) {
        String qq = deleteInfo.getQq();
        String timeNow = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String botQq = VerifyUtils.decodeQq(key);
        deleteInfo.setInfo(qq + "在" + timeNow + "通过" + botQq + "删除");

        List<Boolean> res = new ArrayList<>();
        //批量删除
        deleteInfo.getId().forEach(id -> {
            if (kpMapper.deleteGame(id, qq) == 0) {
                res.add(false);
            } else {
                kpMapper.addDeleteInfo(id, deleteInfo.getInfo());
                res.add(true);
            }
        });
        return res;
    }

    public void addIrregularGame(AuditResult auditResult) {
        kpMapper.addIrregularGame(auditResult);
    }
}

package com.rainchain.arclight.service;

import com.rainchain.arclight.component.DeleteInfo;
import com.rainchain.arclight.entity.AuditResult;
import com.rainchain.arclight.entity.Game;
import com.rainchain.arclight.exception.OperationFailException;
import com.rainchain.arclight.mapper.KpMapper;
import com.rainchain.arclight.mapper.UserMapper;
import com.rainchain.arclight.utils.*;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
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

    public int addGame(Game game) {
        //统一持续时间单位
        game.setLast_timeh(TimeUtils.convertToTimeH(game.getLast_time()));
        if (kpMapper.nameCheck(game) > 0) {
            throw new OperationFailException("你已经有同名的团了");
        }
        kpMapper.addGame(game);
        return game.getId();
    }

    public List<Game> updateGame(Game gameOld,Game gameNew) throws TencentCloudSDKException {

        kpMapper.updateGame(gameNew);

        List<Game> gameList = new ArrayList<>();
        gameList.add(gameOld);
        gameList.add(gameNew);
        return gameList;
    }

    public void deleteGame(DeleteInfo deleteInfo, String key) {
        String qq = deleteInfo.getQq();
        String timeNow = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        String botQq = VerifyUtils.decodeQq(key);

        deleteInfo.setInfo(qq + "在" + timeNow + "通过" + botQq + "删除");
        if (kpMapper.deleteGame(deleteInfo) == 0) {
            throw new OperationFailException("删除失败！没有对应id的团或指定团不属于你");
        }
        kpMapper.addDeleteInfo(deleteInfo);

        //todo 缓存删除数据

    }

    public void addIrregularGame(AuditResult auditResult) {
        kpMapper.addIrregularGame(auditResult);
    }
}

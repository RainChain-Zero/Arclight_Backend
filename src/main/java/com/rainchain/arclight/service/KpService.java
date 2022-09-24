package com.rainchain.arclight.service;

import cn.hutool.core.collection.CollUtil;
import com.rainchain.arclight.component.AcceptOrRefuseInfo;
import com.rainchain.arclight.component.DeleteInfo;
import com.rainchain.arclight.component.Player;
import com.rainchain.arclight.component.RemoveInfo;
import com.rainchain.arclight.entity.AuditResult;
import com.rainchain.arclight.entity.Game;
import com.rainchain.arclight.entity.KpApproval;
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

    //从已正式加入的玩家中移除
    public void removePlayers(RemoveInfo RemoveInfo) {
        //待移除的玩家列表
        List<String> qqs = RemoveInfo.getQqs();
        //查询id对应的团
        Game game = userMapper.searchIdGame(RemoveInfo.getId());
        if (null == game) {
            throw new OperationFailException("找不到指定id的团");
        }
        if (!game.getKp_qq().equals(RemoveInfo.getKp_qq())) {
            throw new OperationFailException("你不是该团的主持人");
        }
        //当前玩家列表
        List<Player> playerList = game.getPlayers();

        //移除玩家
        playerList.removeIf(player -> qqs.contains(player.getQq()));

        kpMapper.removePlayers(RemoveInfo.getId(), playerList);
    }

    public List<Boolean> acceptPlayers(AcceptOrRefuseInfo acceptOrRefuseInfo) {
        //返回结果
        List<Boolean> res = new ArrayList<>();
        //待接受的玩家QQ列表
        List<String> qqs = acceptOrRefuseInfo.getQqs();
        //待处理的团本
        Game game = userMapper.searchIdGame(acceptOrRefuseInfo.getId());
        if (null == game) {
            throw new OperationFailException("找不到指定id的团");
        }
        if (!game.getKp_qq().equals(acceptOrRefuseInfo.getKp_qq())) {
            throw new OperationFailException("你不是该团的主持人");
        }
        //新玩家列表
        List<Player> playersNew = new ArrayList<>();
        //获取申请的玩家列表
        List<KpApproval> kpApprovals = kpMapper.getPlApplication(acceptOrRefuseInfo.getId(), qqs);
        qqs.forEach(qq -> {
            KpApproval kpApproval = new KpApproval();
            kpApproval.setQq(qq);
            kpApproval.setId(acceptOrRefuseInfo.getId());
            int index = kpApprovals.indexOf(kpApproval);
            //若玩家不在申请列表中
            if (-1 == index) {
                res.add(false);
                return;
            }
            kpApproval = kpApprovals.get(index);
            Player playerNew = new Player(kpApproval.getNick(), qq);
            playersNew.add(playerNew);
            res.add(true);
        });
        //更新玩家列表,取并集
        List<Player> playersDb = new ArrayList<>(CollUtil.union(game.getPlayers(), playersNew));

        kpMapper.acceptPlayers(acceptOrRefuseInfo.getId(), qqs, playersDb, new Date().getTime() / 1000);

        return res;
    }

    public List<Boolean> refusePlayers(AcceptOrRefuseInfo acceptOrRefuseInfo) {
        //返回结果
        List<Boolean> res = new ArrayList<>();
        //待拒绝的玩家QQ列表
        List<String> qqs = acceptOrRefuseInfo.getQqs();
        //待处理的团本
        Game game = userMapper.searchIdGame(acceptOrRefuseInfo.getId());
        if (null == game) {
            throw new OperationFailException("找不到指定id的团");
        }
        if (!game.getKp_qq().equals(acceptOrRefuseInfo.getKp_qq())) {
            throw new OperationFailException("你不是该团的主持人");
        }
        //获取待申请的玩家列表
        List<KpApproval> kpApprovals = kpMapper.getPlApplication(acceptOrRefuseInfo.getId(), qqs);
        qqs.forEach(qq -> {
            //通过qq查找在待评审的列表中的玩家
            KpApproval kpApproval = new KpApproval();
            kpApproval.setQq(qq);
            kpApproval.setId(acceptOrRefuseInfo.getId());
            //若玩家不在申请列表中
            if (!CollUtil.contains(kpApprovals, kpApproval)) {
                res.add(false);
                return;
            }
            res.add(true);
        });

        kpMapper.refusePlayers(acceptOrRefuseInfo.getId(), qqs, new Date().getTime() / 1000);

        return res;
    }

    public void addIrregularGame(AuditResult auditResult) {
        kpMapper.addIrregularGame(auditResult);
    }
}

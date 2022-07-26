package com.rainchain.arclight.service;

import cn.hutool.core.collection.CollUtil;
import com.rainchain.arclight.component.JoinOrQuitInfo;
import com.rainchain.arclight.component.Player;
import com.rainchain.arclight.component.SearchCondition;
import com.rainchain.arclight.entity.Game;
import com.rainchain.arclight.entity.JoinOrQuitInfoDB;
import com.rainchain.arclight.entity.ParticipatingGames;
import com.rainchain.arclight.mapper.UserMapper;
import com.rainchain.arclight.utils.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public List<Game> searchIdGame(Long id) {
        return userMapper.searchIdGame(id);
    }

    public List<Game> searchGames(SearchCondition searchCondition) {
        //模糊匹配团名
        if (searchCondition.getTitle() != null) {
            searchCondition.setTitle("%" + searchCondition.getTitle() + "%");
        }
        //将团持续时间统一成小时制
        String lastTime = searchCondition.getLast_time();
        if (searchCondition.getLast_time() != null) {
            searchCondition.setLast_timeh(TimeUtils.convertToTimeH(lastTime));
        }

        //模糊匹配tags和group
        return userMapper.searchGames(SearchCondition.tagsAndGroupSearcher(searchCondition));
    }

    public List<Boolean> joinGames(JoinOrQuitInfo joinOrQuitInfo) {
        Player player = joinOrQuitInfo.getPlayer();
        String playerQQ = player.getQq();
        List<Boolean> res = new ArrayList<>();
        JoinOrQuitInfoDB joinOrQuitInfoDB = new JoinOrQuitInfoDB();
        //去重
        //对每个id单独判断是否加入成功
        for (Long id : joinOrQuitInfo.getId()) {
            joinOrQuitInfoDB.setId(id);
            //先提取id对应的团数据
            List<Game> games = userMapper.searchIdGame(id);
            if (CollUtil.isEmpty(games)) {
                res.add(false);
                continue;
            }
            //满人的团无法加入
            if (games.get(0).isIsfull()) {
                res.add(false);
                continue;
            }
            //提取对应团原先的玩家列表
            //todo 重写Player equals方法以采用CollUtil实现
            List<Player> playerList = games.get(0).getPlayers();
            if (!CollUtil.isEmpty(playerList)) {
                //不可重复添加
                boolean flag = true;
                for (Player playerOri : playerList) {
                    if (playerQQ.equals(playerOri.getQq())) {
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    playerList.add(player);
                    joinOrQuitInfoDB.setPlayers(playerList);
                    userMapper.joinOrQuitGames(joinOrQuitInfoDB);
                    res.add(true);
                } else {
                    res.add(false);
                }
            } else {
                playerList = new ArrayList<Player>() {{
                    add(player);
                }};
                joinOrQuitInfoDB.setPlayers(playerList);
                userMapper.joinOrQuitGames(joinOrQuitInfoDB);
                res.add(true);
            }
        }
        return res;
    }

    //退出团
    public List<Boolean> quitGames(JoinOrQuitInfo joinOrQuitInfo) {
        String playerQQ = joinOrQuitInfo.getPlayer().getQq();
        List<Boolean> res = new ArrayList<>();

        JoinOrQuitInfoDB joinOrQuitInfoDB = new JoinOrQuitInfoDB();
        for (Long id : joinOrQuitInfo.getId()) {
            joinOrQuitInfoDB.setId(id);
            List<Game> games = userMapper.searchIdGame(id);
            if (CollUtil.isEmpty(games)) {
                res.add(false);
                continue;
            }
            //提取对应团原先的玩家列表
            List<Player> playerList = games.get(0).getPlayers();
            if (CollUtil.isEmpty(playerList)) {
                res.add(false);
                continue;
            }
            int length = playerList.size();
            //从玩家列表中删除对应玩家
            playerList = CollUtil.filter(playerList, player -> !player.getQq().equals(playerQQ));
            if (playerList.size() < length) {
                joinOrQuitInfoDB.setPlayers(playerList);
                userMapper.joinOrQuitGames(joinOrQuitInfoDB);
                res.add(true);
            } else {
                res.add(false);
            }
        }
        return res;
    }

    public List<ParticipatingGames> getParticipatingGames(String qq) {
        return userMapper.getParticipatingGames(qq);
    }
}

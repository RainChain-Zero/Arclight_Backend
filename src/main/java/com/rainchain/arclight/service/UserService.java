package com.rainchain.arclight.service;

import cn.hutool.core.collection.CollUtil;
import com.rainchain.arclight.component.JoinOrQuitInfo;
import com.rainchain.arclight.component.Player;
import com.rainchain.arclight.component.SearchCondition;
import com.rainchain.arclight.entity.Game;
import com.rainchain.arclight.entity.KpApproval;
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

    public Game searchIdGame(Long id) {
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

    //申请加入团
    public List<Boolean> joinGames(JoinOrQuitInfo joinOrQuitInfo) {
        Player player = joinOrQuitInfo.getPlayer();
        String playerQQ = player.getQq();
        List<Long> ids = joinOrQuitInfo.getIds();
        List<Boolean> res = new ArrayList<>();
        //获取所有游戏列表
        List<Game> games = userMapper.searchIdsGames(ids);
        //对每个id单独判断是否加入成功
        for (Long id : ids) {
            //先提取id对应的团数据
            Game game = new Game();
            game.setId(id);
            int index = games.indexOf(game);
            //如果没有这个团，加入失败
            if (-1 == index) {
                res.add(false);
                continue;
            }
            game = games.get(index);
            //满人的团无法加入
            //todo 讨论加入ob如何判断
            if (game.isIsfull()) {
                res.add(false);
                continue;
            }
            KpApproval kpApproval = new KpApproval(id, playerQQ, game.getKp_qq(), player.getNick(), joinOrQuitInfo.getMsg());
            //待加入团的标题
            String title = game.getTitle();
            //写入数据库
            userMapper.joinGames(kpApproval, title);
            res.add(true);
        }
        return res;
    }

    //退出团,一并退出申请列表和已经通过的玩家列表
    public List<Boolean> quitGames(JoinOrQuitInfo joinOrQuitInfo) {
        String playerQQ = joinOrQuitInfo.getPlayer().getQq();
        List<Boolean> res = new ArrayList<>();
        List<Long> ids = joinOrQuitInfo.getIds();

        List<Game> games = userMapper.searchIdsGames(ids);
        for (Long id : ids) {
            Game game = new Game();
            game.setId(id);
            int index = games.indexOf(game);
            if (-1 == index) {
                res.add(false);
                continue;
            }
            game = games.get(index);
            //提取对应团原先的玩家列表
            List<Player> playerList = game.getPlayers();
            //从现有玩家列表中删除该玩家
            CollUtil.removeAny(playerList, new Player(null, playerQQ));
            //更新数据库
            userMapper.quitGamesNow(id, playerList);
            res.add(true);
        }
        //移除在申请列表中的玩家
        userMapper.quitGamesApplication(ids, playerQQ);
        return res;
    }

    public List<ParticipatingGames> getParticipatingGames(String qq) {
        return userMapper.getParticipatingGames(qq);
    }
}

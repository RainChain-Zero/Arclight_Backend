package com.rainchain.arclight.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.rainchain.arclight.component.DeleteInfo;
import com.rainchain.arclight.entity.Game;
import com.rainchain.arclight.entity.InviteOrRemoveInfo;
import com.rainchain.arclight.exception.OperationFailException;
import com.rainchain.arclight.service.KpService;
import com.rainchain.arclight.service.UserService;
import com.rainchain.arclight.utils.EncodingUtils;
import com.rainchain.arclight.utils.VerifyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;


@RestController
public class KpController {
    @Autowired
    private KpService kpService;
    @Autowired
    private UserService userService;

    @PostMapping("/add")
    public Long addGame(HttpServletRequest request) throws IOException {
        String content = EncodingUtils.charReader(request);
        Game game = VerifyUtils.verifyGame(JSON.parseObject(content, Game.class));
        VerifyUtils.qqVerify(game.getKp_qq());
        return kpService.addGame(game);
    }

    @PostMapping("/update")
    public List<Game> updateGame(HttpServletRequest request) throws IOException {
        String content = EncodingUtils.charReader(request);
        Game game = JSON.parseObject(content, Game.class);
        if (game.getId() == null) {
            throw new OperationFailException("id字段为空");
        }
        List<Game> gameSearch = userService.searchIdGame(game.getId());
        if (CollUtil.isEmpty(gameSearch)) {
            throw new OperationFailException("找不到对应id的团");
        }
        Game gameOld = gameSearch.get(0);

        String kp_qq = game.getKp_qq();
        if (StrUtil.isBlank(kp_qq) || gameOld.getKp_qq().compareTo(kp_qq) != 0) {
            throw new OperationFailException("只有主持人才能修改该团信息");
        }
        Game gameNew = gameOld.updateGame(game);

        return kpService.updateGame(gameOld, gameNew);
    }

    @PostMapping("/delete")
    public List<Boolean> deleteGame(@RequestBody @Validated DeleteInfo deleteInfo, @RequestParam("api_key") String key) {
        VerifyUtils.qqVerify(deleteInfo.getQq());
        return kpService.deleteGame(deleteInfo, key);
    }

    @PostMapping("/invite")
    public List<Boolean> invitePlayers(HttpServletRequest request) throws IOException {
        String content = EncodingUtils.charReader(request);
        InviteOrRemoveInfo inviteOrRemoveInfo = JSON.parseObject(content, InviteOrRemoveInfo.class);
        VerifyUtils.verifyInviteOrRemoveInfo(inviteOrRemoveInfo);
        return kpService.invitePlayers(inviteOrRemoveInfo);
    }

    @PostMapping("/remove")
    public List<Boolean> removePlayers(@RequestBody @Validated InviteOrRemoveInfo inviteOrRemoveInfo) {
        VerifyUtils.verifyInviteOrRemoveInfo(inviteOrRemoveInfo);
        return kpService.removePlayers(inviteOrRemoveInfo);
    }
}

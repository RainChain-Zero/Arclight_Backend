package com.rainchain.arclight.controller;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.rainchain.arclight.component.AcceptOrRefuseInfo;
import com.rainchain.arclight.component.DeleteInfo;
import com.rainchain.arclight.component.RemoveInfo;
import com.rainchain.arclight.entity.Game;
import com.rainchain.arclight.entity.KpApproval;
import com.rainchain.arclight.exception.OperationFailException;
import com.rainchain.arclight.service.KpService;
import com.rainchain.arclight.service.UserService;
import com.rainchain.arclight.utils.EncodingUtils;
import com.rainchain.arclight.utils.VerifyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
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
        Game gameOld = userService.searchIdGame(game.getId());
        if (null == gameOld) {
            throw new OperationFailException("找不到对应id的团");
        }

        String kp_qq = game.getKp_qq();
        if (StrUtil.isBlank(kp_qq) || gameOld.getKp_qq().compareTo(kp_qq) != 0) {
            throw new OperationFailException("只有主持人才能修改该团信息");
        }
        Game gameNew = gameOld.updateGame(game);

        return kpService.updateGame(gameOld, gameNew);
    }

    @PostMapping("/delete")
    public void deleteGame(@RequestBody @Validated DeleteInfo deleteInfo, @RequestParam("api_key") String key) {
        VerifyUtils.qqVerify(deleteInfo.getKp_qq());
        kpService.deleteGame(deleteInfo, key);
    }

    @PostMapping("/remove")
    public void removePlayers(@RequestBody @Validated RemoveInfo RemoveInfo) {
        VerifyUtils.verifyRemoveInfo(RemoveInfo);
        kpService.removePlayers(RemoveInfo);
    }

    //接受pl的入团申请
    @PostMapping("/accept")
    public List<Boolean> acceptPlayers(@RequestBody @Valid AcceptOrRefuseInfo acceptOrRefuseInfo) {
        VerifyUtils.verifyAcceptOrRefuseInfo(acceptOrRefuseInfo);
        return kpService.acceptPlayers(acceptOrRefuseInfo);
    }

    @PostMapping("/refuse")
    public List<Boolean> refusePlayers(@RequestBody @Valid AcceptOrRefuseInfo acceptOrRefuseInfo) {
        VerifyUtils.verifyAcceptOrRefuseInfo(acceptOrRefuseInfo);
        return kpService.refusePlayers(acceptOrRefuseInfo);
    }

    @GetMapping("/getApproval")
    public List<KpApproval> getApproval(@RequestParam(value = "id", required = false) List<Long> ids, @RequestParam("kp_qq") String kp_qq) {
        VerifyUtils.qqVerify(kp_qq);
        return kpService.getApproval(ids, kp_qq);
    }
}

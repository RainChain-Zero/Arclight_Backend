package com.rainchain.arclight.controller;

import com.alibaba.fastjson.JSON;
import com.rainchain.arclight.component.DeleteInfo;
import com.rainchain.arclight.entity.Game;
import com.rainchain.arclight.exception.OperationFailException;
import com.rainchain.arclight.service.KpService;
import com.rainchain.arclight.service.UserService;
import com.rainchain.arclight.utils.EncodingUtils;
import com.rainchain.arclight.utils.TextModerationUtils;
import com.rainchain.arclight.utils.VerifyUtils;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
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
    public int addGame(HttpServletRequest request) throws TencentCloudSDKException, IOException {
        String content = EncodingUtils.charReader(request);

        Game game = VerifyUtils.verifyGame(JSON.parseObject(content, Game.class));
        VerifyUtils.qqVerify(game.getKp_qq());
        TextModerationUtils.auditText(game, kpService);
        return kpService.addGame(game);
    }

    @PostMapping("/update")
    public List<Game> updateGame(HttpServletRequest request) throws TencentCloudSDKException, IOException {
        String content = EncodingUtils.charReader(request);

        Game game = JSON.parseObject(content, Game.class);
        if (game.getId() == null) {
            throw new OperationFailException("id字段为空");
        }
        List<Game> gameSearch = userService.searchIdGame(game.getId());
        if (gameSearch == null || gameSearch.size() == 0) {
            throw new OperationFailException("找不到对应id的团！");
        }
        Game gameOld = gameSearch.get(0);
        if (gameOld.getKp_qq().compareTo(game.getKp_qq()) != 0) {
            throw new OperationFailException("只有主持人才能修改该团信息");
        }
        boolean isDesNull = gameOld.getDes() == null;
        Game gameNew = gameOld.updateGame(game);

        //修改的信息中描述部分非空才审核
        if (!isDesNull) {
            TextModerationUtils.auditText(gameNew, kpService);
        }
        return kpService.updateGame(gameOld, gameNew);
    }

    @PostMapping("/delete")
    public void deleteGame(@RequestBody @Validated DeleteInfo deleteInfo, @RequestParam("api_key") String key) {
        VerifyUtils.qqVerify(deleteInfo.getQq());
        kpService.deleteGame(deleteInfo, key);
    }
}

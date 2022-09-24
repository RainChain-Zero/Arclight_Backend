package com.rainchain.arclight.controller;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.rainchain.arclight.component.JoinInfo;
import com.rainchain.arclight.component.QuitInfo;
import com.rainchain.arclight.component.SearchCondition;
import com.rainchain.arclight.entity.Game;
import com.rainchain.arclight.entity.PlApplication;
import com.rainchain.arclight.service.UserService;
import com.rainchain.arclight.utils.EncodingUtils;
import com.rainchain.arclight.utils.VerifyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    //搜索团
    @GetMapping("/search")
    public List<Game> searchGames(@Validated SearchCondition searchCondition) {
        String qq = searchCondition.getKp_qq();
        //校验qq号
        if (!StrUtil.isBlank(qq)) {
            VerifyUtils.qqVerify(qq);
        }

        return userService.searchGames(searchCondition);
    }

    /*
     * 加入团
     * 如果返回false，可能为团已满人或者团不存在
     * */
    @PostMapping("/join")
    public List<Boolean> joinGames(HttpServletRequest request) throws IOException {
        String content = EncodingUtils.charReader(request);
        JoinInfo joinInfo = JSON.parseObject(content, JoinInfo.class);
        //参数校验
        VerifyUtils.verifyJoinInfo(joinInfo);

        return userService.joinGames(joinInfo);
    }

    //退出团
    @PostMapping("/quit")
    public List<Boolean> quitGames(@Validated @RequestBody QuitInfo quitInfo) {
        VerifyUtils.verifyQuitInfo(quitInfo);
        return userService.quitGames(quitInfo);
    }

    @GetMapping("/getApplication")
    public List<PlApplication> getApplication(@RequestParam("qq") String qq) {
        VerifyUtils.qqVerify(qq);
        return userService.getApplication(qq);
    }
}

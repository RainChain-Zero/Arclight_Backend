package com.rainchain.arclight.controller;

import com.alibaba.fastjson2.JSON;
import com.rainchain.arclight.component.JoinOrQuitInfo;
import com.rainchain.arclight.component.SearchCondition;
import com.rainchain.arclight.entity.Game;
import com.rainchain.arclight.exception.OperationFailException;
import com.rainchain.arclight.service.UserService;
import com.rainchain.arclight.utils.EncodingUtils;
import com.rainchain.arclight.utils.VerifyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
        if (qq != null) {
            VerifyUtils.qqVerify(qq);
        }
        //校验开团时间
        String start_time = searchCondition.getStart_time();
        if (start_time != null && searchCondition.getData_now().compareTo(start_time) > 0) {
            throw new OperationFailException("指定日期不能早于当日");
        }

        return userService.searchGames(searchCondition);
    }

    //加入团
    @PostMapping("/join")
    public List<Boolean> joinGames(HttpServletRequest request) throws IOException {
        String content = EncodingUtils.charReader(request);
        JoinOrQuitInfo joinOrQuitInfo = JSON.parseObject(content, JoinOrQuitInfo.class);

        //参数校验
        VerifyUtils.verifyJoinOrQuitInfo(joinOrQuitInfo);

        return userService.joinGames(joinOrQuitInfo);
    }

    //退出团
    @PostMapping("/quit")
    public List<Boolean> quitGames(@Validated @RequestBody JoinOrQuitInfo joinOrQuitInfo) {
        VerifyUtils.verifyJoinOrQuitInfo(joinOrQuitInfo);
        return userService.quitGames(joinOrQuitInfo);
    }
}

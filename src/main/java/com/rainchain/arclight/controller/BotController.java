package com.rainchain.arclight.controller;

import com.rainchain.arclight.entity.BotAccount;
import com.rainchain.arclight.exception.OperationFailException;
import com.rainchain.arclight.service.BotService;
import com.rainchain.arclight.utils.RedisUtils;
import com.rainchain.arclight.utils.VerifyUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
public class BotController {

    @Autowired
    private BotService botService;

    @Autowired
    private RedisUtils redisUtils;

    //注册，返回api_key
    @PostMapping("/register")
    public String signUp(@RequestBody @Validated BotAccount botAccount) {
        VerifyUtils.qqVerify(botAccount.getQq());

        if (redisUtils.hasBotQqCache(botAccount.getQq())){
            throw new OperationFailException("此QQ号已被注册！");
        }

        return botService.signUp(botAccount);
    }

    @GetMapping("/searchKey")
    public String searchKey(@Validated BotAccount botAccount){
        VerifyUtils.qqVerify(botAccount.getQq());

        return botService.searchKey(botAccount);
    }
}

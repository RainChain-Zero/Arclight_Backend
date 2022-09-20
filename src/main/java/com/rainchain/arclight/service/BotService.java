package com.rainchain.arclight.service;

import com.rainchain.arclight.entity.BotAccount;
import com.rainchain.arclight.exception.OperationFailException;
import com.rainchain.arclight.mapper.BotMapper;
import com.rainchain.arclight.utils.Base64Utils;
import com.rainchain.arclight.utils.DesUtils;
import com.rainchain.arclight.utils.VerifyUtils;
import com.rainchain.arclight.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BotService {
    @Autowired
    private BotMapper botMapper;

    @Autowired
    private RedisUtils redisUtils;

    //机器人注册
    public String signUp(BotAccount account) {

        String password = account.getPassword();
        //密码合法性检查
        VerifyUtils.passwordVerify(password);

        //密码加密
        String encryptedPassword = DesUtils.encryptBasedDes(password);

        //生成api_key
        String apiKey = DesUtils.encryptBasedDes(account.getQq() + "arclight" + password);
        //base64编码
        apiKey = Base64Utils.convertToUrlBase64(apiKey);
        botMapper.signUp(Long.valueOf(account.getQq()), encryptedPassword, apiKey);

        //缓存key和botQq
        redisUtils.setKeyCache(apiKey);
        redisUtils.setBotQqCache(account.getQq());

        return apiKey;
    }

    public String searchKey(BotAccount botAccount){
        String password = botAccount.getPassword();
        //密码合法性检查
        VerifyUtils.passwordVerify(password);

        //密码加密
        String encryptedPassword = DesUtils.encryptBasedDes(password);
        botAccount.setPassword(encryptedPassword);

        BotAccount botAccount1 = botMapper.searchKey(botAccount);

        if (botAccount1==null){
            throw new OperationFailException("qq和对应密码不匹配！");
        }
        return botAccount1.getApi_key();
    }
}
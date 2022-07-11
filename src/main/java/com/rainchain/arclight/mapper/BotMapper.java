package com.rainchain.arclight.mapper;

import com.rainchain.arclight.entity.BotAccount;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface BotMapper {

    void signUp(Long qq, String password, String api_key);

    BotAccount searchKey(BotAccount botAccount);
}

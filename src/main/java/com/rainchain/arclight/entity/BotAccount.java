package com.rainchain.arclight.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.stereotype.Component;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Component
public class BotAccount {
    @Length(min = 6, max = 10, message = "不合法的QQ号！")
    private String qq;

    @Length(min = 8, max = 16, message = "密码长度需要为8~16位")
    private String password;

    private String api_key;
}

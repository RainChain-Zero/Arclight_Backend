package com.rainchain.arclight.component;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Digits;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Component
public class DeleteInfo {
    @Digits(integer = 2147483647,fraction = 0,message = "id必须为非负整数")
    private int id;

    @Length(min=5,max = 10,message = "不合法的QQ号！")
    private String qq;

    private String info;
}

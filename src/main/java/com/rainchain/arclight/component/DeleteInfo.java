package com.rainchain.arclight.component;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.Digits;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Component
public class DeleteInfo {

    @Valid
    private List<@Digits(message = "id必须为非负整数", integer = 2147483647, fraction = 0) Long> id;

    @Length(min = 5, max = 10, message = "不合法的QQ号！")
    private String qq;

    private String info;
}

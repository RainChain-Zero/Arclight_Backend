package com.rainchain.arclight.component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.Digits;
import java.util.List;

/**
 * @author RainChain-Zero
 * @version 1.0
 * @date 2022-09-24 23:29
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class QuitInfo {
    @Valid
    private List<@Digits(message = "id必须为非负整数", integer = 2147483647, fraction = 0) Long> ids;

    @Length(min = 5, max = 10, message = "玩家QQ号非法！")
    private String qq;

}

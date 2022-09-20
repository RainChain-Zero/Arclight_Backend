package com.rainchain.arclight.component;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Component
public class Player {
    @NotNull
    private String nick;
    @Length(min = 5, max = 10, message = "玩家QQ号非法！")
    private String qq;
}

package com.rainchain.arclight.component;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.Digits;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class JoinInfo {
    @Valid
    private List<@Digits(message = "id必须为非负整数", integer = 2147483647, fraction = 0) Long> ids;

    @Valid
    private Player player;

    private String msg;

}

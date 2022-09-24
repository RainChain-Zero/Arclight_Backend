package com.rainchain.arclight.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.rainchain.arclight.component.Player;
import com.rainchain.arclight.mybatis.PlayersListTypeHandler;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.Digits;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(autoResultMap = true)
public class InviteOrRemoveInfo {
    @Digits(message = "id必须为非负整数", integer = 2147483647, fraction = 0)
    private Long id;

    @Length(min = 5, max = 10, message = "主持人QQ号非法！")
    private String kp_qq;

    @Valid
    @TableField(typeHandler = PlayersListTypeHandler.class)
    private List<Player> players;
}

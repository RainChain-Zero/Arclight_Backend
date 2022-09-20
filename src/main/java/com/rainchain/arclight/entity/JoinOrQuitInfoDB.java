package com.rainchain.arclight.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import com.rainchain.arclight.component.Player;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
@TableName(autoResultMap = true)
public class JoinOrQuitInfoDB {
    private Long id;

    @TableField(typeHandler = FastjsonTypeHandler.class)
    private List<Player> players;

}

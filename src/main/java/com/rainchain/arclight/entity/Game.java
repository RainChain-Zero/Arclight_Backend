package com.rainchain.arclight.entity;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.rainchain.arclight.component.Player;
import com.rainchain.arclight.mybatis.PlayersListTypeHandler;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Component
@JsonIgnoreProperties(value = {"update_time"})
@TableName(autoResultMap = true)
public class Game {
    @Null(message = "不可以对id参数赋值")
    private Long id;

    @NotBlank(message = "团名不能为空")
    private String title;

    @NotBlank(message = "主持人昵称不能为空")
    private String kp_name;

    @Length(min = 5, max = 10, message = "主持人QQ号非法！")
    private String kp_qq;

    @TableField(typeHandler = FastjsonTypeHandler.class)
    private List<String> groups = new ArrayList<>();

    @TableField(typeHandler = PlayersListTypeHandler.class)
    private List<Player> players = new ArrayList<>();

    @NotBlank(message = "开团时间不能为空")
    private String start_time;


    @Pattern(regexp = "\\d+[hd]", message = "团时长格式错误")
    private String last_time;

    @Null(message = "不可以对last_timeh参数赋值")
    private String last_timeh;

    @Range(min = 1, max = 30, message = "团最少人数超出范围")
    private Integer minper;

    @Range(min = 1, max = 30, message = "团最多人数超出范围")
    private Integer maxper;

    //默认为false
    private boolean isfull = false;

    @NotBlank(message = "团标签不能为空")
    private String tags;

    private String skills;

    private String tips;
    @NotBlank(message = "团描述不能为空")
    private String des;

    //时间戳
    private Long timestamp = new Date().getTime();

    private final String update_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

    public Game updateGame(Game gameNew) {
        //不能通过updata修改参团玩家
        gameNew.players = this.players;
        //对限定群groups去重
        gameNew.groups = CollUtil.distinct(gameNew.groups);
        return gameNew;
    }
}

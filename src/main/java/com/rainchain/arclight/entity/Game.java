package com.rainchain.arclight.entity;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.rainchain.arclight.component.Player;
import com.rainchain.arclight.mybatis.PlayersListTypeHandler;
import com.rainchain.arclight.utils.TimeUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(value = {"update_time", "timestamp"})
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
    //是否允许ob，默认为false
    private boolean ob = false;

    @NotBlank(message = "团标签不能为空")
    private String tags;

    private String skills;

    private String tips;
    @NotBlank(message = "团描述不能为空")
    private String des;

    //时间戳
    private Long timestamp = new Date().getTime() / 1000;

    private final String update_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

    //重写equals和hashcode方法，id相同则代表为同一个团
    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null || getClass() != that.getClass()) {
            return false;
        }
        Game game = (Game) that;
        return id.equals(game.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    //获取更新后的团本信息
    public Game updateGame(Game gameNew) {
        Game gameNow = new Game();
        gameNow.id = this.id;
        gameNow.title = gameNew.title == null ? this.title : gameNew.title;
        gameNow.kp_name = gameNew.kp_name == null ? this.kp_name : gameNew.kp_name;
        gameNow.kp_qq = gameNew.kp_qq == null ? this.kp_qq : gameNew.kp_qq;
        gameNow.groups = gameNew.groups == null ? this.groups : gameNew.groups;
        gameNow.start_time = gameNew.start_time == null ? this.start_time : gameNew.start_time;
        gameNow.last_time = gameNew.last_time == null ? this.last_time : gameNew.last_time;
        gameNow.last_timeh = TimeUtils.convertToTimeH(gameNow.last_time);
        gameNow.minper = gameNew.minper == null ? this.minper : gameNew.minper;
        gameNow.maxper = gameNew.maxper == null ? this.maxper : gameNew.maxper;
        gameNow.isfull = gameNew.isfull;
        gameNow.ob = gameNew.ob;
        gameNow.tags = gameNew.tags == null ? this.tags : gameNew.tags;
        gameNow.skills = gameNew.skills == null ? this.skills : gameNew.skills;
        gameNow.tips = gameNew.tips == null ? this.tips : gameNew.tips;
        gameNow.des = gameNew.des == null ? this.des : gameNew.des;
        //不能通过update修改参团玩家
        gameNow.players = this.players;
        //对限定群groups去重
        gameNow.groups = CollUtil.distinct(gameNow.groups);
        return gameNow;
    }
}

package com.rainchain.arclight.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.rainchain.arclight.component.Player;
import com.rainchain.arclight.exception.OperationFailException;
import com.rainchain.arclight.mybatis.PlayersListTypeHandler;
import com.rainchain.arclight.utils.TimeUtils;
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
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Component
@JsonIgnoreProperties(value = {"update_time", "restricted"})
@TableName(autoResultMap = true)
public class Game {
    //校验是否全为数字
    private static final java.util.regex.Pattern NUMBER_PATTERN = java.util.regex.Pattern.compile("-?\\d+(\\.\\d+)?");
    @Null(message = "不可以对id参数赋值")
    private Long id;

    @NotBlank(message = "团名不能为空")
    private String title;

    @NotBlank(message = "主持人昵称不能为空")
    private String kp_name;

    @Length(min = 5, max = 10, message = "主持人QQ号非法！")
    private String kp_qq;

    //parm
    private List<String> groups;
    //data
    private String restricted;

    @TableField(typeHandler = PlayersListTypeHandler.class)
    private List<Player> players;

    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "开团日期格式错误")
    private String start_time;

    @Pattern(regexp = "\\d+[hd]", message = "团时长格式错误")
    private String last_time;

    @Null(message = "不可以对last_timeh参数赋值")
    private String last_timeh;

    @Range(min = 1, max = 30, message = "团最少人数超出范围")
    private Integer minper;

    @Range(min = 1, max = 30, message = "团最多人数超出范围")
    private Integer maxper;

    private boolean isfull = false;

    @NotBlank(message = "团标签不能为空")
    private String tags;

    private String skills;

    private String tips;
    @NotBlank(message = "团描述不能为空")
    private String des;

    private final String update_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

    //从groups参数获得DB参数restricted
    public static Game getRestricted(Game game) {
        //校验限制群
        List<String> groups = game.getGroups();
        if (groups != null && groups.size() > 0) {
            StringBuilder restricted = new StringBuilder();
            groups.forEach(group -> {
                if (group.length() < 6 || group.length() > 10 || !NUMBER_PATTERN.matcher(group).matches()) {
                    throw new OperationFailException("QQ群号有误！");
                }
                restricted.append(group).append(",");
            });
            //字符串最后一个,用于后续查询时匹配，不可删去
            game.setRestricted(new String(restricted));
        }
        return game;
    }

    //从DB参数restricted获得groups参数
    public static Game getGroups(Game game) {
        game.setGroups(Arrays.asList(game.getRestricted().split(",")));
        return game;
    }

    //将tags转为小写

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
        gameNow.tags = gameNew.tags == null ? this.tags : gameNew.tags;
        gameNow.skills = gameNew.skills == null ? this.skills : gameNew.skills;
        gameNow.tips = gameNew.tips == null ? this.tips : gameNew.tips;
        gameNow.des = gameNew.des == null ? this.des : gameNew.des;
        //不能通过updata修改参团玩家
        gameNow.players = this.players;
        return Game.getRestricted(gameNow);
    }
}

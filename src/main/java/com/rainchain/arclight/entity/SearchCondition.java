package com.rainchain.arclight.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Pattern;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Component
public class SearchCondition {
    @Valid
    private List<@Digits(message = "id必须为非负整数", integer = 2147483647, fraction = 0) Long> id;

    @Range(min = 1, max = 100, message = "一次返回数量在1~100")
    private Integer maxnum = 100;

    @Length(min = 5, max = 10, message = "不合法的QQ号！")
    private String kp_qq;

    private List<String> groups;
    
    private String title;

    private String data_now = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "开团日期格式错误")
    //默认30天后
    private String start_time;

    @Pattern(regexp = "\\d{1,3}[hd]", message = "团时长格式错误")
    private String last_time;

    //小时制的持续时间
    private String last_timeh;

    @Range(min = 1, max = 30, message = "团最少人数超限")
    private Integer minper;

    @Range(min = 1, max = 30, message = "团最大人数超限")
    private Integer maxper;

}

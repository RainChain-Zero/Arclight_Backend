package com.rainchain.arclight.component;

import cn.hutool.core.collection.CollUtil;
import com.rainchain.arclight.utils.VerifyUtils;
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
import java.util.ArrayList;
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

    private String group;

    private String title;

    @Pattern(regexp = "\\d{1,3}[hd]", message = "团时长格式错误")
    private String last_time;

    //默认为false
    private boolean isfull = false;
    
    //小时制的持续时间
    private String last_timeh;

    @Range(min = 1, max = 30, message = "团最少人数超限")
    private Integer minper;

    @Range(min = 1, max = 30, message = "团最大人数超限")
    private Integer maxper;

    private List<String> tags;

    //tags模糊匹配
    public static SearchCondition tagsSearcher(SearchCondition searchCondition) {
        List<String> tags = searchCondition.getTags();
        if (!CollUtil.isEmpty(tags)) {
            List<String> tmp = new ArrayList<>();
            tags.forEach(tag -> {
                tmp.add("%" + tag + "%");
            });
            searchCondition.setTags(tmp);
        }
        return searchCondition;
    }

    //限定群
    public static SearchCondition groupSearcher(SearchCondition searchCondition) {
        String group = searchCondition.getGroup();
        if (null != group) {
            VerifyUtils.groupVerify(group);
            searchCondition.setGroup("%\"" + group + "\"%");
        }
        return searchCondition;
    }

    public static SearchCondition tagsAndGroupSearcher(SearchCondition searchCondition) {
        return SearchCondition.groupSearcher(SearchCondition.tagsSearcher(searchCondition));
    }
}

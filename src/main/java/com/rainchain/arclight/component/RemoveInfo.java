package com.rainchain.arclight.component;


import com.baomidou.mybatisplus.annotation.TableName;
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
public class RemoveInfo {
    @Digits(message = "id必须为非负整数", integer = 2147483647, fraction = 0)
    private Long id;
    
    @Length(min = 5, max = 10, message = "主持人QQ号非法！")
    private String kp_qq;

    @Valid
    private List<@Length(min = 5, max = 10, message = "主持人QQ号非法！") String> qqs;
}

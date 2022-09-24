package com.rainchain.arclight.component;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Player {
    @NotNull
    private String nick;
    @Length(min = 5, max = 10, message = "玩家QQ号非法！")
    private String qq;

    //重写equals和hashcode方法，qq相同，即为同一玩家
    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null || getClass() != that.getClass()) {
            return false;
        }
        Player player = (Player) that;
        return qq.equals(player.qq);
    }

    @Override
    public int hashCode() {
        return qq.hashCode();
    }
}

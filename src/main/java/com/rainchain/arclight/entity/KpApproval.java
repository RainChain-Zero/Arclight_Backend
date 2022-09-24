package com.rainchain.arclight.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @TableName kp_approval
 */
@TableName(value = "kp_approval")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class KpApproval implements Serializable {
    /**
     * 团id
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 申请人qq
     */
    @TableId(value = "qq")
    private String qq;

    /**
     * 主持人qq
     */
    @TableField(value = "kp_qq")
    private String kp_qq;

    /**
     * 申请人昵称
     */
    @TableField(value = "nick")
    private String nick;

    /**
     * 申请人附加信息
     */
    @TableField(value = "msg")
    private String msg;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        KpApproval other = (KpApproval) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getQq() == null ? other.getQq() == null : this.getQq().equals(other.getQq()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getQq() == null) ? 0 : getQq().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", qq=").append(qq);
        sb.append(", kp_qq=").append(kp_qq);
        sb.append(", nick=").append(nick);
        sb.append(", msg=").append(msg);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
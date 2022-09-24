package com.rainchain.arclight.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @TableName pl_application
 */
@TableName(value = "pl_application")
@Data
public class PlApplication implements Serializable {
    /**
     * pl的qq
     */
    @TableId(value = "qq")
    private String qq;

    /**
     * 团的id
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 团的名称
     */
    @TableField(value = "title")
    private String title;

    /**
     * 0：等待中；1：申请被同意；2：申请被拒绝
     */
    @TableField(value = "status")
    private Integer status = 0;

    /**
     * 主持人处理时间，未处理为NULL
     */
    @TableField(value = "timestamp")
    private Long timestamp;

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
        PlApplication other = (PlApplication) that;
        return (this.getQq() == null ? other.getQq() == null : this.getQq().equals(other.getQq()))
                && (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getTitle() == null ? other.getTitle() == null : this.getTitle().equals(other.getTitle()))
                && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getQq() == null) ? 0 : getQq().hashCode());
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getTitle() == null) ? 0 : getTitle().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", qq=").append(qq);
        sb.append(", id=").append(id);
        sb.append(", title=").append(title);
        sb.append(", status=").append(status);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
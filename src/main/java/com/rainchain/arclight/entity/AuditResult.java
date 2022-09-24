package com.rainchain.arclight.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuditResult {
    private String title;
    private String kp_qq;
    private String deleteTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    private String Label;
    private int Score;
    private String des;
    private String Keywords;
}

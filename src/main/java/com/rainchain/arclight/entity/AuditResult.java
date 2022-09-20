package com.rainchain.arclight.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Component
public class AuditResult {
    private String title;
    private String kp_qq;
    private String deleteTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    private String Label;
    private int Score;
    private String des;
    private String Keywords;
}

package com.rainchain.arclight.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
@JsonIgnoreProperties(value = {"qq"})
public class ParticipatingGames {

    private String qq;

    private Long id;

    private String title;
}

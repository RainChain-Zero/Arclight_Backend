package com.rainchain.arclight.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(value = {"qq"})
public class ParticipatingGames {

    private String qq;

    private Long id;

    private String title;
}

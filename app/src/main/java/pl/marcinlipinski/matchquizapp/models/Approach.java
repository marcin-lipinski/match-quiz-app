package pl.marcinlipinski.matchquizapp.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@SuperBuilder
public class Approach {
    private Long id;
    private String league;
    private LocalDate approachDate;
    private String score;
    private Integer favourite;
}

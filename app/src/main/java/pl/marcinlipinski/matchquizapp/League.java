package pl.marcinlipinski.matchquizapp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@AllArgsConstructor
@SuperBuilder
public class League {
    private Long id;
    private String name;
    private String logo;
}

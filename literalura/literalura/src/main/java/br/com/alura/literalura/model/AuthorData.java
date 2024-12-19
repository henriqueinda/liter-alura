package br.com.alura.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AuthorData(String name,
                         @JsonAlias("birth_year") Long birthYear,
                         @JsonAlias("death_year") Long deathYear) {

    @Override
    public String toString() {
        return  name;
    }
}

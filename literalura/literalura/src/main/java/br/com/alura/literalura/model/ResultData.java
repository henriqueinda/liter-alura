package br.com.alura.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ResultData(Long count,
                         @JsonAlias("results") List<BookData> listOfResults) {

    @Override
    public String toString() {
        return "\nQTD LIVROS: " + count +
                "\n" + listOfResults;
    }
}

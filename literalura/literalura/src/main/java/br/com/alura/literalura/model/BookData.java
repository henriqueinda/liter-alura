package br.com.alura.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record BookData (String title,
                        List<AuthorData> authors,
                        List<String> languages,
                        @JsonAlias("download_count") Long downloads) {

    @Override
    public String toString() {
        return "LIVRO: " + title + " | " +
                "AUTORES: " + authors +
                " IDIOMAS: " + languages + "\n";
    }
}

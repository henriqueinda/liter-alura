package br.com.alura.literalura.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "livros")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String title;

    @ManyToMany(mappedBy = "books", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Author> authors = new ArrayList<>();

    private String languages;
    private double downloads;

    public Book() {}

    public Book(BookData bookData) {
        this.title = bookData.title();
        this.languages = bookData.languages().get(0);
        this.downloads = bookData.downloads();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getAuthors() {
        return authors.stream()
                .map(Author::getName)
                .collect(Collectors.toList());
    }

    public void setAuthors(List<Author> authors) {
        authors.forEach(author -> author.setBooks(this));
        this.authors = authors;
    }

    public String getLanguages() {
        return languages;
    }

    public void setLanguages(String languages) {
        this.languages = languages;
    }

    public double getDownloads() {
        return downloads;
    }

    public void setDownloads(double downloads) {
        this.downloads = downloads;
    }

    @Override
    public String toString() {
        return """
               ----- LIVRO -----
               TÍTULO: %s
               AUTOR: %s
               IDIOMA: %s
               DOWNLOADS: %s
               -----------------
               """.formatted(getTitle(), getAuthors(), getLanguages(), getDownloads());
    }
}

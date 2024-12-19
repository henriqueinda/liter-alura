package br.com.alura.literalura.main;

import br.com.alura.literalura.model.Author;
import br.com.alura.literalura.model.Book;
import br.com.alura.literalura.model.ResultData;
import br.com.alura.literalura.repository.AuthorRepository;
import br.com.alura.literalura.repository.BookRepository;
import br.com.alura.literalura.service.ConverterData;
import br.com.alura.literalura.service.RequestAPI;

import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    private Scanner scanner = new Scanner(System.in);
    private RequestAPI requestAPI = new RequestAPI();
    private ConverterData converter = new ConverterData();
    private BookRepository repositoryBook;
    private AuthorRepository authorRepository;

    private final String URL = "https://gutendex.com/books/?search=";

    public Main(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.repositoryBook = bookRepository;
        this.authorRepository = authorRepository;
    }

    public void showMenu() {
        var option = "-1";

        clearScreen();

        label:
        while (!option.equalsIgnoreCase("0")) {
            var menu = """
                    ##################################################
                    
                    LITER@LURA
                    
                    ------------- [ESCOLHA UMA OPÇÃO] -------------
                    
                    (NA API -> 'GUTENDEX')
                    [1] BUSCAR LIVRO PELO TÍTULO;
                    
                    (NO BANCO LOCAL -> 'POSTGRES')
                    [2] LISTAR LIVROS REGISTRADOS;
                    [3] LISTAR AUTORES REGISTRADOS;
                    [4] LISTAR AUTORES VIVOS EM UM DETERMINADO ANO;
                    [5] LISTAR LIVROS POR IDIOMA;
                    
                    [0] SAIR
                    """;

            System.out.println(menu);
            option = scanner.next();
            scanner.nextLine();

            switch (option) {
                case "0":
                    System.out.println("\nATÉ BREVE!\n\n##################################################\n");
                    break label;

                case "1":
                    findByTitle();
                    break;

                case "2":
                    System.out.println("\n" + repositoryBook.findAll());
                    break;

                case "3":
                    System.out.println("\n" + authorRepository.findAll());
                    break;

                case "4":
                    System.out.println("\nDIGITE UM 'ANO' PARA BUSCA:");
                    var year = scanner.nextInt();
                    System.out.println("\n" + authorRepository.searchAuthorsYear(year));
                    break;

                case "5":
                    findByLanguage();
                    break;

                default:
                    System.out.println("\nOPÇÃO INVÁLIDA!\nTENTE NOVAMENTE...\n");
                    break;

            }
        }
    }

    private void findByTitle() {
        System.out.println("\nDIGITE UMA 'PALAVRA-CHAVE' PARA BUSCA:");
        String title = scanner.nextLine().toLowerCase().replace(" ", "%20");
        ResultData resultsSearch = converter.getData(requestAPI.getDataBook(URL + title), ResultData.class);

        System.out.println("\n----- RESULTADO DA BUSCA -----\n");
        AtomicInteger i = new AtomicInteger(0);
        resultsSearch.listOfResults().forEach(book -> System.out.println("[" + i.incrementAndGet() + "]" +
                "LIVRO: " + book.title().toUpperCase() + " | " +
                "AUTOR(ES): " + book.authors()));

        var dataFailed = true;

        if (resultsSearch.listOfResults().isEmpty()) {
            System.out.println("NENHUM LIVRO ENCONTRADO!\n");
        } else {
            while (dataFailed) {
                System.out.println("\nQUAL OBRA VOCÊ GOSTARIA DE ADICIONAR AO BANCO DE DADOS? (DIGITE O NÚMERO DA OPÇÃO)");
                var option = -1;

                try {
                    option = scanner.nextInt();
                } catch (Exception e) {
                    System.out.println("OPÇÃO INVÁLIDA!");
                    throw new RuntimeException(e);
                }
                scanner.nextLine();

                if ((option > i.get()) | (option <= 0)) {
                    System.out.println("OPÇÃO INVÁLIDA!");
                } else {
                    dataFailed = false;

                    var bookData = resultsSearch.listOfResults().get((option - 1));
                    Book book = new Book(bookData);
                    List<Author> authorList = bookData.authors().stream()
                            .map(Author::new)
                            .toList();
                    book.setAuthors(authorList);

                    System.out.println(book);
                    System.out.println("LIVRO ARMAZENADO!\n");

                    repositoryBook.save(book);
                }
            }
        }
    }

    private void findByLanguage() {
        var menuLanguages = """
                ----- IDIOMA PARA BUSCA -----
                
                [AR] ÁRABE
                [DE] ALEMÃO
                [EN] INGLÊS
                [ES] ESPANHOL
                [FR] FRANCÊS
                [KO] COREANO
                [LA] LATIM
                [PT] PORTUGUÊS
                [RU] RUSSO
                [ZH] CHINÊS
                
                DIGITE O IDIOMA DESEJADO:
                """;
        System.out.println(menuLanguages);
        var optionLanguage = scanner.nextLine().toLowerCase();

        if (optionLanguage.contains("ara")) {
            optionLanguage = "ar";
        } else if ((optionLanguage.contains("de")) | (optionLanguage.contains("ale"))) {
            optionLanguage = "de";
        } else if ((optionLanguage.contains("en")) | (optionLanguage.contains("ing"))) {
            optionLanguage = "en";
        } else if ((optionLanguage.contains("esp"))) {
            optionLanguage = "es";
        } else if ((optionLanguage.contains("fr"))) {
            optionLanguage = "fr";
        } else if ((optionLanguage.contains("ko")) | (optionLanguage.contains("cor"))) {
            optionLanguage = "ko";
        } else if ((optionLanguage.contains("la"))) {
            optionLanguage = "la";
        } else if ((optionLanguage.contains("pt")) | (optionLanguage.contains("por"))) {
            optionLanguage = "pt";
        } else if ((optionLanguage.contains("ru"))) {
            optionLanguage = "ru";
        } else if ((optionLanguage.contains("zh")) | (optionLanguage.contains("chi"))) {
            optionLanguage = "zh";
        } else {
            System.out.println("OPÇÃO INVÁLIDA!");
        }

        var book = repositoryBook.searchBookLanguage(optionLanguage);
        if (!book.isEmpty()) {
            System.out.println(book);
        } else {
            System.out.println("\nNENHUM LIVRO ENCONTRADO!\n");
        }

    }

    private void clearScreen() {
        for (int i = 0; i < 20; i++) {
            System.out.println("\n");
        }
    }

}

package br.com.alura.literalura.service;

public interface IConverterData {
    <T> T  getData(String json, Class<T> variableClass);
}

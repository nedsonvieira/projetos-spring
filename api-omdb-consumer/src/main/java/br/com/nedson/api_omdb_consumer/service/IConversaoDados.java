package br.com.nedson.api_omdb_consumer.service;

public interface IConversaoDados {

    <T> T converterDados(String json, Class<T> classe);

}

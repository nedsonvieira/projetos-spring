package br.com.nedson.AluraFlix.dto.categoria.validacoes;

import br.com.nedson.AluraFlix.dto.categoria.DadosAtualizarCategoria;
import br.com.nedson.AluraFlix.dto.categoria.DadosCadastrarCategoria;

public interface ValidadorCategorias {
    void validar(DadosCadastrarCategoria dados);
    void validar(DadosAtualizarCategoria dados);
}

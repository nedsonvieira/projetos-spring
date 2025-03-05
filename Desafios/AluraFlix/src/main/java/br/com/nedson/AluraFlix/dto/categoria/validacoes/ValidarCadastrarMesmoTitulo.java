package br.com.nedson.AluraFlix.dto.categoria.validacoes;

import br.com.nedson.AluraFlix.dto.categoria.DadosAtualizarCategoria;
import br.com.nedson.AluraFlix.dto.categoria.DadosCadastrarCategoria;
import br.com.nedson.AluraFlix.repository.CategoriaRepository;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ValidarCadastrarMesmoTitulo implements ValidadorCategorias{

    private final CategoriaRepository categoriaRepository;

    @Override
    public void validar(DadosCadastrarCategoria dados) {
        if (categoriaRepository.existsByTitulo(dados.titulo())){
            throw new DataIntegrityViolationException("Já existe uma categoria salva com este título!");
        }
    }

    @Override
    public void validar(DadosAtualizarCategoria dados) {
        if (categoriaRepository.existsByTitulo(dados.titulo())){
            throw new DataIntegrityViolationException("Já existe uma categoria salva com este título!");
        }
    }
}

package br.com.nedson.nv_ecommerce.produtos_service.domain.service;

import br.com.nedson.nv_ecommerce.produtos_service.domain.Categoria;
import br.com.nedson.nv_ecommerce.produtos_service.domain.dto.ProdutoAtualizarRequestDto;
import br.com.nedson.nv_ecommerce.produtos_service.domain.dto.ProdutoCadastrarRequestDto;
import br.com.nedson.nv_ecommerce.produtos_service.domain.dto.ProdutoMapper;
import br.com.nedson.nv_ecommerce.produtos_service.domain.dto.ProdutoResponseDto;
import br.com.nedson.nv_ecommerce.produtos_service.domain.entity.Produto;
import br.com.nedson.nv_ecommerce.produtos_service.domain.repository.ProdutoRepository;
import br.com.nedson.nv_ecommerce.produtos_service.infra.exception.CategoriaNaoEncontradaException;
import br.com.nedson.nv_ecommerce.produtos_service.infra.exception.ProdutoJaCadastradoException;
import br.com.nedson.nv_ecommerce.produtos_service.infra.exception.ProdutoNaoEncontradoException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@AllArgsConstructor
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    @Transactional
    public ProdutoResponseDto cadastrarProduto(ProdutoCadastrarRequestDto dto) {
        if (produtoRepository.existsByNome(dto.nome())){
            throw new ProdutoJaCadastradoException("Produto com o nome " +dto.nome()+ " já cadastrado");
        }

        Produto produto = produtoRepository.save(
                ProdutoMapper.toProduto(dto));

        return ProdutoMapper.toResponse(produto);
    }

    public Page<ProdutoResponseDto> listarTodosProdutos(Pageable pageable) {
        return produtoRepository.findAll(pageable)
                .map(ProdutoMapper::toResponse);
    }

    public Page<ProdutoResponseDto> listarPorCategoria(String categoria, Pageable pageable) {

        try {
            Categoria categoriaEnum = Categoria.valueOf(categoria.toUpperCase());

            if (!produtoRepository.existsByCategoria(categoriaEnum)) {
                throw new CategoriaNaoEncontradaException("Categoria '" +categoria+ "' não encontrada." );
            }
            return produtoRepository.findAllByCategoria(categoriaEnum, pageable)
                    .map(ProdutoMapper::toResponse);
        } catch (IllegalArgumentException ex) {
            throw new CategoriaNaoEncontradaException(
                    String.format("Categoria '%s' não é válida. As categorias disponíveis são: %s",
                            categoria,
                            String.join(", ", listarCategoriasValidas()))
            );
        }
    }

    public ProdutoResponseDto listarPorId(String id) {
        Produto produto = verificarProduto(id);
        return ProdutoMapper.toResponse(produto);
    }

    @Transactional
    public ProdutoResponseDto atualizarProduto(String id, ProdutoAtualizarRequestDto dto) {
        Produto produto = verificarProduto(id);
        produto.atualizar(dto);

        return ProdutoMapper.toResponse(produto);
    }

    @Transactional
    public void adicionarEstoque(String id, int quantidade) {
        Produto produto = verificarProduto(id);
        produto.adicionarEstoque(quantidade);
    }

    @Transactional
    public void deletarProduto(String id) {
        Produto produto = verificarProduto(id);
        produtoRepository.delete(produto);
    }

    private Produto verificarProduto(String id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new ProdutoNaoEncontradoException("Produto com id " +id+ " não encontrado"));
    }

    private List<String> listarCategoriasValidas() {
        return Arrays.stream(Categoria.values())
                .map(Enum::name)
                .toList();
    }
}

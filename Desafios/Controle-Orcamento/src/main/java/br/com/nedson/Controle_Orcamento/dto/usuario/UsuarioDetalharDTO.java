package br.com.nedson.Controle_Orcamento.dto.usuario;

import br.com.nedson.Controle_Orcamento.model.Perfil;
import br.com.nedson.Controle_Orcamento.model.Usuario;

import java.util.List;

public record UsuarioDetalharDTO(
        String nome,
        String email,
        List<String> perfis
) {
    public UsuarioDetalharDTO(Usuario usuario){
        this(usuario.getNome(),
                usuario.getEmail(),
                usuario.getPerfis().stream()
                        .map(Perfil::getNome)
                        .toList()
        );
    }
}

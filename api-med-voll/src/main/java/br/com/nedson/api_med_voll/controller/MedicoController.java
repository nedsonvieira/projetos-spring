package br.com.nedson.api_med_voll.controller;

import br.com.nedson.api_med_voll.repository.MedicoRepository;
import br.com.nedson.api_med_voll.dto.medico.DadosAtualizarMedico;
import br.com.nedson.api_med_voll.dto.medico.DadosBuscarMedico;
import br.com.nedson.api_med_voll.dto.medico.DadosCadastrarMedico;
import br.com.nedson.api_med_voll.dto.medico.DadosDetalharMedico;
import br.com.nedson.api_med_voll.model.Medico;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/medicos")
@SecurityRequirement(name = "bearer-key")
public class MedicoController {

    @Autowired
    private MedicoRepository repositorio;

    @PostMapping
    @Transactional
    public ResponseEntity cadastrar(@RequestBody @Valid DadosCadastrarMedico dados,
                                    UriComponentsBuilder uriBuilder){
        var medico = new Medico(dados);
        repositorio.save(medico);

        var uri = uriBuilder.path("/medicos/{id}").buildAndExpand(medico.getId()).toUri();

        return ResponseEntity.created(uri).body(new DadosDetalharMedico(medico));
    }

//    @PostMapping
//    @Transactional
//    public void cadastrarAll(@RequestBody @Valid List<MedicoCadastrarDTO> dados) {
//        List<Medico> medicos = dados.stream()
//                .map(m -> new Medico(null, m.nome(),
//                        m.email(), m.crm().trim(), m.telefone(), m.especialidade(), new Endereco(m.endereco())))
//                .toList();
//        repositorio.saveAll(medicos);
//    }

    @GetMapping //Page retorna informações sobre a paginação
    public ResponseEntity<Page<DadosBuscarMedico>> listar(@PageableDefault(size = 10, sort = {"nome"}) Pageable paginacao){ // Pageable é uma classe do spring para auxiliar a paginação
        var page = repositorio.findAllByAtivoTrue(paginacao) // passando o parametro o Spring montará a query automaticamente com o esquema de paginação
                .map(DadosBuscarMedico::new);
        return ResponseEntity.ok(page);
    /*
    Para mudar o padrão da paginação utiliza @PageableDefault no parâmetro Pageable.
    Passando os atributos size (qntos elementos por página), page (nº da pg) e sort (como os elementos vao está ordenados).
    Pode-se escolher o atributo que guiará a ordenação, passando entre chaves duplas.
    Se os parâmetros que forem adicionados diretamente na URL serão utilizados.
     */
    }

    @PutMapping
    @Transactional
    public ResponseEntity atualizar(@RequestBody @Valid DadosAtualizarMedico dados){
        var medico = repositorio.getReferenceById(dados.id());
        medico.atualizarInfo(dados);
        return ResponseEntity.ok(new DadosDetalharMedico(medico));
    }
//    @DeleteMapping("/{id}") // Exclusão de registro do banco de dados
//    @Transactional
//    public void excluir(@PathVariable Long id){
//        repositorio.deleteById(id);
//    }

    @DeleteMapping("{id}") // exclusão lógica, não exclui o registro
    @Transactional
    @Secured("ROLE_ADMIN") //restringe o acesso a funcionalidade, com base no perfil
    public ResponseEntity excluir(@PathVariable Long id){
        var medico = repositorio.getReferenceById(id);
        medico.inativar();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("{id}")
    public ResponseEntity detalhar(@PathVariable Long id){
        var medico = repositorio.getReferenceById(id);
        return ResponseEntity.ok(new DadosDetalharMedico(medico)) ;
    }
}

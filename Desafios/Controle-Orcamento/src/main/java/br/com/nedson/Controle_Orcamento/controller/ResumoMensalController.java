package br.com.nedson.Controle_Orcamento.controller;

import br.com.nedson.Controle_Orcamento.dto.ResumoMensalDTO;
import br.com.nedson.Controle_Orcamento.email.EmailResumoMensal;
import br.com.nedson.Controle_Orcamento.model.Usuario;
import br.com.nedson.Controle_Orcamento.service.ResumoMensalService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/resumo")
@AllArgsConstructor
@SecurityRequirement(name = "bearer-key")
public class ResumoMensalController {

    private final ResumoMensalService resumoMensalService;

    private final EmailResumoMensal emailResumoMensal;

    private ResumoMensalDTO resumo;

    @GetMapping("/{ano}/{mes}")
    public ResponseEntity<ResumoMensalDTO> obterResumoDoMes(@PathVariable int ano, @PathVariable int mes) {
        resumo = resumoMensalService.gerarResumo(ano, mes);

        return ResponseEntity.ok(resumo);
    }

    @PostMapping("/{ano}/{mes}/enviar-email")
    public ResponseEntity<String > enviarEmail(@PathVariable int ano, @PathVariable int mes) {
        var usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        emailResumoMensal.enviar(resumo, ano, mes, usuario);

        return ResponseEntity.ok("Email enviado!");
    }
}
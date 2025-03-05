package br.com.nedson.dt_itau.controller;

import br.com.nedson.dt_itau.model.Estatistica;
import br.com.nedson.dt_itau.service.EstatisticaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/estatistica")
@Tag(name = "estatistica", description = "Controlador para gerar estatíscas das transações")
public class EstatisticaController {

    private final EstatisticaService estatisticaService;

    @GetMapping
    @Operation(summary = "Gera estatísticas sobre transações realizadas", description = "EndPoint - Gerar Estatísticas das Transações")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })

    public ResponseEntity<Estatistica> gerarEstatisticas(
            @RequestParam(value = "intervalo", required = false, defaultValue = "60") Integer intervalo){
        return ResponseEntity.ok(estatisticaService.gerarEstatisticas(intervalo));
    }
}

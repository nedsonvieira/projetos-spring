package br.com.nedson.nv_food.pedidos.controller;

import br.com.nedson.nv_food.pedidos.dto.PedidoDto;
import br.com.nedson.nv_food.pedidos.dto.StatusDto;
import br.com.nedson.nv_food.pedidos.service.PedidoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/pedidos")
@AllArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    @PostMapping()
    public ResponseEntity<PedidoDto> realizaPedido(@RequestBody @Valid PedidoDto dto, UriComponentsBuilder uriBuilder) {
        PedidoDto pedidoRealizado = pedidoService.criarPedido(dto);

        URI endereco = uriBuilder.path("/pedidos/{id}").buildAndExpand(pedidoRealizado.getId()).toUri();

        return ResponseEntity.created(endereco).body(pedidoRealizado);
    }

    @GetMapping()
    public List<PedidoDto> listarTodos() {
        return pedidoService.obterTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoDto> listarPorId(@PathVariable @NotNull Long id) {
        PedidoDto dto = pedidoService.obterPorId(id);

        return  ResponseEntity.ok(dto);
    }

    @GetMapping("/porta")
    public String retornaPorta(@Value("${local.server.port}") String porta){
        return String.format("Requisição respondida pela instância executando na porta %s", porta);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<PedidoDto> atualizaStatus(@PathVariable Long id, @RequestBody StatusDto status){
        PedidoDto dto = pedidoService.atualizaStatus(id, status);

        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}/pago")
    public ResponseEntity<Void> aprovaPagamento(@PathVariable @NotNull Long id) {
        pedidoService.aprovaPagamentoPedido(id);

        return ResponseEntity.ok().build();
    }
}

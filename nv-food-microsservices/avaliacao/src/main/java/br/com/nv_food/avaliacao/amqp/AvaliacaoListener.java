package br.com.nv_food.avaliacao.amqp;

import br.com.nv_food.avaliacao.dto.PagamentoDto;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class AvaliacaoListener {

    @RabbitListener(queues = "pagamentos.detalhes-avaliacao")
    public void recebeMensagem(@Payload PagamentoDto pagamento) {

        if (pagamento.getNumero().equals("0000")) {
            throw new RuntimeException("não consegui processar a mensagem" + pagamento.getId());
        }

        String msg = """
                Necessário criar registro de avaliação para o pedido: %s
                Id do pagamento: %s
                Nome do cliente: %s
                Valor R$: %s
                Status: %s
                """.formatted(pagamento.getPedidoId(),
                pagamento.getId(),
                pagamento.getNome(),
                pagamento.getValor(),
                pagamento.getStatus());

        System.out.println(msg);
    }
}

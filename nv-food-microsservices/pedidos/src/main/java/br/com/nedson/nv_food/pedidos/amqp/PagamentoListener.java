package br.com.nedson.nv_food.pedidos.amqp;

import br.com.nedson.nv_food.pedidos.dto.PagamentoDto;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class PagamentoListener {

    @RabbitListener(queues = "pagamentos.detalhes-pedido")
    public void recebeMensagem(@Payload PagamentoDto pagamento){

        String msg = """
                Id do pagamento: %s
                Nome do cliente: %s
                Valor R$: %s
                Status: %s
                """.formatted(pagamento.getId(),
                pagamento.getNome(),
                pagamento.getValor(),
                pagamento.getStatus()
        );

        System.out.println("Recebi a mensagem " + msg);
    }
}

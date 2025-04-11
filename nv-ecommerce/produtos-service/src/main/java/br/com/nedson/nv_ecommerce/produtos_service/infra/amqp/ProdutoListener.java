package br.com.nedson.nv_ecommerce.produtos_service.infra.amqp;

import br.com.nedson.nv_ecommerce.produtos_service.domain.dto.EventoProduto;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class ProdutoListener {

    @RabbitListener(queues = "produto.cadastrado")
    public void handleProdutoCadastrado(@Payload EventoProduto eventoProduto) {
        System.out.println("Evento de Produto Cadastrado recebido: " + eventoProduto);
        // Processar o evento conforme necessário
    }

    @RabbitListener(queues = "produto.atualizado")
    public void handleProdutoAtualizado(@Payload EventoProduto eventoProduto) {
        System.out.println("Evento de Produto Atualizado recebido: " + eventoProduto);
        // Processar o evento conforme necessário
    }

    @RabbitListener(queues = "produto.deletado")
    public void handleProdutoDeletado(@Payload EventoProduto eventoProduto) {
        System.out.println("Evento de Produto Deletado recebido: " + eventoProduto);
        // Processar o evento conforme necessário
    }
}


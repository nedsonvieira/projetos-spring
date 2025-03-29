package br.com.nedson.nv_food.pagamentos.repository;

import br.com.nedson.nv_food.pagamentos.model.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {
}

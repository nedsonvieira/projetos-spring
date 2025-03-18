package br.com.nedson.Controle_Orcamento.email;

import br.com.nedson.Controle_Orcamento.dto.ResumoMensalDTO;
import br.com.nedson.Controle_Orcamento.model.Usuario;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class EmailResumoMensal {

    private final EmailSender emailSender;

    @Async
    public void enviar(ResumoMensalDTO dto, int ano, int mes, Usuario usuario){
        String totalGastoFormatado = dto.totalGastoPorCategoria().entrySet().stream()
                .map(entry -> String.format("Categoria: %s, Gasto: R$ %.2f", entry.getKey(), entry.getValue()))
                .collect(Collectors.joining("\n"));

        emailSender.enviarEmail(
                usuario.getEmail(),
                "Resumo mensal de receitas e despesas",
                """
                        Olá, %s
                        
                        Seu resumo mensal do período %02d/%04d foi gerado e segue abaixo!
                        
                        Receitas Totais: R$ %.2f
                        Despesas Totais: R$ %.2f
                        Saldo Final: R$ %.2f
                        
                        Total Gasto por Categoria:
                        %s
                        """.formatted(
                        usuario.getNome(),
                        mes,
                        ano,
                        dto.totalReceitas(),
                        dto.totalDespesas(),
                        dto.saldoFinal(),
                        totalGastoFormatado)
        );
    }
}

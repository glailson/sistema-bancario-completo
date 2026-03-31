package com.projeto.banco.service;

import com.projeto.banco.exception.NegocioException;
import com.projeto.banco.model.Conta;
import com.projeto.banco.model.Transacao;
import com.projeto.banco.repository.ContaRepository;
import com.projeto.banco.repository.TransacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.math.BigDecimal;
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Service
@RequiredArgsConstructor
public class ContaService {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private final ContaRepository contaRepository;
    private final TransacaoRepository transacaoRepository;

    // OPERAÇÃO: Criar Conta
    public Conta criarConta(Conta conta) {
        boolean jaExiste = contaRepository.findByNumero(conta.getNumero()).isPresent();

        if (jaExiste) {
            throw new NegocioException("Não foi possível criar a conta: O número de conta " + conta.getNumero() + " já está em uso.");
        }
        return contaRepository.save(conta);
    }

    // OPERAÇÃO: Creditar (Depósito)
    @Transactional
    public void creditar(Long contaId, BigDecimal valor) {
        Conta conta = buscarComLock(contaId);
        conta.setSaldo(conta.getSaldo().add(valor));
        
        registrarTransacao(conta, valor, "CREDITO");
        contaRepository.save(conta);
    }

    // OPERAÇÃO: Debitar (Saque)
    @Transactional
    public void debitar(Long contaId, BigDecimal valor) {
        Conta conta = buscarComLock(contaId);
        
        if (conta.getSaldo().compareTo(valor) < 0) {
            throw new NegocioException("Saldo insuficiente para o débito.");
        }
        
        conta.setSaldo(conta.getSaldo().subtract(valor));
        registrarTransacao(conta, valor.negate(), "DEBITO");
        contaRepository.save(conta);
    }

    // OPERAÇÃO: Transferir (O maior desafio de concorrência)
    @Transactional
    public void transferir(Long origemId, Long destinoId, BigDecimal valor) {
        if (origemId.equals(destinoId)) {
            throw new NegocioException("Não é possível transferir para a mesma conta.");
        }

        Conta origem = contaRepository.findByIdWithLock(origemId)
                .orElseThrow(() -> new NegocioException("Conta de origem não encontrada"));
        Conta destino = contaRepository.findByNumeroLock(destinoId.toString())
                .orElseThrow(() -> new NegocioException("Conta de destino não encontrada"));

        if (origem.getSaldo().compareTo(valor) < 0) {
            throw new NegocioException("Saldo insuficiente para transferência.");
        }

        origem.setSaldo(origem.getSaldo().subtract(valor));
        destino.setSaldo(destino.getSaldo().add(valor));

        Transacao transacaoOrigem = new Transacao(valor.negate(), Transacao.TipoTransacao.TRANSFERENCIA_ENVIADA, origem, destino);
        origem.getTransacoes().add(transacaoOrigem);

        Transacao transacaoDestino = new Transacao(valor, Transacao.TipoTransacao.TRANSFERENCIA_RECEBIDA, destino, origem);
        destino.getTransacoes().add(transacaoDestino);

        contaRepository.save(origem);
        contaRepository.save(destino);

        // 2. Prepara os dados da mensagem
        String dataHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        String mensagem = String.format("[%s] Transferência de R$ %s da conta %d para a conta %d",
                dataHora, valor, origemId, destinoId);

        // 3. Agenda o envio para o Kafka (Garante que só envia se o commit ocorrer)
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                kafkaTemplate.send("transferencia-movimentacoes-usuarios", mensagem);
            }
        });
    }

    public List<Transacao> emitirExtrato(Long contaId) {
        return transacaoRepository.findByContaIdOrderByDataHoraDesc(contaId);
    }

    //Método para buscar com Bloqueio Pessimista
    private Conta buscarComLock(Long id) {
        return contaRepository.findByIdWithLock(id)
                .orElseThrow(() -> new NegocioException("Conta não encontrada com ID: " + id));
    }

    private void registrarTransacao(Conta conta, BigDecimal valor, String tipo) {
        Transacao t = new Transacao();
        t.setConta(conta);
        t.setValor(valor);
        t.setTipo(tipo);
        transacaoRepository.save(t);
    }
}
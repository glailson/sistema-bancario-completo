package com.projeto.banco.sistema_bancario.service;

import com.projeto.banco.exception.NegocioException;
import com.projeto.banco.model.Conta;
import com.projeto.banco.repository.ContaRepository;
import com.projeto.banco.repository.TransacaoRepository;
import com.projeto.banco.service.ContaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ContaServiceTest {

    @Mock
    private ContaRepository contaRepository;

    @Mock
    private TransacaoRepository transacaoRepository;

    @InjectMocks
    private ContaService contaService;

    private Conta contaOrigem;
    private Conta contaDestino;

    @BeforeEach
    void setUp() {
        // Criando as contas para os testes
        contaOrigem = new Conta();
        contaOrigem.setId(1L);
        contaOrigem.setTitular("Glailson");
        contaOrigem.setSaldo(new BigDecimal("1000.00"));

        contaDestino = new Conta();
        contaDestino.setId(2L);
        contaDestino.setTitular("Admin Sistema");
        contaDestino.setSaldo(new BigDecimal("500.00"));
    }

    @Test
    @DisplayName("Deve creditar valor com sucesso usando findByIdWithLock")
    void deveCreditarValorComSucesso() {
        // Configurando o Mock para o método específico que você usa (linha 70)
        when(contaRepository.findByIdWithLock(1L)).thenReturn(Optional.of(contaOrigem));

        contaService.creditar(1L, new BigDecimal("200.00"));

        assertEquals(new BigDecimal("1200.00"), contaOrigem.getSaldo());
        verify(contaRepository, times(1)).save(contaOrigem);
        verify(transacaoRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Deve debitar valor com sucesso")
    void deveDebitarValorComSucesso() {
        when(contaRepository.findByIdWithLock(1L)).thenReturn(Optional.of(contaOrigem));

        contaService.debitar(1L, new BigDecimal("300.00"));

        assertEquals(new BigDecimal("700.00"), contaOrigem.getSaldo());
        verify(contaRepository, times(1)).save(contaOrigem);
    }

    @Test
    @DisplayName("Deve lançar NegocioException ao tentar debitar sem saldo")
    void deveLancarExcecaoAoDebitarSemSaldo() {
        when(contaRepository.findByIdWithLock(1L)).thenReturn(Optional.of(contaOrigem));

        assertThrows(NegocioException.class, () -> {
            contaService.debitar(1L, new BigDecimal("1500.00"));
        });

        verify(contaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve transferir valor entre contas com sucesso")
    void deveTransferirComSucesso() {
        // Para transferência, o Service busca as duas contas com Lock
        when(contaRepository.findByIdWithLock(1L)).thenReturn(Optional.of(contaOrigem));
        when(contaRepository.findByIdWithLock(2L)).thenReturn(Optional.of(contaDestino));

        contaService.transferir(1L, 2L, new BigDecimal("400.00"));

        assertEquals(new BigDecimal("600.00"), contaOrigem.getSaldo());
        assertEquals(new BigDecimal("900.00"), contaDestino.getSaldo());

        // Verifica se salvou as duas contas
        verify(contaRepository, times(2)).save(any(Conta.class));
    }

    @Test
    @DisplayName("Deve lançar erro ao transferir para a mesma conta")
    void deveLancarErroTransferenciaMesmaConta() {
        assertThrows(NegocioException.class, () -> {
            contaService.transferir(1L, 1L, new BigDecimal("100.00"));
        });
    }
}
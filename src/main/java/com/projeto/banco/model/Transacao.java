package com.projeto.banco.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "transacao")
@Data
public class Transacao {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private LocalDateTime dataHora = LocalDateTime.now();
    private BigDecimal valor;
    private String tipo; // "CREDITO", "DEBITO", "TRANSFERENCIA"

    @ManyToOne
    @JoinColumn(name = "conta_id")
    @JsonBackReference // O Jackson vai ignorar este campo no JSON da Transação
    private Conta conta;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_transacao")
    private TipoTransacao tipoTransacao;

    @ManyToOne
    @JoinColumn(name = "conta_destino_id")
    @JsonIgnoreProperties({"transacoes", "saldo"})
    private Conta contaDestino;

    public Transacao() {}

    public Transacao(BigDecimal valor, TipoTransacao tipo, Conta conta, Conta contaDestino) {
        this.valor = valor;
        this.tipoTransacao = tipo;
        this.conta = conta;
        this.contaDestino = contaDestino;
        this.dataHora = LocalDateTime.now();
    }

    public enum TipoTransacao {
        TRANSFERENCIA_ENVIADA("DÉBITO"),
        TRANSFERENCIA_RECEBIDA("CRÉDITO");

        private String descricao;

        TipoTransacao(String descricao) {
            this.descricao = descricao;
        }

        public String getDescricao() {
            return descricao;
        }
    }
}

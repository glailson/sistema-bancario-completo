package com.projeto.banco.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "contas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Conta {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String numero;
    @Column(nullable = false)
    private String titular;
    @Column(nullable = false)
    private BigDecimal saldo = BigDecimal.ZERO;
    @OneToMany(mappedBy = "conta", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Transacao> transacoes;
    
}

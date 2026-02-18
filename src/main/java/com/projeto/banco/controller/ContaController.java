package com.projeto.banco.controller;

import com.projeto.banco.model.Conta;
import com.projeto.banco.model.Transacao;
import com.projeto.banco.repository.ContaRepository;
import com.projeto.banco.service.ContaService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/contas")
@RequiredArgsConstructor
public class ContaController {

    private final ContaService contaService;
    @Autowired
    private ContaRepository contaRepository;

    @PostMapping
    public ResponseEntity<Conta> criar(@RequestBody Conta conta) {
        return ResponseEntity.ok(contaService.criarConta(conta));
    }

    // USUÁRIO: Depositar (Crédito)
    @PostMapping("/{id}/creditar")
    public ResponseEntity<Void> creditar(@PathVariable Long id, @RequestBody Map<String, BigDecimal> request) {
        contaService.creditar(id, request.get("valor"));
        return ResponseEntity.ok().build();
    }

    // USUÁRIO: Sacar (Débito)
    @PostMapping("/{id}/debitar")
    public ResponseEntity<Void> debitar(@PathVariable Long id, @RequestBody Map<String, BigDecimal> request) {
        contaService.debitar(id, request.get("valor"));
        return ResponseEntity.ok().build();
    }

    // USUÁRIO: Transferir
    // Definição simples do Record para transferência
    public record TransferenciaRequest(Long origemId, Long destinoId, BigDecimal valor) {}

    // No seu Controller
    @PostMapping("/transferir")
    public ResponseEntity<Void> transferir(@RequestBody TransferenciaRequest request) {
        contaService.transferir(request.origemId(), request.destinoId(), request.valor());
        return ResponseEntity.ok().build();
    }

    // USUÁRIO: Ver Extrato
    @GetMapping("/{id}/extrato")
    public ResponseEntity<List<Transacao>> extrato(@PathVariable Long id) {
        return ResponseEntity.ok(contaService.emitirExtrato(id));
    }

    // Busca por ID (Usado no buscarConta())
    @GetMapping("/{id}")
    public ResponseEntity<Conta> buscarPorId(@PathVariable Long id) {
        return contaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // A URL final será: http://localhost:8080/api/contas/numero/{numero}
    @GetMapping("/numero/{numero}")
    public ResponseEntity<Conta> buscarPorNumero(@PathVariable String numero) {
        return contaRepository.findByNumero(numero)
                .map(conta -> ResponseEntity.ok(conta))
                .orElse(ResponseEntity.notFound().build()); // Isso gera o 404 se não achar no banco
    }

}
package com.projeto.banco.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TransferenciaConsumer {

    private static final Logger log = LoggerFactory.getLogger(TransferenciaConsumer.class);

    // Adicione este construtor para ver se a classe inicia
    public TransferenciaConsumer() {
        System.out.println("DEBUG: O Consumidor de Kafka foi iniciado com sucesso!");
    }

    @KafkaListener(topics = "transferencia-movimentacoes-usuarios", groupId = "grupo-banco-projeto-v2")
    public void ouvirTransferencia(String mensagem) {
        // Use System.out para brilhar no console sem erro de configuração de Log
        System.out.println(">>>>>>>> MENSAGEM RECEBIDA DO KAFKA: " + mensagem);
        log.info("### KAFKA CONSUMER ### Mensagem recebida: {}", mensagem);
    }
}

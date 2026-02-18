package com.projeto.banco.repository;

import java.util.Optional;

import aj.org.objectweb.asm.commons.Remapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import com.projeto.banco.model.Conta;

import jakarta.persistence.LockModeType;

public interface ContaRepository extends JpaRepository<Conta, Long>{
	
	@Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM Conta c WHERE c.id = :id")
    Optional<Conta> findByIdWithLock(Long id);

    Optional<Conta> findByNumero(String numero);
}

package com.projeto.banco.exception;

public class NegocioException extends RuntimeException{
	
	public NegocioException(String mensagem) {
        super(mensagem);
    }

}

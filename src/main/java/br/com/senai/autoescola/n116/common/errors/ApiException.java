package br.com.senai.autoescola.n116.common.errors;

import org.springframework.http.HttpStatus;

public abstract class ApiException extends RuntimeException {
	public final HttpStatus status;
	public final String code;

	protected ApiException(HttpStatus status, String code, String message) {
		super(message);
		this.status = status;
		this.code = code;
	}
}

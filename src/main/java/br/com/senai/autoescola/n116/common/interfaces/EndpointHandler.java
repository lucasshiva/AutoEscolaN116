package br.com.senai.autoescola.n116.common.interfaces;

public interface EndpointHandler<TRequest, TResponse> {
    TResponse handle(TRequest request);
}

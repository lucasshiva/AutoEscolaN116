package br.com.senai.autoescola.n116.common.interfaces;

public interface IHandler<TRequest, TResponse> {
    TResponse handle(TRequest request);
}

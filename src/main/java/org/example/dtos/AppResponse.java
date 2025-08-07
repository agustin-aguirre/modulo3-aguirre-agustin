package org.example.dtos;

public record AppResponse<T> (
    int statusCode,
    T data,
    String errorMessage
    )
{ }

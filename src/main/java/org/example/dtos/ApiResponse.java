package org.example.dtos;

import java.util.Collection;

public record ApiResponse <T> (
    int statusCode,
    T data,
    String errorMessage
    )
{ }

package com.example.medassistant.dto;

import jakarta.validation.constraints.NotBlank;

public record ChatRequest(
        @NotBlank(message = "El prompt no puede estar vacío")
        String prompt,
        String model
) {
}

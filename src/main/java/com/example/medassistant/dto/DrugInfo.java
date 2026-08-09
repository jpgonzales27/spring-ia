package com.example.medassistant.dto;

public record DrugInfo(
        String brandName,
        String genericName,
        String purpose,
        String warnings,
        String dosageAndAdministration
) {
}

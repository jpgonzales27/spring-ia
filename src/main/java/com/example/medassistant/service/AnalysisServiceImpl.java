package com.example.medassistant.service;

import com.example.medassistant.dto.analysis.ConditionSummary;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AnalysisServiceImpl implements AnalysisService{

    private final ChatClient geminiClient;
    private final ChatClient ollamaClient;

    public AnalysisServiceImpl(
            @Qualifier("geminiClient") ChatClient geminiClient,
            @Qualifier("ollamaClient") ChatClient ollamaClient) {
        this.geminiClient = geminiClient;
        this.ollamaClient = ollamaClient;
    }

    @Override
    public ConditionSummary summarizeCondition(String condition, String model) {

        log.info("Análisis estructurado de condición: {}, modelo: {}", condition, model);
                var converter = new BeanOutputConverter<>(ConditionSummary.class);

        String format = converter.getFormat();
        log.info("Instrucciones de formato generadas:\n{}", format);

        String prompt = """
                Proporcioná un resumen médico educativo sobre: %s

                %s
                """.formatted(condition, format);

        String jsonResponse = resolveClient(model)
                .prompt(prompt)
                .call()
                .content();
        log.info("Respuesta JSON del modelo:\n{}", jsonResponse);

        return converter.convert(jsonResponse);
    }

    @Override
    public ConditionSummary summarizeCondition2(String condition, String model) {

        log.info("Análisis estructurado de condición: {}, modelo: {}", condition, model);
        return resolveClient(model)
                .prompt()
                .user("Proporcioná un resumen médico educativo sobre: " + condition)
                .call()
                .entity(ConditionSummary.class);
    }

    private ChatClient resolveClient(String model){
        return "ollama".equalsIgnoreCase(model) ? ollamaClient : geminiClient;
    }
}

package com.example.medassistant.service;

import com.example.medassistant.config.ClientResolver;
import com.example.medassistant.dto.analysis.ConditionSummary;
import com.example.medassistant.dto.analysis.SymptomAnalysis;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class AnalysisServiceImpl implements AnalysisService{

    private final ClientResolver clientResolver;

    @Value("classpath:prompts/structured-analysis.st")
    private Resource structuredAnalysisResource;

    private PromptTemplate structuredAnalysisTemplate;

    @PostConstruct
    void init(){
        structuredAnalysisTemplate = new PromptTemplate(structuredAnalysisResource);
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

        String jsonResponse = clientResolver.resolve(model)
                .prompt(prompt)
                .call()
                .content();
        log.info("Respuesta JSON del modelo:\n{}", jsonResponse);

        return converter.convert(jsonResponse);
    }

    @Override
    public ConditionSummary summarizeCondition2(String condition, String model) {

        log.info("Análisis estructurado de condición: {}, modelo: {}", condition, model);
        return clientResolver.resolve(model)
                .prompt()
                .user("Proporcioná un resumen médico educativo sobre: " + condition)
                .call()
                .entity(ConditionSummary.class);
    }

    @Override
    public List<ConditionSummary> listRelatedConditions(String symptoms, String model) {
        log.info("Listado de condiciones realacionadas - modelo: {} ", model);
        return clientResolver.resolve(model)
                .prompt()
                .user("Listá las 3 condiciones médicas más probables " +
                        "para estos síntomas: " + symptoms
                )
                .call()
                .entity(new ParameterizedTypeReference<>() {
                });
    }

    @Override
    public SymptomAnalysis analyzeSymptoms(String symptoms, String model) {
        log.info("Análisis estructurado de síntomas — modelo: {}", model);

        return clientResolver.resolve(model)
                .prompt()
                .user("Analiza los siguientes sintomas de un paciente y " +
                        "proporciona un analisis medico educativo completo: "+ symptoms)
                .call()
                .entity(SymptomAnalysis.class);
    }

    @Override
    public SymptomAnalysis analyzeSymptoms2(String symptoms, String model) {
        log.info("Análisis estructurado de síntomas — modelo: {}", model);

        String message = structuredAnalysisTemplate.render(
                Map.of("sintomas", symptoms)
        );

        return clientResolver.resolve(model)
                .prompt()
                .user(message)
                .call()
                .entity(SymptomAnalysis.class);
    }
}

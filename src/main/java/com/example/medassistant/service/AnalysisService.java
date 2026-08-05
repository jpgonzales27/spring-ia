package com.example.medassistant.service;

import com.example.medassistant.dto.analysis.ConditionSummary;
import com.example.medassistant.dto.analysis.QueryClassification;
import com.example.medassistant.dto.analysis.SymptomAnalysis;

import java.util.List;

public interface AnalysisService {
    ConditionSummary summarizeCondition(String condition, String model);
    ConditionSummary summarizeCondition2(String condition, String model);
    List<ConditionSummary> listRelatedConditions(String symptoms, String model);
    SymptomAnalysis analyzeSymptoms(String symptoms, String model);
    SymptomAnalysis analyzeSymptoms2(String symptoms, String model);
    QueryClassification classifyQuery(String query, String model);
}

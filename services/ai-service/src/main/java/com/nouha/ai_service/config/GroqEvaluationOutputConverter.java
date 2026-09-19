package com.nouha.ai_service.config;

import com.nouha.ai_service.ai.EvaluationResponse;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.node.ObjectNode;
import org.springframework.ai.converter.BeanOutputConverter;

public class GroqEvaluationOutputConverter
        extends BeanOutputConverter<EvaluationResponse> {

    public GroqEvaluationOutputConverter() {
        super(EvaluationResponse.class);
    }

    @Override
    protected String generateSchema() {

        String schema = super.generateSchema();

        try {
            JsonNode root = getJsonMapper().readTree(schema);

            JsonNode score = root
                    .path("properties")
                    .path("score");

            if (score instanceof ObjectNode scoreObject) {
                scoreObject.remove("format");
            }

            return getJsonMapper().writeValueAsString(root);

        } catch (Exception e) {
            throw new IllegalStateException(
                    "Failed to customize EvaluationResponse JSON schema",
                    e
            );
        }
    }
}
package com.myapp;

import java.util.Objects;
import java.util.UUID;
import java.util.stream.IntStream;

import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.Embedding;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingRequest;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.postgresql.PostgreSQLContainer;

@TestConfiguration(proxyBeanMethods = false)
class PgvectorTestConfiguration {

    @Bean
    @ServiceConnection
    PostgreSQLContainer postgresContainer() {
        // No fixed host port, existing container, bind mount, or reusable data volume.
        return new PostgreSQLContainer("pgvector/pgvector:0.8.6-pg18-trixie")
                .withDatabaseName("ubot_test")
                .withUsername("ubot_test")
                .withPassword(UUID.randomUUID().toString())
                .withInitScript("init.sql")
                .withLabel("com.myapp.ubot.test-db", "true")
                .withReuse(false);
    }

    @Bean
    EmbeddingModel testEmbeddingModel() {
        // This deterministic test double checks DB integration, not model quality.
        // Ollama auto-configuration is disabled in the test profile.
        return new EmbeddingModel() {
            @Override
            public EmbeddingResponse call(EmbeddingRequest request) {
                var texts = request.getInstructions();
                return new EmbeddingResponse(IntStream.range(0, texts.size())
                        .mapToObj(index -> new Embedding(vectorFor(texts.get(index)), index))
                        .toList());
            }

            @Override
            public float[] embed(Document document) {
                return vectorFor(Objects.requireNonNull(document.getText()));
            }

            @Override
            public int dimensions() {
                return 1024;
            }

            private float[] vectorFor(String text) {
                float[] vector = new float[dimensions()];
                vector[Math.floorMod(text.hashCode(), vector.length)] = 1.0f;
                return vector;
            }
        };
    }
}

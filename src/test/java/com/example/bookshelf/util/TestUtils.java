package com.example.bookshelf.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.params.provider.Arguments;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

public final class TestUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private TestUtils() {}

    public static String loadJsonFromResource(String path) throws IOException {
        Resource resource = new ClassPathResource(path);
        return new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
    }

    public static <T> T loadJson(String path, Class<T> tClass) throws IOException {
        String json = loadJsonFromResource(path);
        return objectMapper.readValue(json, tClass);
    }

    public static HttpClientErrorException createHttpException(
            HttpStatus status, String message, String json) {
        return HttpClientErrorException.create(
                status, message, HttpHeaders.EMPTY,
                json.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8
        );
    }

    public static Stream<Arguments> errorScenarios() {
        return Stream.of(
                Arguments.of("jsonResponses/global/globalBadApiKey.json",
                        HttpStatus.BAD_REQUEST,
                        "API key not valid. Please pass a valid API key."),
                Arguments.of(
                        "jsonResponses/global/globalRequiredParameterQ.json",
                        HttpStatus.BAD_REQUEST, "Required parameter: q"
                ),
                Arguments.of("jsonResponses/global/globalMissingQuery.json",
                        HttpStatus.BAD_REQUEST, "Missing query."
                ),
                Arguments.of("jsonResponses/global/globalRateLimitExceededExample.json",
                        HttpStatus.TOO_MANY_REQUESTS, "Rate Limit Exceeded"
                ),
                Arguments.of(
                        "jsonResponses/global/globalServiceTemporarilyUnavailable.json",
                        HttpStatus.SERVICE_UNAVAILABLE, "Service temporarily unavailable.")
        );
    }
}

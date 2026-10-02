package com.example.bookshelf.util;

import org.junit.jupiter.params.provider.Arguments;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

public final class TestUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private TestUtils() {
    }

    public static String loadJsonFromResource(String path) throws IOException {
        Resource resource = new ClassPathResource(path);

        try (InputStream inputStream = resource.getInputStream()) {
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    public static <T> T loadJson(String path, Class<T> tClass) throws IOException {
        String json = loadJsonFromResource(path);
        return objectMapper.readValue(json, tClass);
    }

    public static HttpStatusCodeException createHttpException(
            HttpStatus status, String json) {

        if (status.is5xxServerError()) {
            return HttpServerErrorException.create(
                    status, status.getReasonPhrase(), HttpHeaders.EMPTY,
                    json.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8
            );
        } else {
            return HttpClientErrorException.create(
                    status, status.getReasonPhrase(), HttpHeaders.EMPTY,
                    json.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8
            );
        }
    }

    public static Stream<Arguments> errorScenarios() {
        return Stream.of(
                Arguments.of("jsonResponses/global/globalBadApiKey.json",
                        HttpStatus.BAD_REQUEST
                ),
                Arguments.of(
                        "jsonResponses/global/globalRequiredParameterQ.json",
                        HttpStatus.BAD_REQUEST
                ),
                Arguments.of("jsonResponses/global/globalMissingQuery.json",
                        HttpStatus.BAD_REQUEST
                ),
                Arguments.of("jsonResponses/global/globalRateLimitExceededExample.json",
                        HttpStatus.TOO_MANY_REQUESTS
                ),
                Arguments.of(
                        "jsonResponses/global/globalServiceTemporarilyUnavailable.json",
                        HttpStatus.SERVICE_UNAVAILABLE)
        );
    }
}

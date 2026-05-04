package br.com.senai.autoescola.n116.utils;

import br.com.senai.autoescola.n116.common.errors.GlobalExceptionHandler;
import jakarta.annotation.Nullable;
import org.springframework.test.web.servlet.client.RestTestClient;

import java.time.Instant;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

public class ControllerTestUtils {
    public static void assertValidationError(
            String expectedField,
            RestTestClient.RequestHeadersSpec<?> spec,
            @Nullable Consumer<GlobalExceptionHandler.ValidationErrorResponse> extraAssertions
    ) {
        var response = spec.exchange();
        response.expectStatus().isBadRequest()
                .expectBody(GlobalExceptionHandler.ValidationErrorResponse.class)
                .value(body -> {
                    assertThat(body).isNotNull();
                    assertThat(body.errors())
                            .extracting(GlobalExceptionHandler.ValidationError::field)
                            .contains(expectedField);
                    if (extraAssertions != null) {
                        extraAssertions.accept(body);
                    }
                });
    }

    public static void assertValidationError(
            String expectedField,
            RestTestClient.RequestHeadersSpec<?> spec
    ) {
        assertValidationError(expectedField, spec, null);
    }

    public static <T> void assertCreatedWithTimestamp(
            RestTestClient.RequestHeadersSpec<?> spec,
            Class<T> responseType,
            Function<T, Instant> timestampExtractor,
            @Nullable Consumer<T> extraAssertions
    ) {
        var response = spec.exchange();
        response
                .expectStatus().isCreated()
                .expectBody(responseType)
                .value(body -> {
                    assertThat(body).isNotNull();
                    assertThat(timestampExtractor.apply(body))
                            .isNotNull()
                            .isBefore(Instant.now())
                            .isAfter(Instant.now().minusSeconds(5));

                    if (extraAssertions != null) {
                        extraAssertions.accept(body);
                    }
                });
    }

    public static <T> void assertCreatedWithTimestamp(
            RestTestClient.RequestHeadersSpec<?> response,
            Class<T> responseType,
            Function<T, Instant> timestampExtractor
    ) {
        assertCreatedWithTimestamp(response, responseType, timestampExtractor, null);
    }
}

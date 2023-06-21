package dev.country.api.util;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Slf4j
@Component
public class GenericRestClient {
    public <K, T> K post(String baseUrl, String path, T request, Class<K> clazz) {
        K resp = null;
        var requestBody = BodyInserters.fromValue(request);
        resp = WebClient.builder()
                .baseUrl(baseUrl)
                .build().post().uri(path)
                .body(requestBody)
                .exchangeToMono(response -> Optional.of(response)
                        .filter(r -> r.statusCode().is3xxRedirection())
                        .map(r -> getForRedirect(baseUrl, r, clazz))
                        .orElse(response.bodyToMono(clazz))
                ).block();

        return resp;
    }

    public <K, T> Mono<K> getForRedirect(final String url, final ClientResponse clientResponse, Class<K> clazz) {
        String path = clientResponse.headers().header(HttpHeaders.LOCATION).get(0);
        path = URLDecoder.decode(String.valueOf(path), StandardCharsets.UTF_8);
        return WebClient.builder().baseUrl(url).build().get().uri(path)
                .exchangeToMono(response -> response.bodyToMono(clazz));


    }

    public <K, T> K get(final String url, Class<K> clazz) {
        return WebClient.builder()
                .build().get().uri(url)
                .exchangeToMono(response -> response.bodyToMono(clazz)).block();
    }

    private static ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            log.info("Request: {} {}", clientRequest.method(), URLDecoder.decode(String.valueOf(clientRequest.url()), StandardCharsets.UTF_8));
            return Mono.just(clientRequest);
        });
    }


}
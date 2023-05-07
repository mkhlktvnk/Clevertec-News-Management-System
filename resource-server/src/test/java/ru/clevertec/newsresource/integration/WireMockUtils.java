package ru.clevertec.newsresource.integration;

import com.github.tomakehurst.wiremock.WireMockServer;
import lombok.experimental.UtilityClass;

import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;

@UtilityClass
public class WireMockUtils {

    public static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 9000;

    private WireMockServer wireMockServer;

    public void startServer() {
        if (wireMockServer == null) {
            wireMockServer = new WireMockServer(DEFAULT_PORT);
        }
        wireMockServer.start();
        configureFor(DEFAULT_HOST, DEFAULT_PORT);
    }

    public void stopServer() {
        wireMockServer.stop();
    }

    public void resetStubs() {
        wireMockServer.resetAll();
    }

}

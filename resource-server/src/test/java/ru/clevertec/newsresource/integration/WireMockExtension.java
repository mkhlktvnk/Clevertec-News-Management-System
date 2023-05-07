package ru.clevertec.newsresource.integration;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class WireMockExtension implements BeforeAllCallback, AfterEachCallback, AfterAllCallback {

    @Override
    public void beforeAll(ExtensionContext context) {
        WireMockUtils.startServer();
    }

    @Override
    public void afterEach(ExtensionContext context) {
        WireMockUtils.resetStubs();
    }

    @Override
    public void afterAll(ExtensionContext context) {
        WireMockUtils.stopServer();
    }

}

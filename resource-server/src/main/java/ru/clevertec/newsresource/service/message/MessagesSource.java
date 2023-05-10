package ru.clevertec.newsresource.service.message;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * A component that provides a way to retrieve messages from a MessageSource using a key and optional arguments.
 */
@Component
@RequiredArgsConstructor
public class MessagesSource {
    private final MessageSource messageSource;

    /**
     * Retrieves a message from the MessageSource using the provided key and optional arguments.
     *
     * @param key  the key of the message to retrieve
     * @param args optional arguments to be used to format the message
     * @return the retrieved message
     */
    public String get(String key, Object... args) {
        return messageSource.getMessage(key, args, Locale.getDefault());
    }
}


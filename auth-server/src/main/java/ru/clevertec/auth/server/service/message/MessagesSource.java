package ru.clevertec.auth.server.service.message;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * Component for retrieving localized messages from a {@link MessageSource}.
 */
@Component
@RequiredArgsConstructor
public class MessagesSource {

    /**
     * The message source used to retrieve messages.
     */
    private final MessageSource messageSource;

    /**
     * Get the localized message for the given key with the specified arguments.
     *
     * @param key  the key of the message to retrieve.
     * @param args the arguments to include in the message.
     * @return the localized message.
     */
    public String get(String key, Object... args) {
        return messageSource.getMessage(key, args, Locale.getDefault());
    }
}


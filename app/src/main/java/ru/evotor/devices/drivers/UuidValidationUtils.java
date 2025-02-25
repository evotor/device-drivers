package ru.evotor.devices.drivers;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

public final class UuidValidationUtils {

    private static final Pattern UUID_REGEX = Pattern.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");

    public static void checkUuid(@NotNull String uuid) throws IllegalArgumentException {
        if (!UUID_REGEX.matcher(uuid).matches()) {
            throw new IllegalArgumentException("Invalid UUID String " + uuid + " : UUID has to be represented by standard 36-char representation");
        }
    }
}

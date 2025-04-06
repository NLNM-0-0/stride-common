package com.stride.tracking.commons.utils;

import java.util.function.Consumer;

public class UpdateHelper {
    private UpdateHelper() {}

    public static <T> void updateIfNotNull(T newValue, Consumer<T> setter) {
        if (newValue != null) {
            setter.accept(newValue);
        }
    }
}

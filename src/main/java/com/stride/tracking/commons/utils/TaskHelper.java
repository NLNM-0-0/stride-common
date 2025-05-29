package com.stride.tracking.commons.utils;

import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import org.springframework.security.concurrent.DelegatingSecurityContextRunnable;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class TaskHelper {
    private TaskHelper() {
    }

    public static CompletableFuture<Void> runAsyncWithSpan(
            String spanName,
            Span parent,
            Runnable task,
            Executor asyncExecutor,
            Tracer tracer
    ) {
        return CompletableFuture.runAsync(
                () -> run(spanName, parent, task, tracer),
                asyncExecutor
        );
    }

    public static CompletableFuture<Void> runAsyncSecurityContextWithSpan(
            String spanName,
            Span parent,
            Runnable task,
            Executor asyncExecutor,
            Tracer tracer
    ) {
        return CompletableFuture.runAsync(
                new DelegatingSecurityContextRunnable(() -> run(spanName, parent, task, tracer)),
                asyncExecutor
        );
    }

    private static void run(
            String spanName,
            Span parent,
            Runnable task,
            Tracer tracer
    ) {
        if (parent != null) {
            Span span = Objects.requireNonNull(tracer.nextSpan(parent)).name(spanName).start();

            try (Tracer.SpanInScope ignored = tracer.withSpan(span)) {
                task.run();
            } finally {
                span.end();
            }
        } else {
            task.run();
        }
    }

}

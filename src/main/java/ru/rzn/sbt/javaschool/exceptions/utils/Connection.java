package ru.rzn.sbt.javaschool.exceptions.utils;

import java.io.IOException;

/**
 * Creates sessions.
 */
public interface Connection extends AutoCloseable {

    Session createSession() throws IOException;

    @Override
    void close() throws IOException;
}

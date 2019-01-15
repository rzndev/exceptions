package ru.rzn.sbt.javaschool.exceptions.utils;

import java.io.IOException;

/**
 * Creates sessions.
 */
public interface OldConnection {

    OldSession createSession() throws IOException;

    void close() throws IOException;
}

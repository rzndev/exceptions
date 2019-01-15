package ru.rzn.sbt.javaschool.exceptions.utils;

import java.io.IOException;

/**
 * Gets data.
 */
public interface OldSession {

    String getData() throws IOException;

    void close() throws IOException;
}

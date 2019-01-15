package ru.rzn.sbt.javaschool.exceptions.utils;

import java.io.IOException;

/**
 * Gets data.
 */
public interface Session extends AutoCloseable{

    String getData() throws IOException;

    @Override
    void close() throws IOException;
}

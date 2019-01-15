package ru.rzn.sbt.javaschool.exceptions.utils;

import java.io.IOException;

/**
 * Creates connections.
 */
public interface OldConnectionFactory {
    OldConnection createConnection() throws IOException;
}

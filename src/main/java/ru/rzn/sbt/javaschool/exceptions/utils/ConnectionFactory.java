package ru.rzn.sbt.javaschool.exceptions.utils;

import java.io.IOException;

/**
 * Creates connections.
 */
public interface ConnectionFactory {
    Connection createConnection() throws IOException;
}

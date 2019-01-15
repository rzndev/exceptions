package ru.rzn.sbt.javaschool.exceptions.utils;

/**
 * Логгер пишет логи.
 */
public interface Logger {

    /**
     * Записывает сообщение в лог.
     * @param message сообщение
     */
    void log(String message);
}

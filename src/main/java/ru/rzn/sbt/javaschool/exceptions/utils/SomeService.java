package ru.rzn.sbt.javaschool.exceptions.utils;

import java.io.IOException;

/**
 * Среднестатистический сервис.
 *
 * Делает полезные вещи.
 */
public interface SomeService {

    /** Сделать что-то полезное. */
    void doSomething() throws IOException;

    /** Закрыть соединение с сервисом. */
    void closeConnection();

    void showMeTheWay() throws ChildException;
}

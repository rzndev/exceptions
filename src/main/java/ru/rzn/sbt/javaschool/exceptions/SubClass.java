package ru.rzn.sbt.javaschool.exceptions;

import ru.rzn.sbt.javaschool.exceptions.utils.SomeService;

import java.io.FileNotFoundException;
import java.io.IOException;

public class SubClass extends SuperClass {
    @Override
    public String callService(SomeService service) throws FileNotFoundException {
        String data = null;
        try {
            data = super.callService(service);
            service.doSomething();
            data = "DONE";
        } catch (FileNotFoundException ex) {
            throw ex;
        } catch(Exception ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
        return data;
    }
}

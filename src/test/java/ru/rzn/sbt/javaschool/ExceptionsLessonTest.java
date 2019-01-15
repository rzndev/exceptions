package ru.rzn.sbt.javaschool;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import ru.rzn.sbt.javaschool.exceptions.ExceptionsLesson;
import ru.rzn.sbt.javaschool.exceptions.SuperClass;
import ru.rzn.sbt.javaschool.exceptions.utils.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static ru.rzn.sbt.javaschool.exceptions.ExceptionsLesson.*;

/**
 * Handling Exceptions
 *
 * OCA Objectives
 * 8.1 Differentiate among checked exceptions, RuntimeExceptions, and errors.
 * 8.2 Create a try-catch block and determine how exceptions alter normal program flow.
 * 8.3 Describe what exceptions are used for in Java.
 * 8.4 Invoke a method that throws an exception.
 * 8.5 Recognize common exception classes and categories.
 *
 * OCP
 * 6.2 Develop code that handles multiple exception types in a single catch block.
 * 6.3 Develop code that uses try-with-resources statements (including using classes that
 *     implement the AutoCloseable interface).
 * 6.5 Test invariants by using assertions.
 */
public class ExceptionsLessonTest {

    private static final String MESSAGE = "Some message";

    ExceptionsLesson instance = new ExceptionsLesson();

    @Mock
    Logger log;

    @Mock
    SomeService service;

    ArgumentCaptor<String> stringCaptor = ArgumentCaptor.forClass(String.class);

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    /* 1. logException  */

    @Test
    public void logException_noexception() throws Exception {
        instance.logException(service, log);
        verify(service).doSomething();
        verify(log, never()).log(anyString());
    }

    @Test
    public void logException_IOException() throws Exception {
        doThrow(new IOException(MESSAGE)).when(service).doSomething();
        instance.logException(service, log);
        verify(service).doSomething();
        verify(log).log(MESSAGE);
    }

    @Test
    public void logException_RTE() throws Exception {
        doThrow(new RuntimeException(MESSAGE)).when(service).doSomething();
        Exception thrown = null;
        try {
            instance.logException(service, log);
        } catch (Exception e) {
            thrown = e;
        }
        verify(service).doSomething();
        verify(log, never()).log(MESSAGE);
        assertEquals(MESSAGE, thrown.getMessage());
    }

    /* 2. closeConnection  */

    @Test
    public void closeConnection_noexception() throws Exception {
        instance.closeConnection(service, log);
        verify(service).doSomething();
        verify(log, never()).log(anyString());
        verify(service).closeConnection();
    }

    @Test
    public void closeConnection_IOException() throws Exception {
        doThrow(new IOException(MESSAGE)).when(service).doSomething();
        instance.closeConnection(service, log);
        verify(service).doSomething();
        verify(log).log(MESSAGE);
        verify(service).closeConnection();
    }

    @Test
    public void closeConnection_RTE() throws Exception {
        doThrow(new RuntimeException(MESSAGE)).when(service).doSomething();
        Exception thrown = null;
        try {
            instance.closeConnection(service, log);
        } catch (Exception e) {
            thrown = e;
        }
        verify(service).doSomething();
        verify(log, never()).log(MESSAGE);
        assertEquals(MESSAGE, thrown.getMessage());
        verify(service).closeConnection();
    }


    /* 3. getStackTrace  */

    @Test
    public void getStackTrace() throws Exception {
        Exception e = new RuntimeException(MESSAGE);
        doThrow(e).when(service).doSomething();
        //ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        instance.getStackTrace(service, log);
        verify(log, atLeastOnce()).log(stringCaptor.capture());
        List<String> messages = stringCaptor.getAllValues();
        for (String message : messages) {
            System.out.println(message);
        }
        assertEquals(listToString(Arrays.asList(e.getStackTrace())), listToString(messages));
    }

    private String listToString(List<? extends Object> list) {
        StringBuilder sb = new StringBuilder();
        for (Object o : list) {
            sb.append(o.toString());
        }
        return sb.toString().replaceAll("\\s*", "");
    }

    /* 4. showMeTheWay */

    @Test
    public void showMeTheWay_ChildException() throws Exception {
        doThrow(new ChildException()).when(service).showMeTheWay();
        instance.showMeTheWay(service, log);
        verify(log).log(stringCaptor.capture());
        List<String> messages = stringCaptor.getAllValues();
        assertEquals(1, messages.size());
        assertEquals(UNKNOWN, messages.get(0));
    }

    @Test
    public void showMeTheWay_BoyException() throws Exception {
        doThrow(new BoyException()).when(service).showMeTheWay();
        instance.showMeTheWay(service, log);
        verify(log).log(stringCaptor.capture());
        List<String> messages = stringCaptor.getAllValues();
        assertEquals(1, messages.size());
        assertEquals(RIGHT, messages.get(0));
    }

    @Test
    public void showMeTheWay_GirlException() throws Exception {
        doThrow(new GirlException()).when(service).showMeTheWay();
        instance.showMeTheWay(service, log);
        verify(log).log(stringCaptor.capture());
        List<String> messages = stringCaptor.getAllValues();
        assertEquals(1, messages.size());
        assertEquals(LEFT, messages.get(0));
    }

    @Test
    public void showMeTheWay_RTE() throws Exception {
        doThrow(new RuntimeException()).when(service).showMeTheWay();
        Exception thrown = null;
        try {
            instance.showMeTheWay(service, log);
        } catch (Exception e) {
            thrown = e;
        }
        verify(service).showMeTheWay();
        verify(log, never()).log(anyString());
        assertNotNull(thrown);
    }

    /* 5. doIt */

    @Test
    public void doIt_NoExceptions() throws Exception {
        ExceptionsLesson.Thrower thrower = new ExceptionsLesson.Thrower();
        Method method = ExceptionsLesson.Thrower.class.getMethod("doIt", int.class);
        for (int n : new int[]{1, 2, 11, 16, 1456, 45678, 111222}) {
            method.invoke(thrower, n);
        }
        assertTrue(true); // No exceptions should be thrown
    }

    @Test
    public void doIt_Exception() throws Exception {
        ExceptionsLesson.Thrower thrower = new ExceptionsLesson.Thrower();
        Method method = ExceptionsLesson.Thrower.class.getMethod("doIt", int.class);
        for (int n : new int[]{10, 20, 110, 160, 14560, 456780, 1112220}) {
            InvocationTargetException thrown = null;
            try {
                method.invoke(thrower, n);
            } catch (InvocationTargetException e) {
                thrown = e;
            }
            assertEquals(ENDS_WITH_ZERO, thrown.getTargetException().getMessage());
        }
    }

    /* 6. helloWorldException */

    @Test
    public void helloWorldException_ClassDefinition() throws Exception {
        Class hweClass = Class.forName("ru.rzn.sbt.javaschool.exceptions.ExceptionsLesson$HelloWorldException");
        assertEquals(Exception.class, hweClass.getSuperclass());
    }

    @Test
    public void helloWorldException_Constructor() throws Exception {
        Class hweClass = Class.forName("ru.rzn.sbt.javaschool.exceptions.ExceptionsLesson$HelloWorldException");
        List<Constructor> list = Arrays.asList(hweClass.getDeclaredConstructors());
        assertEquals(1, list.size());
        Constructor c = list.get(0);
        Class[] params = c.getParameterTypes();
        if (params.length == 3) { // non-static nested class
            assertEquals(ExceptionsLesson.class, c.getParameterTypes()[0]);
            assertEquals(String.class, c.getParameterTypes()[1]);
            assertEquals(int.class, c.getParameterTypes()[2]);
        } else {
            assertEquals(2, params.length);
            assertEquals(String.class, c.getParameterTypes()[0]);
            assertEquals(int.class, c.getParameterTypes()[1]);
        }
    }

    @Test
    public void helloWorldException_Thrown() throws Exception {
        Class hweClass = Class.forName("ru.rzn.sbt.javaschool.exceptions.ExceptionsLesson$HelloWorldException");
        Exception thrown = null;
        try {
            instance.helloWorldException();
        } catch (Exception e) {
            thrown = e;
        }
        assertEquals(hweClass, thrown.getClass());
        assertEquals(THROW_THE_WORLD_MSG, thrown.getMessage());
        Method getErrorCode = hweClass.getMethod("getErrorCode");
        getErrorCode.setAccessible(true);
        assertEquals(THROW_THE_WORLD_CODE, getErrorCode.invoke(thrown));
    }

    /* 7. callMe */

    @Test
    public void callMe() throws Exception {
        class Caller {
            String fhgfFhgjkKjhKLjh () { return instance.callMe(); }
            String JHGjhgJhgjhftftd () { return instance.callMe(); }
            String retTrtrTTb       () { return instance.callMe(); }
            String ytfyt$$$hgfhgf   () { return instance.callMe(); }
        }
        Caller caller = new Caller();

        final int TIMES = 5;
        for (int i=0; i< TIMES; i++) {
            Method[] methods = Caller.class.getDeclaredMethods();
            int n =  new Random().nextInt(methods.length);
            String expected = methods[n].getName();
            String actual = (String) methods[n].invoke(caller, new Object[]{});
            // System.out.println(expected + "   " + actual);
            assertEquals(expected, actual);
        }
    }

    /* 8. closeResource */

    @Test
    public void closeResource_HappyPath() throws Exception {
        OldConnection c = Mockito.mock(OldConnection.class);
        OldSession s = Mockito.mock(OldSession.class);
        final String CORRECT_DATA = "4 8 15 16 23 42";
        when(c.createSession()).thenReturn(s);
        when(s.getData()).thenReturn(CORRECT_DATA);

        String data = instance.closeResource(c, log);

        verify(s).close();
        assertEquals(CORRECT_DATA, data);
    }

    @Test
    public void closeResource_IOExceptionThrown() throws Exception {
        OldConnection c = Mockito.mock(OldConnection.class);
        OldSession s = Mockito.mock(OldSession.class);
        when(c.createSession()).thenReturn(s);
        when(s.getData()).thenThrow(new IOException("Something went wrong!!!1111"));

        String data = instance.closeResource(c, log);

        verify(s).close();
        assertEquals(null, data);
    }

    @Test
    public void closeResource_Throwable() throws Exception {
        OldConnection c = Mockito.mock(OldConnection.class);
        OldSession s = Mockito.mock(OldSession.class);
        when(c.createSession()).thenReturn(s);
        final String ERROR = "Something went terribly wrong!!!1111";
        when(s.getData()).then(i -> {throw(new Throwable(ERROR));});

        String data = null;
        Throwable thrown = null;
        try {
            data = instance.closeResource(c, log);
        } catch (Throwable e){
            thrown = e;
        }

        verify(s).close();
        assertEquals(null, data);
        assertNotNull("Never ever catch Throwable!", thrown);
        assertEquals(thrown.getMessage(), ERROR);
    }

    @Test
    public void closeResource_SessionCreationFailed() throws Exception {
        OldConnection c = Mockito.mock(OldConnection.class);
        final String ERROR = "Could not create session";
        when(c.createSession()).thenThrow(new IOException(ERROR));

        String data = instance.closeResource(c, log);

        verify(log).log(ERROR);
        assertEquals(null, data);
    }

    @Test
    public void closeResource_SessionCloseFailed() throws Exception {
        OldConnection c = Mockito.mock(OldConnection.class);
        OldSession s = Mockito.mock(OldSession.class);
        final String CORRECT_DATA = "4 8 15 16 23 42";
        when(c.createSession()).thenReturn(s);
        when(s.getData()).thenReturn(CORRECT_DATA);
        final String ERROR = "Could not close connection";
        doThrow(new IOException(ERROR)).when(s).close();

        String data = instance.closeResource(c, log);

        verify(s).close();
        verify(log).log(ERROR);
        assertEquals(CORRECT_DATA, data);
    }

    @Test
    public void closeResource_ThrowsNoExceptions() throws Exception {
        Method m = ExceptionsLesson.class.getDeclaredMethod("closeResource",
                OldConnection.class, Logger.class);
        Class[] exceptions = m.getExceptionTypes();
        assertEquals(0, exceptions.length);
    }

    /* 9. autocloseResource */

    @Test
    public void autocloseResource_HappyPath() throws Exception {
        Connection c = Mockito.mock(Connection.class);
        Session s = Mockito.mock(Session.class);
        final String CORRECT_DATA = "4 8 15 16 23 42";
        when(c.createSession()).thenReturn(s);
        when(s.getData()).thenReturn(CORRECT_DATA);

        String data = instance.autocloseResource(c, log);

        verify(s).close();
        assertEquals(CORRECT_DATA, data);
    }

    @Test
    public void autocloseResource_IOExceptionThrown() throws Exception {
        Connection c = Mockito.mock(Connection.class);
        Session s = Mockito.mock(Session.class);
        when(c.createSession()).thenReturn(s);
        when(s.getData()).thenThrow(new IOException("Something went wrong!!!1111"));

        String data = instance.autocloseResource(c, log);

        verify(s).close();
        assertEquals(null, data);
    }

    @Test
    public void autocloseResource_Throwable() throws Exception {
        Connection c = Mockito.mock(Connection.class);
        Session s = Mockito.mock(Session.class);
        when(c.createSession()).thenReturn(s);
        final String ERROR = "Something went terribly wrong!!!1111";
        when(s.getData()).then(i -> {throw(new Throwable(ERROR));});

        String data = null;
        Throwable thrown = null;
        try {
            data = instance.autocloseResource(c, log);
        } catch (Throwable e){
            thrown = e;
        }

        verify(s).close();
        assertEquals(null, data);
        assertNotNull("Never ever catch Throwable!", thrown);
        assertEquals(thrown.getMessage(), ERROR);
    }

    @Test
    public void autocloseResource_SessionCreationFailed() throws Exception {
        Connection c = Mockito.mock(Connection.class);
        final String ERROR = "Could not create session";
        when(c.createSession()).thenThrow(new IOException(ERROR));

        String data = instance.autocloseResource(c, log);

        verify(log).log(ERROR);
        assertEquals(null, data);
    }

    @Test
    public void autocloseResource_SessionCloseFailed() throws Exception {
        Connection c = Mockito.mock(Connection.class);
        Session s = Mockito.mock(Session.class);
        final String CORRECT_DATA = "4 8 15 16 23 42";
        when(c.createSession()).thenReturn(s);
        when(s.getData()).thenReturn(CORRECT_DATA);
        final String ERROR = "Could not close connection";
        doThrow(new IOException(ERROR)).when(s).close();

        String data = instance.autocloseResource(c, log);

        verify(s).close();
        verify(log).log(ERROR);
        assertEquals(CORRECT_DATA, data);
    }

    @Test
    public void autocloseResource_ThrowsNoExceptions() throws Exception {
        Method m = ExceptionsLesson.class.getDeclaredMethod("autocloseResource",
                Connection.class, Logger.class);
        Class[] exceptions = m.getExceptionTypes();
        assertEquals(0, exceptions.length);
    }

    /* 10. closeEverything */

    class StateHolder {
        boolean sessionClosed = false;
        boolean sessionClosedBeforeConnection = false;
    }

    @Test
    public void closeEverything_HappyPath() throws Exception {
        OldConnectionFactory cf = Mockito.mock(OldConnectionFactory.class);
        OldConnection c = Mockito.mock(OldConnection.class);
        OldSession s = Mockito.mock(OldSession.class);
        final String CORRECT_DATA = "4 8 15 16 23 42";
        when(cf.createConnection()).thenReturn(c);
        when(c.createSession()).thenReturn(s);
        when(s.getData()).thenReturn(CORRECT_DATA);

        StateHolder state = new StateHolder();

        doAnswer(i -> {
            if (state.sessionClosed) {state.sessionClosedBeforeConnection = true;}
            return null;
        }).when(c).close();

        doAnswer(i -> {
            state.sessionClosed = true;
            return null;
        }).when(s).close();

        String data = instance.closeEverything(cf, log);

        verify(c).close();
        verify(s).close();
        assertTrue("Session should be closed before connection!", state.sessionClosedBeforeConnection);
        assertEquals(CORRECT_DATA, data);
    }

    @Test
    public void closeEverything_IOExceptionThrown() throws Exception {
        OldConnectionFactory cf = Mockito.mock(OldConnectionFactory.class);
        OldConnection c = Mockito.mock(OldConnection.class);
        OldSession s = Mockito.mock(OldSession.class);
        when(cf.createConnection()).thenReturn(c);
        when(c.createSession()).thenReturn(s);
        when(s.getData()).thenThrow(new IOException("Something went wrong!!!1111"));

        StateHolder state = new StateHolder();

        doAnswer(i -> {
            if (state.sessionClosed) {state.sessionClosedBeforeConnection = true;}
            return null;
        }).when(c).close();

        doAnswer(i -> {
            state.sessionClosed = true;
            return null;
        }).when(s).close();

        String data = instance.closeEverything(cf, log);

        verify(c).close();
        verify(s).close();
        assertTrue("Session should be closed before connection!", state.sessionClosedBeforeConnection);
        assertEquals(null, data);
    }

    @Test
    public void closeEverything_Throwable() throws Exception {
        OldConnectionFactory cf = Mockito.mock(OldConnectionFactory.class);
        OldConnection c = Mockito.mock(OldConnection.class);
        OldSession s = Mockito.mock(OldSession.class);
        when(cf.createConnection()).thenReturn(c);
        when(c.createSession()).thenReturn(s);
        final String ERROR = "Something went terribly wrong!!!1111";
        when(s.getData()).then(i -> {throw(new Throwable(ERROR));});

        StateHolder state = new StateHolder();

        doAnswer(i -> {
            if (state.sessionClosed) {state.sessionClosedBeforeConnection = true;}
            return null;
        }).when(c).close();

        doAnswer(i -> {
            state.sessionClosed = true;
            return null;
        }).when(s).close();

        String data = null;
        Throwable thrown = null;
        try {
            data = instance.closeEverything(cf, log);
        } catch (Throwable e){
            thrown = e;
        }

        verify(c).close();
        verify(s).close();
        assertTrue("Session should be closed before connection!", state.sessionClosedBeforeConnection);
        assertEquals(null, data);
        assertNotNull("Never ever catch Throwable!", thrown);
        assertEquals(thrown.getMessage(), ERROR);
    }

    @Test
    public void closeEverything_ConnectionFailed() throws Exception {
        OldConnectionFactory cf = Mockito.mock(OldConnectionFactory.class);
        final String ERROR = "Could not create connection";
        when(cf.createConnection()).thenThrow(new IOException(ERROR));

        String data = instance.closeEverything(cf, log);

        verify(log).log(ERROR);
        assertEquals(null, data);
    }

    @Test
    public void closeEverything_ConnectionCloseFailed() throws Exception {
        OldConnectionFactory cf = Mockito.mock(OldConnectionFactory.class);
        OldConnection c = Mockito.mock(OldConnection.class);
        OldSession s = Mockito.mock(OldSession.class);
        final String CORRECT_DATA = "4 8 15 16 23 42";
        when(cf.createConnection()).thenReturn(c);
        when(c.createSession()).thenReturn(s);
        when(s.getData()).thenReturn(CORRECT_DATA);
        final String ERROR = "Could not close connection";
        doThrow(new IOException(ERROR)).when(c).close();

        String data = instance.closeEverything(cf, log);

        verify(c).close();
        verify(s).close();
        verify(log).log(ERROR);
        assertEquals(CORRECT_DATA, data);
    }

    @Test
    public void closeEverything_ThrowsNoExceptions() throws Exception {
        Method m = ExceptionsLesson.class.getDeclaredMethod("closeEverything",
                OldConnectionFactory.class, Logger.class);
        Class[] exceptions = m.getExceptionTypes();
        assertEquals(0, exceptions.length);
    }

    /* 11. autocloseEverything */

    @Test
    public void autocloseEverything_HappyPath() throws Exception {
        ConnectionFactory cf = Mockito.mock(ConnectionFactory.class);
        Connection c = Mockito.mock(Connection.class);
        Session s = Mockito.mock(Session.class);
        final String CORRECT_DATA = "4 8 15 16 23 42";
        when(cf.createConnection()).thenReturn(c);
        when(c.createSession()).thenReturn(s);
        when(s.getData()).thenReturn(CORRECT_DATA);

        StateHolder state = new StateHolder();

        doAnswer(i -> {
            if (state.sessionClosed) {state.sessionClosedBeforeConnection = true;}
            return null;
        }).when(c).close();

        doAnswer(i -> {
            state.sessionClosed = true;
            return null;
        }).when(s).close();

        String data = instance.autocloseEverything(cf, log);

        verify(c).close();
        verify(s).close();
        assertTrue("Session should be closed before connection!", state.sessionClosedBeforeConnection);
        assertEquals(CORRECT_DATA, data);
    }

    @Test
    public void autocloseEverything_IOExceptionThrown() throws Exception {
        ConnectionFactory cf = Mockito.mock(ConnectionFactory.class);
        Connection c = Mockito.mock(Connection.class);
        Session s = Mockito.mock(Session.class);
        when(cf.createConnection()).thenReturn(c);
        when(c.createSession()).thenReturn(s);
        when(s.getData()).thenThrow(new IOException("Something went wrong!!!1111"));

        StateHolder state = new StateHolder();

        doAnswer(i -> {
            if (state.sessionClosed) {state.sessionClosedBeforeConnection = true;}
            return null;
        }).when(c).close();

        doAnswer(i -> {
            state.sessionClosed = true;
            return null;
        }).when(s).close();

        String data = instance.autocloseEverything(cf, log);

        verify(c).close();
        verify(s).close();
        assertTrue("Session should be closed before connection!", state.sessionClosedBeforeConnection);
        assertEquals(null, data);
    }

    @Test
    public void autocloseEverything_Throwable() throws Exception {
        ConnectionFactory cf = Mockito.mock(ConnectionFactory.class);
        Connection c = Mockito.mock(Connection.class);
        Session s = Mockito.mock(Session.class);
        when(cf.createConnection()).thenReturn(c);
        when(c.createSession()).thenReturn(s);
        final String ERROR = "Something went terribly wrong!!!1111";
        when(s.getData()).then(i -> {throw(new Throwable(ERROR));});

        StateHolder state = new StateHolder();

        doAnswer(i -> {
            if (state.sessionClosed) {state.sessionClosedBeforeConnection = true;}
            return null;
        }).when(c).close();

        doAnswer(i -> {
            state.sessionClosed = true;
            return null;
        }).when(s).close();

        String data = null;
        Throwable thrown = null;
        try {
            data = instance.autocloseEverything(cf, log);
        } catch (Throwable e){
            thrown = e;
        }

        verify(c).close();
        verify(s).close();
        assertTrue("Session should be closed before connection!", state.sessionClosedBeforeConnection);
        assertEquals(null, data);
        assertNotNull("Never ever catch Throwable!", thrown);
        assertEquals(thrown.getMessage(), ERROR);
    }

    @Test
    public void autocloseEverything_ConnectionFailed() throws Exception {
        ConnectionFactory cf = Mockito.mock(ConnectionFactory.class);
        final String ERROR = "Could not create connection";
        when(cf.createConnection()).thenThrow(new IOException(ERROR));

        String data = instance.autocloseEverything(cf, log);

        verify(log).log(ERROR);
        assertEquals(null, data);
    }

    @Test
    public void autocloseEverything_ConnectionCloseFailed() throws Exception {
        ConnectionFactory cf = Mockito.mock(ConnectionFactory.class);
        Connection c = Mockito.mock(Connection.class);
        Session s = Mockito.mock(Session.class);
        final String CORRECT_DATA = "4 8 15 16 23 42";
        when(cf.createConnection()).thenReturn(c);
        when(c.createSession()).thenReturn(s);
        when(s.getData()).thenReturn(CORRECT_DATA);
        final String ERROR = "Could not close connection";
        doThrow(new IOException(ERROR)).when(c).close();

        String data = instance.autocloseEverything(cf, log);

        verify(c).close();
        verify(s).close();
        verify(log).log(ERROR);
        assertEquals(CORRECT_DATA, data);
    }

    @Test
    public void autocloseEverything_ThrowsNoExceptions() throws Exception {
        Method m = ExceptionsLesson.class.getDeclaredMethod("autocloseEverything",
                ConnectionFactory.class, Logger.class);
        Class[] exceptions = m.getExceptionTypes();
        assertEquals(0, exceptions.length);
    }

    /* 12. helloBarbara */
    @Test
    public void helloBarbara_class() throws Exception {
        Class clazz = helloBarbara_getClass();
        assertTrue("SubClass should extend SuperClass", SuperClass.class.isAssignableFrom(clazz));
    }

    @Test
    public void helloBarbara_done() throws Exception {
        Method method = helloBarbara_getMethod();
        Object o = helloBarbara_getClass().newInstance();
        doNothing().when(service).doSomething();

        String result = (String) method.invoke(o, service);

        verify(service).doSomething();
        assertEquals("Methoud callService should return 'DONE'", "DONE", result);
    }

    @Test
    public void helloBarbara_rethrowsFileNotFoundException() throws Exception {
        Method method = helloBarbara_getMethod();
        Object o = helloBarbara_getClass().newInstance();
        Exception exception = new FileNotFoundException(MESSAGE);
        doThrow(exception).when(service).doSomething();

        Throwable thrown = null;
        try {
            method.invoke(o, service);
        } catch (InvocationTargetException e) {
            thrown = e.getTargetException();
        }

        verify(service).doSomething();
        assertEquals("Methoud callService should rethrow the same exception", exception, thrown);
    }

    @Test
    public void helloBarbara_throwsRTE() throws Exception {
        Method method = helloBarbara_getMethod();
        Object o = helloBarbara_getClass().newInstance();
        Exception exception = new IOException(MESSAGE);
        doThrow(exception).when(service).doSomething();

        Throwable thrown = null;
        try {
            method.invoke(o, service);
        } catch (InvocationTargetException e) {
            thrown = e.getTargetException();
        }

        verify(service).doSomething();
        assertNotNull("Methoud callService should throw unchecked exception", thrown);
        assertTrue("Methoud callService should throw UNCHECKED exception",
                RuntimeException.class.isAssignableFrom(thrown.getClass()));
        assertEquals("Methoud callService should throw exception with the same message",
                exception.getMessage(), thrown.getMessage());
        assertEquals("Methoud callService should specify cause for thrown exception",
                exception, thrown.getCause());
    }

    private Class helloBarbara_getClass() throws Exception {
        return Class.forName("ru.rzn.sbt.javaschool.exceptions.SubClass");
    }

    private Method helloBarbara_getMethod() throws Exception {
        Class clazz = helloBarbara_getClass();
        return clazz.getMethod("callService", SomeService.class);
    }
}

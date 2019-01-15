package ru.rzn.sbt.javaschool;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.rzn.sbt.javaschool.basics.BasicsLesson;
import ru.rzn.sbt.javaschool.basics.utils.Logger;
import ru.rzn.sbt.javaschool.basics.utils.StringSupplier;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class BasicsLessonTest {

    private BasicsLesson instance = new BasicsLesson();

    private ArgumentCaptor<String> stringCaptor = ArgumentCaptor.forClass(String.class);

    @Mock
    Logger log;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getSeason() throws Exception {
        String [][] data = {
                {"ДЕКАБРЬ" , "ЗИМА"      }, {"ЯНВАРЬ" , "ЗИМА"       }, {"ФЕВРАЛЬ", "ЗИМА"       },
                {"МАРТ"    , "ВЕСНА"     }, {"АПРЕЛЬ" , "ВЕСНА"      }, {"МАЙ"    , "ВЕСНА"      },
                {"ИЮНЬ"    , "ЛЕТО"      }, {"ИЮЛЬ"   , "ЛЕТО"       }, {"АВГУСТ" , "ЛЕТО"       },
                {"СЕНТЯБРЬ", "ОСЕНЬ"     }, {"ОКТЯБРЬ", "ОСЕНЬ"      }, {"НОЯБРЬ" , "ОСЕНЬ"      },
                {"БЕРЕЗЕНЬ", "НЕТ ДАННЫХ"}, {"ВЕРЕСЕНЬ", "НЕТ ДАННЫХ"}, {"КВИТЕНЬ" , "НЕТ ДАННЫХ"},
        };
        for (String[] d: data) {
            assertEquals(d[0], d[1], instance.getSeason(d[0]));
        };
    }

    @Test
    public void xLittleIndians() {
        for (int i = 2 + (int) (Math.random() * 5); i > 0; i--) {
            reset(log);
            ArgumentCaptor<String> logged = ArgumentCaptor.forClass(String.class);
            int x = 1 + (int) (Math.random() * 10);
            String expected = "";
            for (int j = x; j>0; j--) {
                expected += j + " little indian" + (j>1 ? "s": "") + "\n";
            }
            instance.xLittleIndians(x, log);
            verify(log, times(x)).log(logged.capture());
            String actual = logged.getAllValues().stream()
                    .map(s -> s.replace("\n", ""))
                    .reduce((s, si) -> s + "\n" + si)
                    .orElse("")
                    + "\n";
            assertEquals("From " + x + " to 1", expected, actual);
        }
    }

    @Test
    public void logEverything_full() {
        List<String> strings = IntStream.range(0, 1 + (int) (Math.random() * 10))
                .mapToObj(n -> IntStream.range(0, 1 + (int) (Math.random() * 10))
                        .mapToObj(l -> new String("" + (char)(33 + Math.random() * 80)))
                        .collect(Collectors.joining())
                ).collect(Collectors.toList());

        StringSupplier supplier = new StringSupplier() {
            private Iterator<String> i = strings.iterator();
            public boolean hasNext() { return i.hasNext(); }
            public String next() { return i.next(); }
        };

        instance.logEverything(supplier, log);
        verify(log, times(strings.size())).log(stringCaptor.capture());

    }

    @Test
    public void logEverything_empty() {
        StringSupplier supplier = new StringSupplier() {
            public boolean hasNext() { return false; }
            public String next() { return null; }
        };
        instance.logEverything(supplier, log);
        verify(log, never()).log(anyString());
    }

    @Test
    public void littleKid() {
        String [][] data = {
                {"МАМА", "МАМА"}, {"ЯНВАРЬ" , "ЯНВАЛЬ"}, {"ФЕВРАЛЬ"  , "ФЕВЛАЛЬ"  }, {""  , ""},
                {"БРАТ", "БЛАТ"}, {"АРКАША" , "АЛКАША"}, {"ТАРАТОРКА", "ТАЛАТОЛКА"},
        };
        for (String[] d: data) {
            assertEquals(d[0], d[1], instance.littleKid(d[0]).toUpperCase());
        };
    }

    @Test
    public void newClass() throws Exception {
        Class clazz = Class.forName("ru.rzn.sbt.javaschool.basics.LethalWeapon");
        Field color = clazz.getField("color");
        assertEquals(String.class, color.getType());
        assertEquals("public", Modifier.toString(color.getModifiers()));

        Field roundsLeft = clazz.getDeclaredField("roundsLeft");
        assertEquals(int.class, roundsLeft.getType());
        assertEquals("private", Modifier.toString(roundsLeft.getModifiers()));

        Method pewPew = clazz.getMethod("pewPew");
        Method reload = clazz.getMethod("reload", int.class);
        Object o = instance.newClass();
        reload.invoke(o, 10);
        pewPew.invoke(o);
    }

    @Test
    public void serial() throws Exception {
        Class clazz = Class.forName("ru.rzn.sbt.javaschool.basics.LethalWeapon");
        Constructor constructor = clazz.getConstructor(String.class, Double.class, int.class);
        constructor.newInstance("green", 1.0, 10);
        constructor.newInstance("yellow", 2.0, 10);
        Object o = constructor.newInstance("black", 3.0, 10);
        Method getSerial = clazz.getMethod("getSerial");
        assertEquals(2L, getSerial.invoke(o));
    }

    @Test
    public void equalsContract() throws Exception {
        Class clazz = Class.forName("ru.rzn.sbt.javaschool.basics.LethalWeapon");
        Constructor constructor = clazz.getConstructor(String.class, Double.class, int.class);
        Object o1 = constructor.newInstance("green", 1.0, 10);
        Object o2 = constructor.newInstance("green", 1.0, 10);

        assertEquals(o1, o2);
        assertEquals(o1.hashCode(), o2.hashCode());
    }

    @Test
    public void stringExercise1() throws Exception {
        String s = ";sdf;4sHlgoGLG938222LG5kbFSdfbk6633";
        String result = instance.stringExercise1(s);
        Assert.assertEquals("stringExercise1 test failed!", result, "df;4sHlgoGLG938222LG5kbFS".toUpperCase());
    }

    @Test
    public void stringExercise2() throws Exception {
        String s = " LkRo,Dl-87 , as9,LJ - lls093jjj   ";
        int result = instance.stringExercise2(s);
        Assert.assertTrue("stringExercise2 test failed!",
                result == s.length() * 3 * 2 * s.indexOf("R") * s.lastIndexOf("L") * 4);
    }

    @Test
    public void stringExercise3() throws Exception {
        List<String> strings = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        map.put("   Hello wonderful world!", "Hello world!");
        map.put("Hello world!", "!dlrow olleH");
        map.put(" Goodbye cruel world    ", "Goodbye world!");
        map.put("Goodbye cruel world!", "Goodbye world");
        map.put("Goodbye world", "world goodbye");

        for (Map.Entry<String, String> entry : map.entrySet()) {
            String result = instance.stringExercise3(entry.getKey());
            Assert.assertEquals("stringExercise3 test failed!", result, entry.getValue());
        }
    }
}

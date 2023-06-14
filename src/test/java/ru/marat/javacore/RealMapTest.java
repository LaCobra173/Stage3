package ru.marat.javacore;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RealMapTest {
    RealMap<String, Integer> map;
    RealMap<String, Integer> expected;

    @BeforeEach
    void setUp() {
        map = new RealMap<>();
        map.put("Test", 7);
        map.put("Alex", 36);
        map.put("Gordan", 170);

        expected = new RealMap<>();
        expected.put("Jira", 11);
        expected.put("Bob", 322);
        expected.put("Gyara", 25);
    }
    @Test
    void size() throws InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(2);
        CountDownLatch latch = new CountDownLatch(5);
        System.out.println("---");
        for (int i = 0; i < 5; i++) {
            int value = i + 1;
            service.execute(() -> {
                map.put("Java" + value, 123 + value);
                map.put("Getter" + value, 15 + value);
                map.put("Setter" + value, 54 + value);
                latch.countDown();
            });
        }

        latch.await();
        System.out.println("Map size = " + map.size());
        System.out.println("----");
        service.shutdown();

//        int size = 17;
//        assertEquals(size, map.size());
    }

    @Test
    void isEmpty() {
        map.put("Test1", 12);
        map.put("Test2", 7);
        map.put("Test3", 77);
        System.out.println("---");

        ExecutorService service = Executors.newFixedThreadPool(3);
        Future<Boolean> future = service.submit(() -> {
            for (int i = 0; i < 3; i++) {
                map.remove("Test1");
                map.remove("Test2");
                map.remove("Test3");
                map.remove("Test");
                map.remove("Alex");
                map.remove("Gordan");
            }
            return map.isEmpty();
        });

        try {
            boolean result = future.get();
            System.out.println("Map is empty? - " + result);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println("---");
        service.shutdown();

//        map.clear();
//        int size = 0;
//        assertEquals(size, map.size());
    }

    @Test
    void containsKey() {
//        boolean result = true;
//        assertEquals(result, map.containsKey("Alex"));
    }

    @Test
    void containsValue() {

        //boolean result = false;
        //assertEquals(result, map.containsValue(100));
    }

    @Test
    void get() {
        //int value = 170;
        //assertEquals(value, map.get("Gordan"));
    }

    @Test
    void remove() throws InterruptedException {
        map.put("Test1", 12);
        map.put("Test3", 7);
        map.put("Test2", 71);
        map.put("Test4", 5);
        map.put("Test8", 120);
        map.put("Test7", 55);
        map.put("Test9", 69);
        System.out.println("---");
        System.out.println("Map at the beginning contains: " + map.toString());

        ExecutorService service = Executors.newFixedThreadPool(2);
        CountDownLatch latch = new CountDownLatch(5);

        service.execute(() -> {
            for (int i = 1; i < 6; i++) {
                int value = i;
                map.remove("Test" + value);
                latch.countDown();
            }
        });
        latch.await();
        System.out.println("Map must contains: {Test=7, Test7=55, Test8=120, Gordan=170, Test9=69, Alex=36}");
        System.out.println("Map at the end contains: " + map.toString());
        System.out.println("---");
        service.shutdown();

        //int oldValue = 36;
        //assertEquals(oldValue, map.remove("Alex"));
    }

    @Test
    void putAll() throws InterruptedException{
        System.out.println("---");
        ExecutorService service = Executors.newFixedThreadPool(2);
        CountDownLatch latch = new CountDownLatch(5);
        service.execute(() -> {
            RealMap<String, Integer> addedMap = new RealMap<>();
            for (int i = 0; i < 5; i++) {
                addedMap.put("Acer#" + (int) Math.random() * 100, (int) Math.random() * 1000);
                latch.countDown();
            }
            map.putAll(addedMap);
        });

        latch.await();
        System.out.println(map.toString());
        System.out.println("---");
        service.shutdown();

        //map.putAll(expected);
    }

    @Test()
    void put() throws InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(2);
        CountDownLatch latch = new CountDownLatch(5);
        System.out.println("---");
        System.out.println("Map must contains: {Test=7, Alex=36, Gordan=170, Java SE5=11, Java SE4=10, Java SE3=9, Java SE2=8, Java SE1=7}");

        for (int i = 0; i < 5; i++) {
            int value = i + 1;
            service.execute(() -> {
                map.put("Java SE" + value, 6 + value);
                map.containsKey("Java SE" + value);
                latch.countDown();
            });
        }

        latch.await();
        System.out.println("Map contains: " + map.toString());
        System.out.println("---");
        service.shutdown();

        //int result = 36;
        //assertEquals(result, map.put("Alex", 39));
    }

    @Test
    void clear() {
        //int result = 0;
        //map.clear();
        //assertEquals(result, map.size());
    }

    @Test
    void keySet() {
//        Collection result = new HashSet();
//        result.add("Alex");
//        result.add("Test");
//        result.add("Gordan");
//        assertEquals(result, map.keySet());
    }

    @Test
    void values() {
//        Collection result = new HashSet();
//        result.add(7);
//        result.add(36);
//        result.add(170);
//        assertEquals(result, map.values());
    }

    @Test
    void entrySet() {
    }
}
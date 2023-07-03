package ru.marat.javacore;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RealMap<K,V> implements Map<K,V> {
    private LinkedList<Entry<K,V>>[] buckets;
    private int capacity;
    private int size;
    private static class Entry<K, V> {
        private final K key;
        private V value;
        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
        public K getKey() {
            return key;
        }
        public V getValue() {
            return value;
        }
        public void setValue(V value) {
            this.value = value;
        }
    }
    public RealMap() {
        capacity = 16;
        size = 0;
        buckets = IntStream.range(0, capacity)
                .mapToObj(i -> new LinkedList<>())
                .toArray(LinkedList[]::new);
    }
    private int hash(Object key) {
        return Math.abs(key.hashCode() % capacity);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        int index = hash(key);
        LinkedList<Entry<K, V>> bucket = buckets[index];
        return bucket.stream().
                anyMatch(entry -> entry.getKey().equals(key));
    }

    @Override
    public boolean containsValue(Object value) {
        return Arrays.stream(buckets)
                .flatMap(Collection::stream)
                .anyMatch(entry -> entry.getValue().equals(value));
    }

    @Override
    public V get(Object key) {
        int index = hash(key);
        LinkedList<Entry<K, V>> bucket = buckets[index];
        Optional<V> element = bucket.stream()
                .filter(kvEntry -> kvEntry.getKey().equals(key))
                .map(Entry::getValue)
                .findAny();
        return element.isPresent() ? element.get() : null;
    }

    @Override
    public V put(K key, V value) {
        int index = hash(key);
        LinkedList<Entry<K, V>> bucket = buckets[index];

        Optional<V> oldValue = bucket.stream()
                .filter(entry -> entry.getKey().equals(key)).findFirst()
                .map(e -> {
                    V val = e.getValue();
                    e.setValue(value);
                    return val;
                });

        if (oldValue.isEmpty()) {
            bucket.add(new Entry<>(key, value));
            size++;
        }

        return oldValue.isEmpty() ? null : oldValue.get();
    }

    @Override
    public V remove(Object key) {
        int index = hash((K)key);
        LinkedList<Entry<K, V>> bucket = buckets[index];

        LinkedList<Entry<K, V>> sortedLinkedList = bucket.stream()
                .filter(f -> !f.getKey().equals(key))
                .collect(Collectors.toCollection(LinkedList::new));

        Optional<V> oldValue = bucket.stream()
                .filter(e -> e.getKey().equals(key))
                .findFirst()
                .map(Entry::getValue);

        if (oldValue.isEmpty()) {
            return null;
        }
        else {
            buckets[index] = sortedLinkedList;
            size--;
            return oldValue.get();
        }
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        m.entrySet().stream()
                .forEach((Consumer<Map.Entry<? extends K, ? extends V>>) entry -> put(entry.getKey(), entry.getValue()));
    }

    @Override
    public void clear() {
        size = 0;
        buckets = new LinkedList[capacity];
    }

    @Override
    public Set<K> keySet() {
        return Arrays.stream(buckets)
                .flatMap(Collection::stream)
                .map(Entry::getKey)
                .collect(Collectors.toSet());
    }

    @Override
    public Collection<V> values() {
        return Arrays.stream(buckets)
                .flatMap(Collection::stream)
                .map(Entry::getValue)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        return Arrays.stream(buckets)
                .flatMap(Collection::stream)
                .map(m -> {
                    return Map.entry(m.getKey(), m.getValue());
                })
                .collect(Collectors.toSet());
    }

    @Override
    public String toString() {
        String str = "";
        for (int i = 0; i < capacity; i++) {
            LinkedList<Entry<K,V>> bucket = buckets[i];
            for (Entry entry : bucket) {
                str+= entry.getKey() + "=" + entry.getValue() + ", ";
            }
        }
        String s = str.substring(0, str.length() - 2);

        return "{" + s + '}';
    }
}

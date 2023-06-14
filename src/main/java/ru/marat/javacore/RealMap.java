package ru.marat.javacore;

import java.awt.List;
import java.util.*;
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
        buckets = new LinkedList[capacity];
        for (int i = 0; i < capacity; i++) {
            buckets[i] = new LinkedList<>();
        }
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
        for (Entry<K, V> entry : bucket) {
            if (entry.getKey().equals(key))
                return true;
        }
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        for (LinkedList<Entry<K, V>> bucket : buckets) {
            for (Entry<K, V> entry : bucket) {
                if (entry.getValue().equals(value))
                    return true;
            }
        }
        return false;
    }

    @Override
    public V get(Object key) {
        int index = hash(key);
        LinkedList<Entry<K, V>> bucket = buckets[index];
        for (Entry<K, V> entry : bucket) {
            if (entry.getKey().equals(key)) {
                return entry.getValue();
            }
        }
        return null;
    }

    @Override
    public V put(K key, V value) {
        int index = hash(key);
        LinkedList<Entry<K, V>> bucket = buckets[index];
        V oldValue = null;
        for (Entry<K, V> entry : bucket) {
            if (entry.getKey().equals(key)) {
                oldValue = entry.getValue();
                entry.setValue(value);
            }
        }
        bucket.add(new Entry<>(key,value));
        size++;

        return oldValue;
    }

    @Override
    public V remove(Object key) {
        int index = hash((K)key);
        V oldValue = null;
        LinkedList<Entry<K, V>> bucket = buckets[index];
        for (Entry<K, V> entry : bucket) {
            if (entry.getKey().equals(key)) {
                oldValue = entry.getValue();
                bucket.remove(entry);
                size--;
            }
        }

        return oldValue;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        for (Map.Entry<? extends K, ? extends V> map : m.entrySet()) {
            K key = map.getKey();
            V value = map.getValue();
            put(key, value);
        }
    }

    @Override
    public void clear() {
        size = 0;
        for (int i = 0; i < capacity; i++) {
            buckets[i] = null;
        }
    }

    @Override
    public Set<K> keySet() {
        Set<K> setCollection = new HashSet<>();
        for (LinkedList<Entry<K, V>> bucket : buckets) {
            for (Entry<K, V> entry : bucket) {
                setCollection.add(entry.getKey());
            }
        }
        return setCollection;
    }

    @Override
    public Collection<V> values() {
        Collection<V> collection = new HashSet<>();
        for (LinkedList<Entry<K, V>> bucket : buckets) {
            for (Entry<K, V> entry : bucket) {
                collection.add(entry.getValue());
            }
        }
        return collection;
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        Set<Map.Entry<K, V>> setCollection = new HashSet<>();
        for (LinkedList<Entry<K, V>> bucket : buckets) {
            for (Entry<K, V> entry : bucket) {
                setCollection.add((Map.Entry<K, V>) entry);
            }
        }
        return setCollection;
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

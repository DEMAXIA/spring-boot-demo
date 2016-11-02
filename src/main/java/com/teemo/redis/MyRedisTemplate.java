package com.teemo.redis;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.ScanOptions.ScanOptionsBuilder;
import org.springframework.data.redis.serializer.RedisSerializer;



@Configuration
public class MyRedisTemplate extends RedisTemplate<Object, Object> {

    public static final TimeUnit TIME_UNIT = TimeUnit.SECONDS;

    @Override
    public RedisSerializer<Object> getKeySerializer() {
        return (RedisSerializer<Object>) super.getKeySerializer();
    }

    @Override
    public RedisSerializer<Object> getValueSerializer() {
        return (RedisSerializer<Object>) super.getValueSerializer();
    }

    @Override
    public RedisSerializer<Object> getHashKeySerializer() {
        return (RedisSerializer<Object>) super.getHashKeySerializer();
    }

    @Override
    public RedisSerializer<Object> getHashValueSerializer() {
        return (RedisSerializer<Object>) super.getHashValueSerializer();
    }

    public void set(final Object key, final Object value) {
        this.boundValueOps(key).set(value);
    }

    public void set(final Object key, final Object value, final long timeout) {
        this.boundValueOps(key).set(value, timeout, TIME_UNIT);
    }

    public void set(final Map<Object, Object> map) {
        this.execute(new RedisCallback<Boolean>() {

            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                if (map.isEmpty()) {
                    return false;
                }
                Iterator<Object> iterator = map.keySet().iterator();
                Object key = null;
                while (iterator.hasNext()) {
                    key = iterator.next();
                    if (!(key instanceof String)) {
                        continue;
                    }
                    if (key.toString().endsWith(":int")) {
                        connection.set(getKeySerializer().serialize(key), getValueSerializer().serialize(Integer.parseInt(map.get(key).toString())));
                    } else {
                        connection.set(getKeySerializer().serialize(key), getValueSerializer().serialize(map.get(key)));
                    }

                }
                return true;
            }
        });
    }

    public <T> T get(Object key) {
        return (T) this.opsForValue().get(key);
    }

    public void hSet(Object key, Object hashKey, Object value) {
        BoundHashOperations<Object, Object, Object> boundHashOps = this.boundHashOps(key);
        boundHashOps.put(hashKey, value);
    }

    public void hMSet(Object key, Map<? extends Object, ? extends Object> map) {
        BoundHashOperations<Object, Object, Object> boundHashOps = this.boundHashOps(key);
        boundHashOps.putAll(map);
    }

    public void hDel(Object key, Object... hashKey) {
        BoundHashOperations<Object, Object, Object> boundHashOps = this.boundHashOps(key);
        boundHashOps.delete(hashKey);
    }

    public <T> T hGetAll(Object key) {
        return (T) this.opsForHash().entries(key);
    }

    public <T> T hGet(Object key, Object hashKey) {
        return (T) this.opsForHash().get(key, hashKey.toString());
    }

    public <T> List<T> fuzzySearchKeys(final String pattern) {
        return this.execute(new RedisCallback<List<T>>() {

            @Override
            public List<T> doInRedis(RedisConnection connection) throws DataAccessException {
                List<T> keys = new ArrayList<T>();
                ScanOptionsBuilder scanOptions = ScanOptions.scanOptions();
                scanOptions.match(pattern);
                Cursor<byte[]> scan = connection.scan(scanOptions.build());
                while (scan.hasNext()) {
                    keys.add((T) getKeySerializer().deserialize(scan.next()));
                }
                return keys;
            }
        });
    }

    // public <K, V> Map<K, V> fuzzySearchKeyValues(final String pattern) {
    // return this.execute(new SessionCallback<Map<K, V>>() {
    //
    // @Override
    // public Map<K, V> execute(RedisOperations operations) throws DataAccessException {
    // Map<K, V> keyValues = new HashMap<K, V>();
    // List<Object> fuzzySearchKeys = fuzzySearchKeys(pattern);
    // for (final Object key : fuzzySearchKeys) {
    // DataType type = operations.type(key);
    // if (type == DataType.HASH) {
    // keyValues.put((K) key, (V) operations.opsForHash().entries(key));
    // } else if (type == DataType.SET) {
    // keyValues.put((K) key, (V) operations.opsForSet().members(key));
    // } else if (type == DataType.LIST) {
    // ListOperations opsForList = operations.opsForList();
    // keyValues.put((K) key, (V) opsForList.range(key, 0, opsForList.size(key)));
    // } else if (type == DataType.STRING) {
    // // System.out.println(operations.opsForValue().get(key));
    // keyValues.put((K) key, (V) operations.opsForValue().get(key));
    // }
    // }
    // return keyValues;
    // }
    // });
    // }

    public <T> List<T> lRange(Object key, long start, long end) {
        return (List<T>) this.opsForList().range(key, start, end);
    }
}

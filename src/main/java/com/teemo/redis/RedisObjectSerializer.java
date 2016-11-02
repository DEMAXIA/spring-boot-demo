package com.teemo.redis;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.serializer.support.DeserializingConverter;
import org.springframework.core.serializer.support.SerializingConverter;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

/**
 * Created by tangjingxiang on 2016/11/2.
 */
public class RedisObjectSerializer implements RedisSerializer<Object> {

    //序列化
    private Converter<Object, byte[]> serializer = new SerializingConverter();
    //反序列化
    private Converter<byte[], Object> deserializer = new DeserializingConverter();


    static final byte[] EMPTY_ARRAY = new byte[0];

    /**
     * 序列化
     * @param object
     * @return
     * @throws SerializationException
     */
    @Override
    public byte[] serialize(Object object) throws SerializationException {
        if (object == null) {
            return EMPTY_ARRAY;
        }
        try {
            return serializer.convert(object);
        } catch (Exception ex) {
            return EMPTY_ARRAY;
        }
    }

    /**
     * 反序列化
     * @param bytes
     * @return
     * @throws SerializationException
     */
    @Override
    public Object deserialize(byte[] bytes) throws SerializationException {
        if (isEmpty(bytes)) {
            return null;
        }
        try {
            return deserializer.convert(bytes);
        } catch (Exception ex) {
            throw new SerializationException("Cannot deserialize", ex);
        }
    }


    private boolean isEmpty(byte[] data) {
        return (data == null || data.length == 0);
    }
}

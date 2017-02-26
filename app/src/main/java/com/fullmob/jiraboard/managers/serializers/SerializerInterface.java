package com.fullmob.jiraboard.managers.serializers;

import com.google.gson.reflect.TypeToken;

public interface SerializerInterface
{
    String serialize(Object object);
    <T>T deSerialize(String string, Class<T> classOfT);
    <T>T deSerialize(String string, TypeToken<T> typeToken);
}

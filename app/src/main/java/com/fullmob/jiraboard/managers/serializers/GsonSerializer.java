package com.fullmob.jiraboard.managers.serializers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.inject.Inject;

public class GsonSerializer implements SerializerInterface {
    private Gson mGson;

    @Inject
    public GsonSerializer(Gson gson) {
        mGson = gson;
    }

    @Override
    public String serialize(Object object) {
        return mGson.toJson(object);
    }

    @Override
    public <T> T deSerialize(String string, Class<T> classOfT) {
        return mGson.fromJson(string, classOfT);
    }

    @Override
    public <T> T deSerialize(String string, TypeToken<T> typeToken) {
        return mGson.fromJson(string, typeToken.getType());
    }
}

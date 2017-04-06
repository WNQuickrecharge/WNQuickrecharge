package com.optimumnano.quickcharge.utils;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class JsonSerializer {

    private final static Gson gson = new Gson();

    public JsonSerializer() {

    }

    public static <T> String serialize(T t) {
        String json = gson.toJson(t);
        return json;
    }

    @SuppressWarnings("unchecked")
    public static <T> T deSerialize(String json, Class<?> cls) {
        T t = null;

        if (TextUtils.isEmpty(json) || null == cls) {
            throw new AssertionError("json is null");
        }
        try {
            t = (T) gson.fromJson(json, cls);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return t;
    }

    @SuppressWarnings("unchecked")
    public static <T> T deSerialize(String json, Type type) {
        T t = null;

        if (TextUtils.isEmpty(json) || null == type) {
            throw new AssertionError("json is null");
        }
        try {
            t = (T) gson.fromJson(json, type);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return t;
    }


    @SuppressWarnings("unchecked")
    public static <T> T deSerialize(String json) {
        T t = null;
        if (TextUtils.isEmpty(json)) {
            throw new AssertionError("json is null");
        }
        try {
            TypeToken<T> list = new TypeToken<T>() {
            };
            t =  gson.fromJson(json, list.getType());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return t;
    }

//    public <T, U> Map<T, U> deserialize(JsonElement json, Type typeOfT, Type typeOfU, JsonDeserializationContext context)
//            throws JsonParseException {
//
//        if (!json.isJsonObject()) {
//            return null;
//        }
//
//        JsonObject jsonObject = json.getAsJsonObject();
//        Set<Map.Entry<String, JsonElement>> jsonEntrySet = jsonObject.entrySet();
//        Map<T, U> deserializedMap = new HashMap<T, U>();
//
//        for (Map.Entry<String, JsonElement> entry : jsonEntrySet) {
//            try {
//                U value = context.deserialize(entry.getValue(), typeOfU);
//                deserializedMap.put((T) entry.getKey(), value);
//            } catch (Exception ex) {
//                ex.printStackTrace();
////                logger.info("Could not deserialize map.", ex);
//            }
//        }
//
//        return deserializedMap;
//    }
}

package com.hans.aggreation.webadmin.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JacksonUtil {
    private static ObjectMapper mapper = new ObjectMapper();
    static {
        // when parse json to object，ignore the properties of json that don't exist in the object
        // otherwise it probably throw UnrecognizedPropertyException
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 在序列化时日期格式默认为 yyyy-MM-dd'T'HH:mm:ss.SSSZ
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,false);
        //在序列化时忽略值为 null 的属性
//        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        //忽略值为默认值的属性
//        mapper.setDefaultPropertyInclusion(JsonInclude.Include.NON_DEFAULT);
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    }

    public static <T> List<T> parseArray(String json, Class<T> clazz) {
        try {
            if (json==null || json.isBlank()) return null;
            JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, clazz);
            return mapper.readValue(json, javaType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     *
     * @param obj
     * @param clazz
     * @return
     * @param <T>
     */
    public static <T> T parse(Object obj, Class<T> clazz) {
        try {
            if (obj==null) return null;
            if (obj instanceof String) {
                return mapper.readValue(obj.toString(), clazz);
            }
            String json = mapper.writeValueAsString(obj);
            return mapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * @param obj
     * @return
     */
    public static String toJson(Object obj) {
        return toJson(obj, false);
    }
    public static String toPrettyJson(Object obj) {
        return toJson(obj, true);
    }
    private static String toJson(Object obj, boolean pretty) {
        try {
            if (pretty) {
                return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
            }
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static Map map(Object obj) {
        try {
            if (obj==null) return null;
            if (obj instanceof String) {
                return mapper.readValue(obj.toString(), new TypeReference<>() {});
            }
            return mapper.readValue(toJson(obj), new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}

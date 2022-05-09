/* (C)2022 */
package com.advendio.marketplace.openservice.utils;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Map;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.json.JSONObject;
import org.springframework.util.ObjectUtils;

/** Support Function JSON utility */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JsonUtils {
    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setSerializationInclusion(Include.NON_NULL);
    }

    /**
     * Get Object JsonNode From Json String
     *
     * @param jsonString
     * @return
     * @throws IOException
     */
    public static JsonNode getJsonNodeFromJsonString(String jsonString) throws IOException {
        JsonNode retval = null;
        if (!ObjectUtils.isEmpty(jsonString)) {
            retval = objectMapper.readTree(jsonString);
        }
        return retval;
    }

    /**
     * Get Json String From Object With Pretty Printer
     *
     * @param obj
     * @return
     * @throws IOException
     */
    public static String getJsonStringFromObjectWithPrettyPrinter(Object obj) throws IOException {
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
    }

    /**
     * Get Map From Object
     *
     * @param obj
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> getMapFromObject(Object obj) {
        return objectMapper.convertValue(obj, Map.class);
    }

    /**
     * Convert object to map
     *
     * @param obj
     * @return
     * @throws JsonProcessingException
     */
    public static String mapToJson(Object obj) throws JsonProcessingException {
        return objectMapper.writeValueAsString(obj);
    }

    /**
     * Convert JSON string to object
     *
     * @param <T>
     * @param json
     * @param clazz
     * @return
     * @throws JsonProcessingException
     */
    public static <T> T mapFromJson(String json, Class<T> clazz) throws JsonProcessingException {
        return objectMapper.readValue(json, clazz);
    }

    /**
     * Data format Is JSON have key file is not empty
     *
     * @param dataJsonStr
     * @return
     */
    public static JSONObject getValidJsonWithExistKey(String dataJsonStr, String key) {
        if (ObjectUtils.isEmpty(dataJsonStr)) {
            return null;
        }
        JSONObject dataJson = new JSONObject(dataJsonStr);
        if (!dataJson.has(key) || ObjectUtils.isEmpty(dataJson.getString(key))) {
            return null;
        }
        return dataJson;
    }
}

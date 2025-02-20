package org.FipeApp.Services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import java.util.List;

public class JsonToObject {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    // Converte {} para um objeto
    public static <T> T converterObject(String json, Class<T> clazz) throws JsonProcessingException {
        return objectMapper.readValue(json, clazz);
    }

    // Converte de [{},{},{}] para uma lista de objetos
    public static <T> List<T> converterList(String json, Class<T> clazz) throws JsonProcessingException {
        CollectionType listType = objectMapper.getTypeFactory().constructCollectionType(List.class, clazz);
        return objectMapper.readValue(json, listType);
    }

    // Converte {[{},{},{}]} para uma lista de objetos
    public static  <T> List<T> converterObjList(String json, Class<T> clazz, String nodeName) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(json);
        JsonNode dataNode = rootNode.get(nodeName);
        return objectMapper.readValue(dataNode.toString(), objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
    }
}
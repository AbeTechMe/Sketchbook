package com.movies.screenMatch.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DataConvert {

     private static final ObjectMapper objectMapper = new ObjectMapper();

    // convert JSON to Record
    public static <T> T converter(String json, Class<T> recordClass) throws JsonProcessingException {
        return objectMapper.readValue(json, recordClass);
    }
}

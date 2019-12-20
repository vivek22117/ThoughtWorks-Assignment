package com.ddsolutions.iot.equipment.data.utility;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

@Component
public class JsonUtility {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtility.class);
    private ObjectMapper objectMapper;

    public JsonUtility() {
        this.objectMapper = new ObjectMapper().configure(FAIL_ON_UNKNOWN_PROPERTIES, false);
    }


    public <T> String convertToString(T object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException ex) {
            LOGGER.error("Unable to convert into string.." + ex.getMessage());
        }
        return null;
    }

    public <T> T convertFromJson(String json, Class<T> type) {
        try {
            return objectMapper.readValue(json, type);
        } catch (IOException ex) {
            LOGGER.error("Unable to covert from json.." + ex.getMessage());
        }
        return null;
    }
}

package com.assignment.backendapp.utils;

import static com.assignment.backendapp.exceptions.ErrorCode.DATA_PARSE_ERROR;

import com.assignment.backendapp.exceptions.DataParseException;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;

@UtilityClass
public class JSONUtils {
  private static final ObjectMapper mapper;

  public static <T> T fromJson(String serializedString, Class<T> toClass) {
    try {
      return mapper.readValue(serializedString, toClass);
    } catch (JsonProcessingException ex) {
      throw new DataParseException(DATA_PARSE_ERROR, "Unable to parse data");
    }
  }

  static {
    mapper =
        (new ObjectMapper((new JsonFactory()).enable(JsonGenerator.Feature.IGNORE_UNKNOWN)))
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .setSerializationInclusion(JsonInclude.Include.NON_NULL);
  }
}
